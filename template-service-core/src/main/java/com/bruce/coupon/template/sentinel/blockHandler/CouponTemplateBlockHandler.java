package com.bruce.coupon.template.sentinel.blockHandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.bruce.coupon.template.domain.CouponTemplateInfo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@Slf4j
public class CouponTemplateBlockHandler {

    /**
     * CouponTemplateController getTemplate 流控处理
     * @param id
     * @param blockException
     * @return
     */
    public static CouponTemplateInfo getTemplate_block(Long id , BlockException blockException){
        log.info("getTemplate 接口限流");
        return CouponTemplateInfo.builder().build();
    }


    /**
     * CouponTemplateController getTemplateInBatch 流控处理
     * @param ids
     * @param exception
     * @return
     */
    public static Map<Long, CouponTemplateInfo> getTemplateInBatch_block(
            Collection<Long> ids, BlockException exception) {
        log.info("接口被限流");
        return Maps.newHashMap();
    }
}
