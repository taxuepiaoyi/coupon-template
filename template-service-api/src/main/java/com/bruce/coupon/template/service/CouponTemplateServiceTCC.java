package com.bruce.coupon.template.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface CouponTemplateServiceTCC {

    @TwoPhaseBusinessAction(name = "deleteCouponTemplateById" ,commitMethod = "" ,rollbackMethod = "")
    Boolean deleteCouponTemplateById(@BusinessActionContextParameter Long id) ;

    Boolean deleteCouponTemplateByIdCommit(BusinessActionContext context) ;

    Boolean deleteCouponTemplateByIdRollback(BusinessActionContext context) ;
}
