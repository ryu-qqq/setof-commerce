package com.setof.connectly.module.notification.dto.order;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOrderSheet {
    private long paymentId;
    private long paymentAmount;
    private long orderId;
    private long orderAmount;
    private long productGroupId;
    private String productGroupName;
    private String csPhoneNumber;
    private String phoneNumber;
    private List<Double> usedMileageAmounts;
    private OrderStatus orderStatus;
    private String reason;
    private String detailReason;

    public ProductOrderSheet(
            long paymentId, long paymentAmount, String productGroupName, String phoneNumber) {
        this.paymentId = paymentId;
        this.paymentAmount = paymentAmount;
        this.productGroupName = productGroupName;
        this.phoneNumber = phoneNumber;
    }

    @QueryProjection
    public ProductOrderSheet(
            long paymentId,
            long paymentAmount,
            long orderId,
            long orderAmount,
            long productGroupId,
            String productGroupName,
            String csPhoneNumber,
            String phoneNumber,
            List<Double> usedMileageAmounts,
            OrderStatus orderStatus) {
        this.paymentId = paymentId;
        this.paymentAmount = paymentAmount;
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.csPhoneNumber = csPhoneNumber;
        this.phoneNumber = phoneNumber;
        this.usedMileageAmounts = usedMileageAmounts;
        this.orderStatus = orderStatus;
    }

    public long getUsedMileageAmounts() {
        return orderAmount
                - (long) usedMileageAmounts.stream().mapToDouble(Double::longValue).sum();
    }

    public String getSubStringProductGroupName() {
        if (productGroupName.length() > 14) {
            return productGroupName.substring(0, 10) + "...";
        }
        return productGroupName;
    }

    public String getReason() {
        if (reason.length() > 14) {
            return reason.substring(0, 10) + "...";
        }
        return reason;
    }

    public String getDetailReason() {
        if (detailReason.length() > 14) {
            return detailReason.substring(0, 10) + "...";
        }
        return detailReason;
    }

    public void setClaimReason(String reason, String detailReason) {
        setReason(reason);
        setDetailReason(detailReason);
    }

    private void setReason(String reason) {
        this.reason = reason;
    }

    private void setDetailReason(String detailReason) {
        this.detailReason = detailReason;
    }
}
