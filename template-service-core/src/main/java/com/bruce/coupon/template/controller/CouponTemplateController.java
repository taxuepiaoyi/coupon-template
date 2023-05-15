package com.bruce.coupon.template.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.bruce.coupon.template.domain.CouponTemplateInfo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("coupon-template")
public class CouponTemplateController {


    @GetMapping("/getTemplate")
    @SentinelResource(value = "getTemplate")
    public CouponTemplateInfo getTemplate(@RequestParam("id") Long id){
        return CouponTemplateInfo.builder().build();
    }

    @GetMapping("/getBatch")
    @SentinelResource(value = "getTemplateInBatch", blockHandler = "getTemplateInBatch_block")
    public Map<Long, CouponTemplateInfo> getTemplateInBatch(
            @RequestParam("ids") Collection<Long> ids) {
        return Maps.newHashMap() ;
    }
    public Map<Long, CouponTemplateInfo> getTemplateInBatch_block(
            Collection<Long> ids, BlockException exception) {
        log.info("接口被限流");
        return Maps.newHashMap();
    }
}
