package com.ryuqq.setof.adapter.in.rest.v1.pg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PortOneWebHookV1ApiRequest(
    @Schema(description = "아임포트 고유 ID", example = "imp_123456789") @JsonProperty("imp_uid")
    String impUid,
    @Schema(description = "가맹점 주문 ID", example = "merchant_123456")
    @JsonProperty("merchant_uid")
    String merchantUid
) {
}
