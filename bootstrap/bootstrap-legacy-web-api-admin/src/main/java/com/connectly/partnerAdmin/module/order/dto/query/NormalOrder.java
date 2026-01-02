package com.connectly.partnerAdmin.module.order.dto.query;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("normalOrder")
public class NormalOrder extends AbstractUpdateOrder{

    public NormalOrder(long orderId, @NotNull(message = "주문 상태 값은 필수입니다.") OrderStatus orderStatus, Boolean byPass) {
        super(orderId, orderStatus, byPass);
    }

}
