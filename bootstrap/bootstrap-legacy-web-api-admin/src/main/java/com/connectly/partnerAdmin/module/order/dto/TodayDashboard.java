package com.connectly.partnerAdmin.module.order.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodayDashboard {
    private Long orderCount;
    private Long claimCount;
    private Long orderInquiryCount;
    private Long productInquiryCount;

}
