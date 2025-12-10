package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import com.ryuqq.setof.adapter.in.rest.v1.product.dto.response.ProductGroupThumbnailV1ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 브랜드 컴포넌트 상세 Response
 *
 * <p>브랜드 컴포넌트의 상세 정보를 반환하는 응답 DTO입니다.
 *
 * @param componentId 컴포넌트 ID
 * @param componentName 컴포넌트명
 * @param displayOrder 전시 순서
 * @param viewExtensionId 뷰 확장 ID
 * @param componentType 컴포넌트 타입
 * @param displayPeriod 전시 기간
 * @param displayYn 전시 여부
 * @param brandComponentId 브랜드 컴포넌트 ID
 * @param brandList 브랜드 목록
 * @param categoryId 카테고리 ID
 * @param exposedProducts 노출 상품 수
 * @param productGroupThumbnails 상품 그룹 썸네일 목록
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "브랜드 컴포넌트 상세 응답")
public record BrandComponentDetailV1ApiResponse(
        @Schema(description = "컴포넌트 ID", example = "1") Long componentId,
        @Schema(description = "컴포넌트명", example = "브랜드 컴포넌트") String componentName,
        @Schema(description = "전시 순서", example = "1") int displayOrder,
        @Schema(description = "뷰 확장 ID", example = "100") Long viewExtensionId,
        @Schema(description = "컴포넌트 타입", example = "BRAND") String componentType,
        @Schema(description = "전시 기간")
                SubComponentV1ApiResponse.DisplayPeriodV1ApiResponse displayPeriod,
        @Schema(description = "전시 여부", example = "Y") String displayYn,
        @Schema(description = "브랜드 컴포넌트 ID", example = "1") Long brandComponentId,
        @Schema(description = "브랜드 목록") List<BrandV1ApiResponse> brandList,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "노출 상품 수", example = "10") int exposedProducts,
        @Schema(description = "상품 그룹 썸네일 목록")
                List<ProductGroupThumbnailV1ApiResponse> productGroupThumbnails)
        implements SubComponentV1ApiResponse {

    @Schema(description = "브랜드 응답")
    public record BrandV1ApiResponse(
            @Schema(description = "브랜드 ID", example = "1") Long brandId,
            @Schema(description = "브랜드명", example = "브랜드A") String brandName) {}
}
