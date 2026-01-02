package com.connectly.partnerAdmin.module.discount.mapper;

import com.connectly.partnerAdmin.module.discount.dto.query.CreateDiscount;
import com.connectly.partnerAdmin.module.discount.dto.query.CreateDiscountDetails;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.entity.embedded.DiscountDetails;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapperImpl implements DiscountMapper {


    @Override
    public DiscountPolicy toEntity(CreateDiscount createDiscount) {
        return new DiscountPolicy(toDetails(createDiscount.getDiscountDetails()));
    }

    private DiscountDetails toDetails(CreateDiscountDetails createDiscountDetails) {
        return DiscountDetails.builder()
                .discountPolicyName(createDiscountDetails.getDiscountPolicyName())
                .discountType(createDiscountDetails.getDiscountType())
                .publisherType(createDiscountDetails.getPublisherType())
                .issueType(createDiscountDetails.getIssueType())
                .discountLimitYn(createDiscountDetails.getDiscountLimitYn())
                .maxDiscountPrice(createDiscountDetails.getMaxDiscountPrice())
                .shareYn(createDiscountDetails.getShareYn())
                .shareRatio(createDiscountDetails.getShareRatio())
                .discountRatio(createDiscountDetails.getDiscountRatio())
                .policyStartDate(createDiscountDetails.getPolicyStartDate())
                .policyEndDate(createDiscountDetails.getPolicyEndDate())
                .memo(createDiscountDetails.getMemo())
                .priority(createDiscountDetails.getPriority())
                .activeYn(createDiscountDetails.getActiveYn())
                .build();


    }


}
