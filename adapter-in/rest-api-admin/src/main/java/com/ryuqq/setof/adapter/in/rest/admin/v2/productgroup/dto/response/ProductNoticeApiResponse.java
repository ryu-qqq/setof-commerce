package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/** 상품 고시정보 API 응답 DTO. */
@Schema(description = "상품 고시정보 응답")
public record ProductNoticeApiResponse(
        @Schema(description = "고시정보 ID", example = "1") Long id,
        @Schema(description = "고시정보 항목 목록") List<ProductNoticeEntryApiResponse> entries,
        @Schema(description = "생성일시 (ISO 8601)") String createdAt,
        @Schema(description = "수정일시 (ISO 8601)") String updatedAt) {}
