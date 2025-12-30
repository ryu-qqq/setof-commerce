package com.connectly.partnerAdmin.module.discount.filter;


import com.connectly.partnerAdmin.module.common.enums.PeriodType;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.enums.PublisherType;
import com.connectly.partnerAdmin.module.common.filter.SearchAndDateFilter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DiscountFilter extends SearchAndDateFilter {

    private Long discountPolicyId;
    private PeriodType periodType;
    private Yn activeYn;
    private PublisherType publisherType;
    private IssueType issueType;
    private Long userId;


}
