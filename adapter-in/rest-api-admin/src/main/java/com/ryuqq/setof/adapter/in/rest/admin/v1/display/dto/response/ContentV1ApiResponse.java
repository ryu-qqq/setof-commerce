package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 컨텐츠 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "컨텐츠 응답")
public record ContentV1ApiResponse(
        @Schema(description = "컨텐츠 ID", example = "1") Long contentId,
        @Schema(description = "컨텐츠 제목", example = "신상품 소개") String title,
        @Schema(description = "컨텐츠 내용", example = "새로운 상품을 소개합니다.") String content,
        @Schema(description = "표시 여부", example = "true") Boolean displayYn,
        @Schema(description = "생성일시") LocalDateTime createdAt,
        @Schema(description = "수정일시") LocalDateTime updatedAt) {}
