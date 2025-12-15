package com.setof.connectly.module.discount.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.discount.enums.DiscountType;
import com.setof.connectly.module.discount.enums.IssueType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountCacheDto {

    private long discountPolicyId;
    private DiscountType discountType;
    private IssueType issueType;
    private Yn discountLimitYn;
    public long maxDiscountPrice;
    private double discountRatio;
    private LocalDateTime policyStartDate;
    private LocalDateTime policyEndDate;
    private int priority;
    private long targetId;

    private double shareRatio;

    @Builder
    @QueryProjection
    public DiscountCacheDto(
            long discountPolicyId,
            DiscountType discountType,
            IssueType issueType,
            Yn discountLimitYn,
            long maxDiscountPrice,
            double discountRatio,
            LocalDateTime policyStartDate,
            LocalDateTime policyEndDate,
            int priority,
            long targetId,
            double shareRatio) {
        this.discountPolicyId = discountPolicyId;
        this.discountType = discountType;
        this.issueType = issueType;
        this.discountLimitYn = discountLimitYn;
        this.maxDiscountPrice = maxDiscountPrice;
        this.discountRatio = discountRatio;
        this.policyStartDate = policyStartDate;
        this.policyEndDate = policyEndDate;
        this.priority = priority;
        this.targetId = targetId;
        this.shareRatio = shareRatio;
    }

    @JsonIgnore
    public boolean isPriceType() {
        return discountType.isPriceType();
    }

    public String makeCacheKey() {
        return RedisKey.DISCOUNT.generateKey(issueType.name() + targetId);
    }
}
