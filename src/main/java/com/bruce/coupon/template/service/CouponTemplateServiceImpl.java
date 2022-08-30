package com.bruce.coupon.template.service;

import com.bruce.coupon.template.domain.CouponTemplateInfo;
import com.bruce.coupon.template.domain.PagedCouponTemplateInfo;
import com.bruce.coupon.template.domain.TemplateSearchParams;

import java.util.Collection;
import java.util.Map;

public class CouponTemplateServiceImpl implements CouponTemplateService{
    @Override
    public CouponTemplateInfo createTemplate(CouponTemplateInfo request) {
        return null;
    }

    @Override
    public CouponTemplateInfo cloneTemplate(Long templateId) {
        return null;
    }

    @Override
    public PagedCouponTemplateInfo search(TemplateSearchParams request) {
        return null;
    }

    @Override
    public CouponTemplateInfo loadTemplateInfo(Long id) {
        return null;
    }

    @Override
    public void deleteTemplate(Long id) {

    }

    @Override
    public Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids) {
        return null;
    }
}
