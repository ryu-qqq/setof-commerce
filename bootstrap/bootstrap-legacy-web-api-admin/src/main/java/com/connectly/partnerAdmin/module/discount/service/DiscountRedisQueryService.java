package com.connectly.partnerAdmin.module.discount.service;

import com.connectly.partnerAdmin.module.discount.core.DiscountInfo;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;

import java.util.List;

public interface DiscountRedisQueryService {

    void saveDiscountCache(DiscountInfo baseDiscountInfo);
    void updateDiscountCache(DiscountPolicy discountPolicy, List<DiscountTarget> savedDiscountTargets);

}
