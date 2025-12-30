package com.ryuqq.setof.adapter.in.rest.v2.faqcategory.dto.response;

import com.ryuqq.setof.application.faqcategory.dto.response.FaqCategoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FAQ Category V2 API Response
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "FAQ 카테고리 응답")
public record FaqCategoryV2ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") Long id,
        @Schema(description = "카테고리 코드", example = "ORDER") String code,
        @Schema(description = "카테고리명", example = "주문/결제") String name,
        @Schema(description = "설명", example = "주문 및 결제 관련 FAQ") String description,
        @Schema(description = "표시 순서", example = "1") int displayOrder) {

    /**
     * Application Response → API Response 변환
     *
     * @param response Application Response
     * @return API Response
     */
    public static FaqCategoryV2ApiResponse from(FaqCategoryResponse response) {
        return new FaqCategoryV2ApiResponse(
                response.id(),
                response.code(),
                response.name(),
                response.description(),
                response.displayOrder());
    }
}
