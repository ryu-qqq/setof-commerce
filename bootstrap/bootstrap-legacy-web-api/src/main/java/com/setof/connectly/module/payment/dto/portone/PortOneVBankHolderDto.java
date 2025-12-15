package com.setof.connectly.module.payment.dto.portone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PortOneVBankHolderDto {

    @JsonProperty("bank_code")
    private String bankCode;

    @JsonProperty("bank_num")
    private String bankNum;
}
