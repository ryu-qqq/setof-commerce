package com.setof.connectly.module.order.dto.order;

import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;

public interface RefundOrderSheet {

    boolean isAllCanceled();

    double getExpectedRefundAmount();

    double getExpectedRefundMileage();

    long getOrderId();

    OrderStatus getOrderStatus();

    long getOrderAmount();

    RefundAccountInfo getRefundAccountInfo();

    void setRefundAccountInfo(RefundAccountInfo refundAccountInfo);

    String getReason();

    String getDetailReason();
}
