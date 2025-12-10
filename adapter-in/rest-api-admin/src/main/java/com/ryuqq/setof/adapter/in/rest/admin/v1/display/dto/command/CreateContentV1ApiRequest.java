package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * V1 컨텐츠 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "컨텐츠 생성 요청")
public record CreateContentV1ApiRequest(
        @Schema(description = "컨텐츠 제목", example = "신상품 소개") @NotBlank String title,
        @Schema(description = "컨텐츠 내용", example = "새로운 상품을 소개합니다.") String content,
        @Schema(description = "표시 여부", example = "true") @NotNull Boolean displayYn) {
}
