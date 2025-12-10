package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 컨텐츠 상품 그룹 섬네일 Response
 *
 * <p>상품 그룹 목록에서 사용하는 요약 정보 응답 DTO입니다.
 *
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 판매자 ID
 * @param productGroupName 상품 그룹명
 * @param brand 상품 그룹 브랜드
 * @param productImageUrl 썸네일 이미지 URL
 * @param price 상품 그룹 가격
 * @param insertDate 상품 등록 시간
 * @param reviewCount 리뷰 개수
 * @param averageRating 리뷰 평점
 * @param score 상품 평점
 * @param isFavorite 찜 여부
 * @param productStatus 품절 여부
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "컨텐츠 상품 그룹 요약 응답")
public record ContentProductGroupThumbnailV1ApiResponse(
    @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
    @Schema(description = "셀러 ID", example = "12345") Long sellerId,
    @Schema(description = "상품 그룹명", example = "베이직 티셔츠") String productGroupName,

    @Schema(description = "브랜드 정보") ProductGroupBrandV1ApiResponse brand,

    @Schema(description = "썸네일 이미지 URL", example = "https://cdn.example.com/image.jpg")
    String productImageUrl,

    @Schema(description = "상품 그룹 가격") ProductGroupPriceV1ApiResponse price,

    @Schema(description = "상품 등록 시간", example = "2024-12-30 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String insertDate,
    @Schema(description = "리뷰 평점 (0.0~5.0)", example = "4.5") Double averageRating,


    @Schema(description = "리뷰 개수", example = "150") Integer reviewCount,
    @Schema(description = "상품 평점 (0.0~5.0)", example = "4.5") Double score,
    @Schema(description = "상품 찜 여부", example = "false") boolean isFavorite,
    @Schema(description = "상품 그룹 상품 상태") ProductGroupStatusV1ApiResponse productStatus

) {

    @Schema(description = "컨텐츠 상품 브랜드 정보")
    public record ProductGroupBrandV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "100") Long brandId,
        @Schema(description = "브랜드명", example = "브랜드A") String brandName
    ){}

    @Schema(description = "컨텐츠 상품 그룹 가격")
    public record ProductGroupPriceV1ApiResponse(
        @Schema(description = "정가", example = "29000") Long regularPrice,
        @Schema(description = "정가", example = "29000") Long currentPrice,
        @Schema(description = "정가", example = "29000") Long salePrice,
        @Schema(description = "할인가", example = "19000") Long directDiscountPrice,
        @Schema(description = "할인율 (%)", example = "34") Integer discountRate,
        @Schema(description = "할인율 (%)", example = "34") Integer directDiscountRate
    ){}

    @Schema(description = "컨텐츠 상품 그룹 상품 상태")
    public record ProductGroupStatusV1ApiResponse(
        @Schema(description = "품절 여부", example = "N")  String soldOutYn,
        @Schema(description = "전시 여부", example = "Y")  String displayYn
    ){}

}
