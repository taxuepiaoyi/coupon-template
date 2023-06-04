package com.bruce.coupon.template.service.impl;

import com.bruce.coupon.template.dao.CouponTemplateDao;
import com.bruce.coupon.template.dao.entity.CouponTemplate;
import com.bruce.coupon.template.service.CouponTemplateServiceTCC;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CouponTemplateServiceTCCImpl implements CouponTemplateServiceTCC {

    @Autowired
    private CouponTemplateDao templateDao ;

    @Override
    public Boolean deleteCouponTemplateById(Long id) {
        log.info("CouponTemplateServiceTCC.....deleteCouponTemplateById....id = {}" ,id);
        CouponTemplate filter = CouponTemplate.builder()
                .available(true)
                .locked(false)
                .id(id)
                .build();

        CouponTemplate template = templateDao.findAll(Example.of(filter))
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Template Not Found"));

        template.setLocked(true);
        templateDao.save(template);
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteCouponTemplateByIdCommit(BusinessActionContext context) {
        Long id = Long.parseLong(context.getActionContext("id").toString());

        CouponTemplate template = templateDao.findById(id).get();

        template.setLocked(false);
        template.setAvailable(false);
        templateDao.save(template);

        log.info("TCC committed");
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteCouponTemplateByIdRollback(BusinessActionContext context) {
        Long id = Long.parseLong(context.getActionContext("id").toString());
        Optional<CouponTemplate> templateOption = templateDao.findById(id);

        if (templateOption.isPresent()) {
            CouponTemplate template = templateOption.get();
            template.setLocked(false);
            templateDao.save(template);
        }
        log.info("TCC cancel");

        return Boolean.TRUE;
    }
}
