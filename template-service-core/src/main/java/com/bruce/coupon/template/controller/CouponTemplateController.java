package com.bruce.coupon.template.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.bruce.coupon.template.domain.CouponTemplateInfo;
import com.bruce.coupon.template.sentinel.blockHandler.CouponTemplateBlockHandler;
import com.bruce.coupon.template.sentinel.fallback.CouponTemplateSentinelFallback;
import com.bruce.coupon.template.service.CouponTemplateService;
import com.bruce.coupon.template.service.CouponTemplateServiceTCC;
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

    @Autowired
    private CouponTemplateServiceTCC templateServiceTCC ;

    /**
     * 新增优惠券
     * @param request
     * @return
     */
    @PostMapping("/createTemplate")
    public CouponTemplateInfo createTemplate(@RequestBody CouponTemplateInfo request){
        if(request == null){
            log.info("createTemplate request is null");
            return null ;
        }
        return templateService.createTemplate(request) ;
    }


    /**
     * 根据templateId获取优惠券信息
     * @param id
     * @return
     * @throws Exception
     */
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

        return templateService.loadTemplateInfo(id);
    }

    /**
     * 批量获取优惠券信息
     * @param ids
     * @return
     */
    @GetMapping("/getBatch")
    @SentinelResource(value = "getTemplateInBatch", blockHandlerClass = CouponTemplateBlockHandler.class)
    public Map<Long, CouponTemplateInfo> getTemplateInBatch(
            @RequestParam("ids") Collection<Long> ids) {
        return templateService.getTemplateInfoMap(ids) ;
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

    /**
     * 使用TCC模式删除优惠券模板
     * @param templateId
     * @return
     */
    @DeleteMapping("/deleteCouponTemplateTCC")
    public  Boolean deleteCouponTemplateTCC(@RequestParam("templateId") Long templateId){
        return templateServiceTCC.deleteCouponTemplateById(templateId) ;
    }
}
