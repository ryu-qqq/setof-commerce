package com.connectly.partnerAdmin.module.portone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PortOneVBankHolderResponse(
        @JsonProperty("bank_holder")
        String bankHolder
){}
