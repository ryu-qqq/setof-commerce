package com.ryuqq.setof.adapter.in.rest.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * ProductGroupDetailV1ApiResponse - 상품그룹 상세 응답 DTO.
 *
 * <p>레거시 ProductGroupResponse 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>단건 상세 조회 시 사용되는 전체 정보입니다.
 *
 * @param productGroup 상품그룹 기본 정보
 * @param productNotices 상품 고시 정보
 * @param productGroupImages 상품그룹 이미지 목록
 * @param products 하위 상품 목록 (옵션 포함)
 * @param categories 연관 카테고리 목록
 * @param detailDescription 상세 설명 이미지 URL
 * @param mileageRate 마일리지 적립율
 * @param expectedMileageAmount 예상 마일리지 적립금액
 * @param favorite 찜 여부
 * @param eventProductType 이벤트 상품 타입
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.setof.connectly.module.product.dto.ProductGroupResponse
 */
@Schema(description = "상품그룹 상세 응답")
public record ProductGroupDetailV1ApiResponse(
        @Schema(description = "상품그룹 기본 정보") ProductGroupInfoResponse productGroup,
        @Schema(description = "상품 고시 정보") ProductNoticeResponse productNotices,
        @Schema(description = "상품그룹 이미지 목록") Set<ProductImageResponse> productGroupImages,
        @Schema(description = "하위 상품 목록 (옵션 포함)") Set<ProductResponse> products,
        @Schema(description = "연관 카테고리 목록") Set<ProductCategoryResponse> categories,
        @Schema(description = "상세 설명 이미지 URL", example = "https://cdn.example.com/detail/123.jpg")
                String detailDescription,
        @Schema(description = "마일리지 적립율 (%)", example = "1.0") double mileageRate,
        @Schema(description = "예상 마일리지 적립금액", example = "390") double expectedMileageAmount,
        @Schema(description = "찜 여부", example = "false") boolean favorite,
        @Schema(description = "이벤트 상품 타입", example = "NORMAL") String eventProductType) {

    /** ProductGroupInfoResponse - 상품그룹 기본 정보 응답 DTO. */
    @Schema(description = "상품그룹 기본 정보")
    public record ProductGroupInfoResponse(
            @Schema(description = "상품그룹 ID", example = "123") long productGroupId,
            @Schema(description = "상품그룹명", example = "여름 원피스") String productGroupName,
            @Schema(description = "셀러 ID", example = "10") long sellerId,
            @Schema(description = "셀러명", example = "패션스토어") String sellerName,
            @Schema(description = "브랜드 정보") BrandResponse brand,
            @Schema(description = "카테고리 ID", example = "100") long categoryId,
            @Schema(description = "가격 정보") PriceResponse price,
            @Schema(description = "옵션 타입", example = "COMBINATION") String optionType,
            @Schema(description = "의류 상세 정보") ClothesDetailResponse clothesDetail,
            @Schema(description = "상품 상태") ProductStatusResponse productStatus,
            @Schema(description = "배송 안내") DeliveryNoticeResponse deliveryNotice,
            @Schema(description = "환불 안내") RefundNoticeResponse refundNotice,
            @Schema(description = "리뷰 통계") ProductReviewResponse productReview) {}

    /** BrandResponse - 브랜드 응답 DTO. */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "5") long brandId,
            @Schema(description = "브랜드명", example = "NIKE") String brandName) {}

    /** PriceResponse - 가격 응답 DTO. */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정가", example = "59000") long regularPrice,
            @Schema(description = "판매가", example = "39000") long salePrice,
            @Schema(description = "할인율 (%)", example = "33") int discountRate) {}

    /** ProductStatusResponse - 상품 상태 응답 DTO. */
    @Schema(description = "상품 상태")
    public record ProductStatusResponse(
            @Schema(description = "전시 여부", example = "Y") String displayYn,
            @Schema(description = "품절 여부", example = "N") String soldOutYn) {}

    /** ClothesDetailResponse - 의류 상세 정보 응답 DTO. */
    @Schema(description = "의류 상세 정보")
    public record ClothesDetailResponse(
            @Schema(description = "상품 상태", example = "NEW") String productCondition,
            @Schema(description = "원산지", example = "대한민국") String origin) {}

    /** DeliveryNoticeResponse - 배송 안내 응답 DTO. */
    @Schema(description = "배송 안내")
    public record DeliveryNoticeResponse(
            @Schema(description = "배송비 타입", example = "FREE") String deliveryFeeType,
            @Schema(description = "배송비", example = "0") long deliveryFee,
            @Schema(description = "무료배송 기준금액", example = "50000") long freeDeliveryOverAmount,
            @Schema(description = "배송 기간 (일)", example = "3") int deliveryDays) {}

    /** RefundNoticeResponse - 환불 안내 응답 DTO. */
    @Schema(description = "환불 안내")
    public record RefundNoticeResponse(
            @Schema(description = "반품 방법", example = "택배수거") String returnMethodDomestic,
            @Schema(description = "반품 택배사", example = "CJ대한통운") String returnCourierDomestic,
            @Schema(description = "반품 배송비", example = "3000") String returnChargeDomestic,
            @Schema(description = "반품/교환 지역", example = "서울시 강남구")
                    String returnExchangeAreaDomestic) {}

    /** ProductReviewResponse - 리뷰 통계 응답 DTO. */
    @Schema(description = "리뷰 통계")
    public record ProductReviewResponse(
            @Schema(description = "평균 평점", example = "4.5") double averageRating,
            @Schema(description = "리뷰 수", example = "128") long reviewCount) {}

    /** ProductNoticeResponse - 상품 고시 정보 응답 DTO. */
    @Schema(description = "상품 고시 정보")
    public record ProductNoticeResponse(
            @Schema(description = "고시 ID", example = "1") long noticeId,
            @Schema(description = "소재", example = "면 100%") String material,
            @Schema(description = "색상", example = "블랙, 화이트") String color,
            @Schema(description = "사이즈", example = "S, M, L, XL") String size,
            @Schema(description = "제조사", example = "OO의류") String maker,
            @Schema(description = "원산지", example = "대한민국") String origin,
            @Schema(description = "세탁 방법", example = "드라이클리닝") String washingMethod,
            @Schema(description = "제조년월", example = "2026-01") String yearMonth,
            @Schema(description = "품질보증기준", example = "제품 이상 시 교환/환불") String assuranceStandard,
            @Schema(description = "A/S 전화번호", example = "1588-0000") String asPhone) {}

    /** ProductImageResponse - 상품 이미지 응답 DTO. */
    @Schema(description = "상품 이미지")
    public record ProductImageResponse(
            @Schema(description = "이미지 ID", example = "1") long imageId,
            @Schema(description = "이미지 타입", example = "MAIN") String imageType,
            @Schema(description = "이미지 URL", example = "https://cdn.example.com/product/123.jpg")
                    String imageUrl) {}

    /** ProductResponse - 하위 상품 (SKU) 응답 DTO. */
    @Schema(description = "하위 상품 (SKU)")
    public record ProductResponse(
            @Schema(description = "상품 ID", example = "1001") long productId,
            @Schema(description = "재고 수량", example = "100") int stockQuantity,
            @Schema(description = "추가 금액", example = "1000") long additionalPrice,
            @Schema(description = "상품 상태") ProductStatusResponse productStatus,
            @Schema(description = "옵션 그룹 ID", example = "1") long optionGroupId,
            @Schema(description = "옵션 상세 ID", example = "10") long optionDetailId,
            @Schema(description = "옵션명", example = "사이즈") String optionName,
            @Schema(description = "옵션값", example = "M") String optionValue) {}

    /** ProductCategoryResponse - 연관 카테고리 응답 DTO. */
    @Schema(description = "연관 카테고리")
    public record ProductCategoryResponse(
            @Schema(description = "카테고리 ID", example = "100") long categoryId,
            @Schema(description = "카테고리명", example = "여성의류") String categoryName,
            @Schema(description = "카테고리 뎁스", example = "1") int depth) {}
}
