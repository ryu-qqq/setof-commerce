package com.setof.connectly.module.payment.dto.portone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PortOneVBankHolderResponse {

    @JsonProperty("bank_holder")
    private String bankHolder;
}
