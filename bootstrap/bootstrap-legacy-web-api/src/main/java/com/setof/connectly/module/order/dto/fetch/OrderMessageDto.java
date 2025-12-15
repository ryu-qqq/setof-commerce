package com.setof.connectly.module.order.dto.fetch;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMessageDto {
    private long orderId;
    private long orderAmount;
    private long productGroupId;
    private String productGroupName;
    private String csPhoneNumber;
    private String phoneNumber;
    private List<Double> usedMileageAmounts;

    @QueryProjection
    public OrderMessageDto(
            long orderId,
            long orderAmount,
            long productGroupId,
            String productGroupName,
            String csPhoneNumber,
            String phoneNumber,
            List<Double> usedMileageAmounts) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.csPhoneNumber = csPhoneNumber;
        this.phoneNumber = phoneNumber;
        this.usedMileageAmounts = usedMileageAmounts;
    }

    public long getUsedMileageAmounts() {
        return orderAmount
                - (long) usedMileageAmounts.stream().mapToDouble(Double::longValue).sum();
    }

    public String getProductGroupName() {
        if (productGroupName.length() > 14) {
            return productGroupName.substring(0, 10) + "...";
        }

        return productGroupName;
    }
}
