package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/** 반품 불가 사유 API 응답 DTO. */
@Schema(description = "반품 불가 사유 응답")
public record NonReturnableConditionApiResponse(
        @Schema(description = "사유 코드", example = "OPENED") String code,
        @Schema(description = "사유 표시명", example = "개봉한 상품") String displayName) {}
