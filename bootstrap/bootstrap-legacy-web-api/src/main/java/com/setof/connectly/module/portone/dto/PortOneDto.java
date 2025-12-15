package com.setof.connectly.module.portone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortOneDto {
    @JsonProperty("imp_uid")
    protected String impUid;

    @JsonProperty("merchant_uid")
    protected String merchantUid;
}
