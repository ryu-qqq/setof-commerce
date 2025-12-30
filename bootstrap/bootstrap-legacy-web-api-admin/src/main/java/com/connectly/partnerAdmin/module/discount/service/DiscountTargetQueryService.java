package com.connectly.partnerAdmin.module.discount.service;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.dto.query.CreateDiscountTarget;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;

import java.util.List;

public interface DiscountTargetQueryService {

    List<DiscountTarget> createDiscountTargets(long discountPolicyId, CreateDiscountTarget createDiscountTarget);
    List<DiscountTarget> updateDiscountTargets(long discountPolicyId, CreateDiscountTarget createDiscountTarget);

    void updateDiscountTargetUseYn(List<DiscountPolicy> discountPolicies, Yn activeYn);

}
