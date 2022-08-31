package com.bruce.coupon.template.converter;

import com.bruce.coupon.template.dao.entity.CouponTemplate;
import com.bruce.coupon.template.domain.CouponTemplateInfo;

public class CouponTemplateConverter {
    public static CouponTemplateInfo convertToTemplateInfo(CouponTemplate template) {

        return CouponTemplateInfo.builder()
                .id(template.getId())
                .name(template.getName())
                .desc(template.getDescription())
                .type(template.getCategory().getCode())
                .shopId(template.getShopId())
                .available(template.getAvailable())
                .rule(template.getRule())
                .build();
    }
}
