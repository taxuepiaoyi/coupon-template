package com.bruce.coupon.template.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bruce.coupon.template.converter.CouponTemplateConverter;
import com.bruce.coupon.template.dao.CouponTemplateDao;
import com.bruce.coupon.template.dao.entity.CouponTemplate;
import com.bruce.coupon.template.domain.CouponTemplateInfo;
import com.bruce.coupon.template.domain.PagedCouponTemplateInfo;
import com.bruce.coupon.template.domain.TemplateSearchParams;
import com.bruce.coupon.template.enums.CouponType;
import com.bruce.coupon.template.service.CouponTemplateService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

/**
 * 优惠券模板类相关操作
 */
@Slf4j
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Autowired
    private CouponTemplateDao templateDao;

    /**
     * 创建优惠券模板
     */
    @Override
    public CouponTemplateInfo createTemplate(CouponTemplateInfo request) {
        // 单个门店最多可以创建100张优惠券模板
        if (request.getShopId() != null) {
            Integer count = templateDao.countByShopIdAndAvailable(request.getShopId(), true);
            if (count >= 100) {
                log.error("the totals of coupon template exceeds maximum number");
                throw new UnsupportedOperationException("exceeded the maximum of coupon templates that you can create");
            }
        }

        // 创建优惠券
        CouponTemplate template = CouponTemplate.builder()
                .name(request.getName())
                .description(request.getDesc())
                .category(CouponType.convert(request.getType()))
                .available(true)
                .shopId(request.getShopId())
                .rule(request.getRule())
                .build();
        template = templateDao.save(template);

        return CouponTemplateConverter.convertToTemplateInfo(template);
    }

    // 克隆优惠券
    @Override
    public CouponTemplateInfo cloneTemplate(Long templateId) {
        log.info("cloning template id {}", templateId);
        CouponTemplate source = templateDao.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("invalid template ID"));

        CouponTemplate target = new CouponTemplate();
        BeanUtils.copyProperties(source, target);

        target.setAvailable(true);
        target.setId(null);

        templateDao.save(target);
        return CouponTemplateConverter.convertToTemplateInfo(target);
    }

    @Override
    public PagedCouponTemplateInfo search(TemplateSearchParams request) {
        CouponTemplate example = CouponTemplate.builder()
                .shopId(request.getShopId())
                .category(CouponType.convert(request.getType()))
                .available(request.getAvailable())
                .name(request.getName())
                .build();

        Pageable page = PageRequest.of(request.getPage(), request.getPageSize());
        templateDao.findAll(Example.of(example), page);

        Page<CouponTemplate> result = templateDao.findAll(Example.of(example), page);
        List<CouponTemplateInfo> couponTemplateInfos = result.stream()
                .map(CouponTemplateConverter::convertToTemplateInfo)
                .collect(Collectors.toList());

        PagedCouponTemplateInfo response = PagedCouponTemplateInfo.builder()
                .templates(couponTemplateInfos)
                .page(request.getPage())
                .total(result.getTotalElements())
                .build();

        return response;
    }

    public List<CouponTemplateInfo> searchTemplate(CouponTemplateInfo request) {
        CouponTemplate example = CouponTemplate.builder()
                .shopId(request.getShopId())
                .category(CouponType.convert(request.getType()))
                .available(request.getAvailable())
                .name(request.getName())
                .build();
        // 可以用下面的方式做分页查询
//        Pageable page = PageRequest.of(0, 100);
//        templateDao.findAll(Example.of(example), page);
        List<CouponTemplate> result = templateDao.findAll(Example.of(example));
        return result.stream()
                .map(CouponTemplateConverter::convertToTemplateInfo)
                .collect(Collectors.toList());
    }

    /**
     * 通过ID查询优惠券模板
     */
    @Override
    public CouponTemplateInfo loadTemplateInfo(Long id) {
        Optional<CouponTemplate> template = templateDao.findById(id);
        return template.isPresent() ? CouponTemplateConverter.convertToTemplateInfo(template.get()) : null;
    }

    // 将券无效化
    @Override
    @Transactional
    public Boolean deleteTemplate(Long id) {
        int rows = templateDao.makeCouponUnavailable(id);
        if (rows == 0) {
            throw new IllegalArgumentException("Template Not Found: " + id);
        }
        return Boolean.TRUE ;
    }

    /**
     * 批量读取模板
     */
    @Override
    public Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            log.info("getTemplateInfoMap....ids is empty.....");
            return null ;
        }

        List<CouponTemplate> templates = templateDao.findAllById(ids);

        if(CollectionUtils.isEmpty(templates)){
            log.info("getTemplateInfoMap....templates is empty.....ids:{}" , JSONObject.toJSONString(ids));
            return null ;
        }

        return templates.stream()
                .map(CouponTemplateConverter::convertToTemplateInfo)
                .collect(Collectors.toMap(CouponTemplateInfo::getId, Function.identity()));
    }
}
