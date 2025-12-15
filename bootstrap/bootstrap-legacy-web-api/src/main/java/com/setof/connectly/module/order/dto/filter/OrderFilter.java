package com.setof.connectly.module.order.dto.filter;

import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.payment.dto.filter.PaymentFilter;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderFilter extends PaymentFilter {

    private List<OrderStatus> orderStatusList = new ArrayList<>();
}
