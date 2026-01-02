package com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

/**
 * GNB 수정 요청 DTO
 *
 * @param title GNB 제목
 * @param linkUrl 링크 URL (nullable)
 * @param displayOrder 노출 순서
 * @param displayStartDate 노출 시작일시 (nullable)
 * @param displayEndDate 노출 종료일시 (nullable)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "GNB 수정 요청")
public record UpdateGnbV2ApiRequest(
        @Schema(description = "GNB 제목", example = "홈", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "제목은 필수입니다")
                String title,
        @Schema(description = "링크 URL", example = "/home") String linkUrl,
        @Schema(description = "노출 순서", example = "1") Integer displayOrder,
        @Schema(description = "노출 시작일시") Instant displayStartDate,
        @Schema(description = "노출 종료일시") Instant displayEndDate) {}
