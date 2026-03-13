package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/** 셀러 옵션 값 API 응답 DTO. */
@Schema(description = "셀러 옵션 값 응답")
public record SellerOptionValueApiResponse(
        @Schema(description = "옵션 값 ID", example = "1") Long id,
        @Schema(description = "소속 옵션 그룹 ID", example = "1") Long sellerOptionGroupId,
        @Schema(description = "옵션 값명", example = "블랙") String optionValueName,
        @Schema(description = "정렬 순서", example = "0") int sortOrder) {}
