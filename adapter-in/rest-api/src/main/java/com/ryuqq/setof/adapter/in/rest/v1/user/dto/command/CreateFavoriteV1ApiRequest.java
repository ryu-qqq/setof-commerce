package com.ryuqq.setof.adapter.in.rest.v1.user.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * V1 찜 추가 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "찜 추가 요청")
public record CreateFavoriteV1ApiRequest(
        @Schema(
                        description = "상품 그룹 ID",
                        example = "12345",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "상품 그룹 ID는 필수입니다.")
                @Positive(message = "상품 그룹 ID는 양수여야 합니다.")
                Long productGroupId) {}
