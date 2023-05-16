package com.bruce.coupon.template.sentinel.fallback;

import com.bruce.coupon.template.domain.CouponTemplateInfo;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Slf4j
public class CouponTemplateSentinelFallback {

    /**
     * CouponTemplateController getTemplate 请求的的熔断处理
     * @param id
     * @param throwable
     * @return
     */
    public static CouponTemplateInfo getTemplate(@RequestParam("id") Long id , Throwable throwable){
        log.info("CouponTemplateSentinelFallback........getTemplate.....");
        return CouponTemplateInfo.builder().build();
    }
}
