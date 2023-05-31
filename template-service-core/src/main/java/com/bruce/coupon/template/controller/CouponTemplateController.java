package com.bruce.coupon.template.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.bruce.coupon.template.domain.CouponTemplateInfo;
import com.bruce.coupon.template.sentinel.blockHandler.CouponTemplateBlockHandler;
import com.bruce.coupon.template.sentinel.fallback.CouponTemplateSentinelFallback;
import com.bruce.coupon.template.service.CouponTemplateService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/template")
public class CouponTemplateController {

    @Autowired
    private CouponTemplateService templateService ;


    @GetMapping("/getTemplate")
    @SentinelResource(value = "getTemplate",blockHandlerClass = CouponTemplateBlockHandler.class,fallbackClass = CouponTemplateSentinelFallback.class)
    public CouponTemplateInfo getTemplate(@RequestParam("id") Long id) throws Exception{

        if(id >= 0 && id <= 1000){
            log.info("getTemplate 熔断规则 异常数");
            new RuntimeException("异常数超标了") ;
        }

        if(id >= 2000 && id <= 5000){
            log.info("getTemplate 熔断规则 异常比例");
            new RuntimeException("异常比例超标了") ;
        }

        // 测试熔断规则中的慢调用
        if(id > 10000){
            log.info("getTemplate 慢调用超标了");
            Thread.sleep(1000);
        }

        return CouponTemplateInfo.builder().build();
    }

    @GetMapping("/getBatch")
    @SentinelResource(value = "getTemplateInBatch", blockHandlerClass = CouponTemplateBlockHandler.class)
    public Map<Long, CouponTemplateInfo> getTemplateInBatch(
            @RequestParam("ids") Collection<Long> ids) {
        return Maps.newHashMap() ;
    }

    /**
     * 删除优惠券模板
     * @param templateId
     * @return
     */
    @DeleteMapping("/deleteCouponTemplate")
    public  Boolean deleteCouponTemplate(@RequestParam("templateId") Long templateId){
        return templateService.deleteTemplate(templateId);
    }
}
