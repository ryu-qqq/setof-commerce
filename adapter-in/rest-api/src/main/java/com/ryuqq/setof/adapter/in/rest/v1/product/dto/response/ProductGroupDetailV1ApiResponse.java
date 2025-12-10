package com.ryuqq.setof.adapter.in.rest.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * V1 상품 그룹 상세 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 상세 응답")
public record ProductGroupDetailV1ApiResponse(
        @Schema(description = "상품 그룹 정보") ProductGroupV1ApiResponse productGroup,
        @Schema(description = "상품 고지 정보") ProductNoticeV1ApiResponse productNotices,
        @Schema(description = "상품 그룹 이미지 목록") Set<ProductImageV1ApiResponse> productGroupImages,
        @Schema(description = "상품 목록") Set<ProductV1ApiResponse> products,
        @Schema(description = "카테고리 목록") Set<ProductCategoryV1ApiResponse> categories,
        @Schema(description = "상세 설명", example = "상품 상세 설명입니다.") String detailDescription,
        @Schema(description = "마일리지 적립률", example = "1.0") Double mileageRate,
        @Schema(description = "예상 마일리지 금액", example = "900.0") Double expectedMileageAmount,
        @Schema(description = "찜 여부", example = "true") Boolean isFavorite,
        @Schema(description = "이벤트 상품 타입", example = "NORMAL") String eventProductType) {}
