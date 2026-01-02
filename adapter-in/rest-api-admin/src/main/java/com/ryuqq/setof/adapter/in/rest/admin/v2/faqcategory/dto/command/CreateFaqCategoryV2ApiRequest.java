package com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * FAQ 카테고리 생성 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "FAQ 카테고리 생성 요청")
public record CreateFaqCategoryV2ApiRequest(
        @Schema(description = "카테고리 코드", example = "ORDER") @NotBlank String code,
        @Schema(description = "카테고리명", example = "주문/결제") @NotBlank String name,
        @Schema(description = "설명", example = "주문 및 결제 관련 FAQ") String description,
        @Schema(description = "표시 순서", example = "1", defaultValue = "0") @Min(0)
                int displayOrder) {

    public CreateFaqCategoryV2ApiRequest {
        if (displayOrder < 0) {
            displayOrder = 0;
        }
    }
}
