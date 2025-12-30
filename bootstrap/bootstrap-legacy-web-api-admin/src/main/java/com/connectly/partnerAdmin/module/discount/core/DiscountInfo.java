package com.connectly.partnerAdmin.module.discount.core;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.enums.DiscountType;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;

import java.time.LocalDateTime;

public interface DiscountInfo {

    DiscountType getDiscountType();
    IssueType getIssueType();
    Yn getDiscountLimitYn();
    long getMaxDiscountPrice();
    double getDiscountRatio();
    LocalDateTime getPolicyStartDate();
    LocalDateTime getPolicyEndDate();
    int getPriority();
    long getTargetId();
    double getShareRatio();

}
