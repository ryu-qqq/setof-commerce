package com.ryuqq.setof.adapter.in.rest.admin.v2.productimage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 상품이미지 목록 조회 응답
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품이미지 목록 조회 응답")
public record ProductImageListV2ApiResponse(
        @Schema(description = "이미지 목록") List<ProductImageV2ApiResponse> items,
        @Schema(description = "이미지 개수", example = "5") int totalCount) {

    public static ProductImageListV2ApiResponse of(List<ProductImageV2ApiResponse> items) {
        return new ProductImageListV2ApiResponse(items, items.size());
    }
}
