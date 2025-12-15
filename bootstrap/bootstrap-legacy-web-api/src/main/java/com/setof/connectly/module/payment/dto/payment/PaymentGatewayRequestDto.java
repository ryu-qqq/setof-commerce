package com.setof.connectly.module.payment.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PaymentGatewayRequestDto {

    private String paymentUniqueId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long paymentId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Long> orderIds;

    private double expectedMileageAmount;
}
