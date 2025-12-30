package com.connectly.partnerAdmin.module.discount.core;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.enums.DiscountType;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseDiscountInfo implements DiscountInfo {

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
    public BaseDiscountInfo(long discountPolicyId, DiscountType discountType, IssueType issueType, Yn discountLimitYn, long maxDiscountPrice, double discountRatio, LocalDateTime policyStartDate, LocalDateTime policyEndDate, int priority, long targetId, double shareRatio) {
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

}
