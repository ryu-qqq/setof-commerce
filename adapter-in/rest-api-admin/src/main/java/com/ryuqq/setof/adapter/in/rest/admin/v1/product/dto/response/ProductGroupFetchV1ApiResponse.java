package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;

/**
 * V1 상품 그룹 조회 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 조회 응답")
public record ProductGroupFetchV1ApiResponse(
        @Schema(description = "상품 그룹 정보") ProductGroupInfoV1ApiResponse productGroup,
        @Schema(description = "상품 고지 정보") ProductNoticeV1ApiResponse productNotices,
        @Schema(description = "상품 그룹 이미지 목록") List<ProductImageV1ApiResponse> productGroupImages,
        @Schema(description = "상세 설명", example = "상품 상세 설명입니다.") String detailDescription,
        @Schema(description = "카테고리 목록") List<TreeCategoryContextV1ApiResponse> categories,
        @Schema(description = "상품 목록") Set<ProductFetchV1ApiResponse> products) {
}
