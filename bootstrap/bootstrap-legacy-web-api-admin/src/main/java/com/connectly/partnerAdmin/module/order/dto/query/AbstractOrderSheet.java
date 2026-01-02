package com.connectly.partnerAdmin.module.order.dto.query;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractOrderSheet implements OrderSheet {


    private Long orderId;
    @NotNull(message = "주문 상태 값은 필수입니다.")
    private OrderStatus orderStatus;

}
