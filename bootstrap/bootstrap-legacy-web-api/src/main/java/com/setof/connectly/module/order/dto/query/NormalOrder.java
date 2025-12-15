package com.setof.connectly.module.order.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("normalOrder")
public class NormalOrder extends AbstractUpdateOrder {

    public boolean saveOrderSnapShot;

    public NormalOrder(
            long paymentId,
            Long orderId,
            @NotNull(message = "주문 상태 값은 필수입니다.") OrderStatus orderStatus,
            boolean saveOrderSnapShot) {
        super(paymentId, orderId, orderStatus);
        this.saveOrderSnapShot = saveOrderSnapShot;
    }
}
