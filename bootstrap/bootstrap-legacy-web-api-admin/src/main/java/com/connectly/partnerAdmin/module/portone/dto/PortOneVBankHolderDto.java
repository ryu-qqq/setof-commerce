package com.connectly.partnerAdmin.module.portone.dto;

import com.connectly.partnerAdmin.module.payment.enums.VBankCode;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PortOneVBankHolderDto(

        @JsonProperty("bank_code")
        String bankCode,
        @JsonProperty("bank_num")
        String bankNum
) {

    public PortOneVBankHolderDto(String bankCode, String bankNum) {
        VBankCode vBankCode = VBankCode.ofDisplayName(bankCode);
        this.bankCode = vBankCode.getCode();
        this.bankNum = bankNum;
    }

}