package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * V1 의류 상세 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "의류 상세 생성 요청")
public record CreateClothesDetailV1ApiRequest(
        @Schema(description = "상품 상태", example = "NEW", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "상품 상태는 필수입니다.")
                String productCondition,
        @Schema(description = "원산지", example = "KR") String origin,
        @Schema(description = "스타일 코드", example = "STYLE001")
                @Length(max = 50, message = "스타일 코드는 50자를 넘을 수 없습니다.")
                String styleCode) {}
