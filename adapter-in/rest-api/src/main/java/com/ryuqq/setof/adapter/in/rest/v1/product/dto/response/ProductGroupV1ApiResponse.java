package com.ryuqq.setof.adapter.in.rest.v1.product.dto.response;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 상품 그룹 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 응답")
public record ProductGroupV1ApiResponse(
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "상품 그룹명", example = "멋진 티셔츠") String productGroupName,
        @Schema(description = "셀러 ID", example = "100") Long sellerId,
        @Schema(description = "셀러명", example = "셀러명") String sellerName,
        @Schema(description = "브랜드 정보") BrandV1ApiResponse brand,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "가격 정보") PriceV1ApiResponse price,
        @Schema(description = "옵션 타입", example = "SIZE_COLOR") String optionType,
        @Schema(description = "의류 상세 정보") ClothesDetailV1ApiResponse clothesDetailInfo,
        @Schema(description = "배송 고지 정보") DeliveryNoticeV1ApiResponse deliveryNotice,
        @Schema(description = "환불 고지 정보") RefundNoticeV1ApiResponse refundNotice,
        @Schema(description = "상품 그룹 메인 이미지 URL",
                example = "https://example.com/image.jpg") String productGroupMainImageUrl,
        @Schema(description = "상품 상태", example = "ON_SALE") String productStatus,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime insertDate,
        @Schema(description = "수정 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updateDate,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator,
        @Schema(description = "평균 평점", example = "4.5") Double averageRating,
        @Schema(description = "리뷰 수", example = "100") Long reviewCount) {

    /**
     * V1 브랜드 Response
     *
     * @author development-team
     * @since 1.0.0
     */
    @Schema(description = "브랜드 응답")
    public record BrandV1ApiResponse(@Schema(description = "브랜드 ID", example = "1") Long brandId,
                                     @Schema(description = "브랜드명", example = "Nike") String brandName) {
    }


    /**
     * V1 의류 상세 Response
     *
     * @author development-team
     * @since 1.0.0
     */
    @Schema(description = "의류 상세 응답")
    public record ClothesDetailV1ApiResponse(
        @Schema(description = "상품 상태", example = "NEW") String productCondition,
        @Schema(description = "원산지", example = "한국") String origin) {
    }


    /**
     * V1 배송 고지 Response
     *
     * @author development-team
     * @since 1.0.0
     */
    @Schema(description = "배송 고지 응답")
    public record DeliveryNoticeV1ApiResponse(
        @Schema(description = "배송 지역", example = "전국") String deliveryArea,
        @Schema(description = "배송비", example = "3000") Long deliveryFee,
        @Schema(description = "평균 배송 기간", example = "3") Integer deliveryPeriodAverage) {
    }


    /**
     * V1 환불 고지 Response
     *
     * @author development-team
     * @since 1.0.0
     */
    @Schema(description = "환불 고지 응답")
    public record RefundNoticeV1ApiResponse(
        @Schema(description = "국내 반품 방법", example = "택배 반품") String returnMethodDomestic,
        @Schema(description = "국내 반품 택배사", example = "CJ대한통운") String returnCourierDomestic,
        @Schema(description = "국내 반품비", example = "3000") Integer returnChargeDomestic,
        @Schema(description = "국내 반품/교환 지역", example = "전국") String returnExchangeAreaDomestic) {
    }


    /**
     * V1 가격 Response
     *
     * @author development-team
     * @since 1.0.0
     */
    @Schema(description = "가격 응답")
    public record PriceV1ApiResponse(@Schema(description = "정가", example = "50000") Long regularPrice,
                                     @Schema(description = "현재가", example = "40000") Long currentPrice,
                                     @Schema(description = "판매가", example = "39000") Long salePrice,
                                     @Schema(description = "직접 할인율", example = "10") Integer directDiscountRate,
                                     @Schema(description = "직접 할인가", example = "1000") Long directDiscountPrice,
                                     @Schema(description = "할인율", example = "22") Integer discountRate) {
    }

}
