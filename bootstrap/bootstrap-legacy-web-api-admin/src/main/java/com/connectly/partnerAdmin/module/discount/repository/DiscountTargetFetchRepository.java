package com.connectly.partnerAdmin.module.discount.repository;

import com.connectly.partnerAdmin.module.discount.dto.ProductDiscountTarget;
import com.connectly.partnerAdmin.module.discount.dto.SellerDiscountTarget;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiscountTargetFetchRepository {

    List<DiscountTarget> fetchDiscountTargetEntities(List<Long> discountPolicyIds);

    List<ProductDiscountTarget> fetchProductTargets(long discountPolicyId, Pageable pageable);

    List<SellerDiscountTarget> fetchSellerTargets(long discountPolicyId, Pageable pageable);


    JPAQuery<Long> fetchProductTargetCountQuery(long discountPolicyId);

    JPAQuery<Long> fetchSellerTargetCountQuery(long discountPolicyId);


}
