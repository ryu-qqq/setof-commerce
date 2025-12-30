package com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * GNB 응답 DTO
 *
 * @param gnbId GNB ID
 * @param title 제목
 * @param linkUrl 링크 URL
 * @param displayOrder 노출 순서
 * @param status 상태
 * @param displayStartDate 노출 시작일시
 * @param displayEndDate 노출 종료일시
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "GNB 응답")
public record GnbV2ApiResponse(
        @Schema(description = "GNB ID", example = "1") Long gnbId,
        @Schema(description = "제목", example = "홈") String title,
        @Schema(description = "링크 URL", example = "/home") String linkUrl,
        @Schema(description = "노출 순서", example = "1") int displayOrder,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(description = "노출 시작일시") Instant displayStartDate,
        @Schema(description = "노출 종료일시") Instant displayEndDate,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {}
