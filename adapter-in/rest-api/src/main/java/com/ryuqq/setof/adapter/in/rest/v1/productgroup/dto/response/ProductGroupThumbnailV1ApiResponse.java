package com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * ProductGroupThumbnailV1ApiResponse - 상품그룹 썸네일 응답 DTO.
 *
 * <p>레거시 ProductGroupThumbnail 기반 변환. 목록 조회(fetchProductGroups), 최근 본 상품(fetchProductGroupLikes),
 * 브랜드별 조회(fetchProductGroupWithBrand), 셀러별 조회(fetchProductGroupWithSeller) 공통 응답 DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>API-DTO-006: 복잡한 구조는 중첩 Record로 표현.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 판매자 ID
 * @param productGroupName 상품그룹명
 * @param brand 브랜드 정보
 * @param productImageUrl 대표 이미지 URL
 * @param price 가격 정보
 * @param insertDate 등록일
 * @param averageRating 평균 평점 (없으면 0.0)
 * @param reviewCount 리뷰 수 (없으면 0)
 * @param score 추천 스코어 (없으면 0.0)
 * @param isFavorite 즐겨찾기 여부
 * @param productStatus 판매 상태
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.product.dto.ProductGroupThumbnail
 */
@Schema(description = "상품그룹 썸네일 응답")
public record ProductGroupThumbnailV1ApiResponse(
        @Schema(description = "상품그룹 ID", example = "1001") long productGroupId,
        @Schema(description = "판매자 ID", example = "50") long sellerId,
        @Schema(description = "상품그룹명", example = "프리미엄 티셔츠") String productGroupName,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(
                        description = "대표 이미지 URL",
                        example = "https://cdn.example.com/images/product/1001_main.jpg")
                String productImageUrl,
        @Schema(description = "가격 정보") PriceResponse price,
        @Schema(description = "등록일", example = "2024-01-15T10:30:00") LocalDateTime insertDate,
        @Schema(description = "평균 평점 (없으면 0.0)", example = "4.5") double averageRating,
        @Schema(description = "리뷰 수 (없으면 0)", example = "128") long reviewCount,
        @Schema(description = "추천 스코어 (없으면 0.0)", example = "0.92") double score,
        @Schema(description = "즐겨찾기 여부", example = "false") boolean isFavorite,
        @Schema(description = "판매 상태") ProductStatusResponse productStatus) {

    /**
     * BrandResponse - 브랜드 정보 응답.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "10") long brandId,
            @Schema(description = "브랜드명", example = "BRAND_A") String brandName) {}

    /**
     * PriceResponse - 가격 정보 응답.
     *
     * @param regularPrice 정가
     * @param currentPrice 현재가
     * @param salePrice 판매가 (할인 적용)
     * @param directDiscountRate 직접 할인율 (%)
     * @param directDiscountPrice 직접 할인 금액
     * @param discountRate 총 할인율 (%)
     */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정가", example = "50000") long regularPrice,
            @Schema(description = "현재가", example = "45000") long currentPrice,
            @Schema(description = "판매가 (할인 적용)", example = "36000") long salePrice,
            @Schema(description = "직접 할인율 (%)", example = "20") int directDiscountRate,
            @Schema(description = "직접 할인 금액", example = "9000") long directDiscountPrice,
            @Schema(description = "총 할인율 (%)", example = "28") int discountRate) {}

    /**
     * ProductStatusResponse - 판매 상태 응답.
     *
     * @param soldOutYn 품절 여부 (Y/N)
     * @param displayYn 노출 여부 (Y/N)
     */
    @Schema(description = "판매 상태")
    public record ProductStatusResponse(
            @Schema(description = "품절 여부 (Y: 품절, N: 정상)", example = "N") String soldOutYn,
            @Schema(description = "노출 여부 (Y: 노출, N: 비노출)", example = "Y") String displayYn) {}
}
