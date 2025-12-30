package com.connectly.partnerAdmin.module.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDashboardResponse {
    private TodayDashboard todayDashboard;
    private List<OrderStatistics> monthOrderStatistics;

}
