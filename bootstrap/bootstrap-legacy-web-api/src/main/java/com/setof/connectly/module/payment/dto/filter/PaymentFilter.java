package com.setof.connectly.module.payment.dto.filter;

import com.setof.connectly.module.common.filter.SearchAndDateFilter;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentFilter extends SearchAndDateFilter {

    private Long lastDomainId;

    private List<OrderStatus> orderStatusList;

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
