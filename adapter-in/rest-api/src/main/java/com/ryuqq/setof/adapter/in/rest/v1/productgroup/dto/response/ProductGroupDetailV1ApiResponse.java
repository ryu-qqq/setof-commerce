package com.ryuqq.setof.adapter.in.rest.v1.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * ProductGroupDetailV1ApiResponse - 상품그룹 단건 상세 응답 DTO.
 *
 * <p>레거시 ProductGroupResponse 기반 변환. 단건 상세 조회(fetchProductGroup) 전용 응답 DTO. 상품그룹 기본 정보, 고시 정보, 이미지
 * 목록, 개별 상품(옵션), 카테고리 계층 등을 포함합니다.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>API-DTO-006: 복잡한 구조는 중첩 Record로 표현.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param productGroup 상품그룹 기본 정보
 * @param productNotices 상품 고시 정보
 * @param productGroupImages 상품 이미지 목록 (MAIN 이미지 우선 정렬)
 * @param products 개별 상품 목록 (옵션 포함)
 * @param categories 카테고리 계층 목록
 * @param detailDescription 상세 설명 이미지 URL
 * @param mileageRate 마일리지 적립률
 * @param expectedMileageAmount 예상 마일리지 금액
 * @param favorite 즐겨찾기 여부
 * @param eventProductType 이벤트 상품 유형
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.product.dto.ProductGroupResponse
 */
@Schema(description = "상품그룹 단건 상세 응답")
public record ProductGroupDetailV1ApiResponse(
        @Schema(description = "상품그룹 기본 정보") ProductGroupResponse productGroup,
        @Schema(description = "상품 고시 정보") ProductNoticeResponse productNotices,
        @Schema(description = "상품 이미지 목록 (MAIN 이미지 우선 정렬)")
                List<ProductImageResponse> productGroupImages,
        @Schema(description = "개별 상품 목록 (옵션 포함)") List<ProductResponse> products,
        @Schema(description = "카테고리 계층 목록") List<ProductCategoryResponse> categories,
        @Schema(description = "상세 설명 이미지 URL", example = "https://cdn.example.com/detail/123.html")
                String detailDescription,
        @Schema(description = "마일리지 적립률", example = "0.0") double mileageRate,
        @Schema(description = "예상 마일리지 금액", example = "0.0") double expectedMileageAmount,
        @Schema(description = "즐겨찾기 여부", example = "false") boolean favorite,
        @Schema(
                        description = "이벤트 상품 유형",
                        example = "NORMAL",
                        allowableValues = {"NORMAL", "RAFFLE"})
                String eventProductType) {

    /**
     * ProductGroupResponse - 상품그룹 기본 정보 응답.
     *
     * @param productGroupId 상품그룹 ID
     * @param productGroupName 상품그룹명
     * @param sellerId 셀러 ID
     * @param sellerName 셀러명
     * @param brand 브랜드 정보
     * @param categoryId 카테고리 ID
     * @param price 가격 정보
     * @param optionType 옵션 유형
     * @param clothesDetailInfo 의류 상세 정보
     * @param deliveryNotice 배송 안내
     * @param refundNotice 반품 안내
     * @param productGroupMainImageUrl 대표 이미지 URL
     * @param productStatus 판매 상태
     * @param insertDate 등록일
     * @param updateDate 수정일
     * @param averageRating 평균 평점
     * @param reviewCount 리뷰 수
     */
    @Schema(description = "상품그룹 기본 정보")
    public record ProductGroupResponse(
            @Schema(description = "상품그룹 ID", example = "123") long productGroupId,
            @Schema(description = "상품그룹명", example = "example_product_name")
                    String productGroupName,
            @Schema(description = "셀러 ID", example = "1") long sellerId,
            @Schema(description = "셀러명", example = "example_seller") String sellerName,
            @Schema(description = "브랜드 정보") BrandResponse brand,
            @Schema(description = "카테고리 ID", example = "50") long categoryId,
            @Schema(description = "가격 정보") PriceResponse price,
            @Schema(
                            description = "옵션 유형",
                            example = "SINGLE",
                            allowableValues = {"SINGLE", "MULTI", "NONE"})
                    String optionType,
            @Schema(description = "의류 상세 정보") ClothesDetailResponse clothesDetailInfo,
            @Schema(description = "배송 안내") DeliveryNoticeResponse deliveryNotice,
            @Schema(description = "반품 안내") RefundNoticeResponse refundNotice,
            @Schema(description = "대표 이미지 URL", example = "https://cdn.example.com/main/123.jpg")
                    String productGroupMainImageUrl,
            @Schema(description = "판매 상태") ProductStatusResponse productStatus,
            @Schema(description = "등록일", example = "2024-01-01 00:00:00") String insertDate,
            @Schema(description = "수정일", example = "2024-06-01 00:00:00") String updateDate,
            @Schema(description = "평균 평점", example = "4.5") double averageRating,
            @Schema(description = "리뷰 수", example = "120") long reviewCount) {}

    /**
     * BrandResponse - 브랜드 정보 응답.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "10") long brandId,
            @Schema(description = "브랜드명", example = "example_brand") String brandName) {}

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
            @Schema(description = "판매가 (할인 적용)", example = "40500") long salePrice,
            @Schema(description = "직접 할인율 (%)", example = "10") int directDiscountRate,
            @Schema(description = "직접 할인 금액", example = "4500") long directDiscountPrice,
            @Schema(description = "총 할인율 (%)", example = "19") int discountRate) {}

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

    /**
     * ClothesDetailResponse - 의류 상세 정보 응답.
     *
     * @param productCondition 상품 상태 (NEW 등)
     * @param origin 원산지
     */
    @Schema(description = "의류 상세 정보")
    public record ClothesDetailResponse(
            @Schema(description = "상품 상태", example = "NEW") String productCondition,
            @Schema(description = "원산지", example = "KOREA") String origin) {}

    /**
     * DeliveryNoticeResponse - 배송 안내 응답.
     *
     * @param deliveryType 배송 유형
     * @param deliveryCompany 배송사
     * @param deliveryCharge 배송비
     * @param freeDeliveryCondition 무료배송 조건 금액
     */
    @Schema(description = "배송 안내")
    public record DeliveryNoticeResponse(
            @Schema(description = "배송 유형", example = "PARCEL") String deliveryType,
            @Schema(description = "배송사", example = "CJ") String deliveryCompany,
            @Schema(description = "배송비", example = "3000") long deliveryCharge,
            @Schema(description = "무료배송 조건 금액", example = "50000") long freeDeliveryCondition) {}

    /**
     * RefundNoticeResponse - 반품 안내 응답.
     *
     * @param returnMethodDomestic 반품 방법
     * @param returnCourierDomestic 반품 택배사
     * @param returnChargeDomestic 반품 배송비
     * @param returnExchangeAreaDomestic 반품/교환 가능 지역
     */
    @Schema(description = "반품 안내")
    public record RefundNoticeResponse(
            @Schema(description = "반품 방법", example = "택배") String returnMethodDomestic,
            @Schema(description = "반품 택배사", example = "CJ대한통운") String returnCourierDomestic,
            @Schema(description = "반품 배송비", example = "3000") int returnChargeDomestic,
            @Schema(description = "반품/교환 가능 지역", example = "전국")
                    String returnExchangeAreaDomestic) {}

    /**
     * ProductNoticeResponse - 상품 고시 정보 응답.
     *
     * @param material 소재
     * @param color 색상
     * @param size 사이즈
     * @param maker 제조사
     * @param origin 원산지
     * @param washingMethod 세탁방법
     * @param yearMonth 제조연월
     * @param assuranceStandard 품질보증기준
     * @param asPhone A/S 전화번호
     */
    @Schema(description = "상품 고시 정보")
    public record ProductNoticeResponse(
            @Schema(description = "소재", example = "면 100%") String material,
            @Schema(description = "색상", example = "블랙") String color,
            @Schema(description = "사이즈", example = "S/M/L/XL") String size,
            @Schema(description = "제조사", example = "example_maker") String maker,
            @Schema(description = "원산지", example = "국내산") String origin,
            @Schema(description = "세탁방법", example = "손세탁") String washingMethod,
            @Schema(description = "제조연월", example = "2024-01") String yearMonth,
            @Schema(description = "품질보증기준", example = "소비자분쟁해결기준에 의거") String assuranceStandard,
            @Schema(description = "A/S 전화번호", example = "1588-0000") String asPhone) {}

    /**
     * ProductImageResponse - 상품 이미지 응답.
     *
     * @param productGroupImageType 이미지 유형 (MAIN / SUB 등)
     * @param imageUrl 이미지 URL
     */
    @Schema(description = "상품 이미지")
    public record ProductImageResponse(
            @Schema(
                            description = "이미지 유형",
                            example = "MAIN",
                            allowableValues = {"MAIN", "SUB"})
                    String productGroupImageType,
            @Schema(description = "이미지 URL", example = "https://cdn.example.com/main/123.jpg")
                    String imageUrl) {}

    /**
     * ProductResponse - 개별 상품 응답.
     *
     * @param productId 개별 상품 ID
     * @param stockQuantity 재고 수량
     * @param productStatus 개별 상품 판매 상태
     * @param option 옵션값 문자열 (옵션 없으면 빈 문자열)
     * @param options 옵션 상세 목록
     */
    @Schema(description = "개별 상품")
    public record ProductResponse(
            @Schema(description = "개별 상품 ID", example = "1001") long productId,
            @Schema(description = "재고 수량", example = "50") int stockQuantity,
            @Schema(description = "판매 상태") ProductStatusResponse productStatus,
            @Schema(description = "옵션값 문자열 (옵션 없으면 빈 문자열)", example = "M / RED") String option,
            @Schema(description = "옵션 상세 목록") List<OptionResponse> options) {}

    /**
     * OptionResponse - 옵션 정보 응답.
     *
     * @param optionGroupId 옵션 그룹 ID
     * @param optionDetailId 옵션 상세 ID
     * @param optionName 옵션명 (SIZE / COLOR 등)
     * @param optionValue 옵션값 ("XL", "RED" 등)
     */
    @Schema(description = "옵션 정보")
    public record OptionResponse(
            @Schema(description = "옵션 그룹 ID", example = "1") long optionGroupId,
            @Schema(description = "옵션 상세 ID", example = "11") long optionDetailId,
            @Schema(description = "옵션명", example = "SIZE") String optionName,
            @Schema(description = "옵션값", example = "M") String optionValue) {}

    /**
     * ProductCategoryResponse - 카테고리 계층 응답.
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param displayName 표시명
     * @param parentCategoryId 부모 카테고리 ID
     * @param categoryDepth 카테고리 깊이
     */
    @Schema(description = "카테고리 계층")
    public record ProductCategoryResponse(
            @Schema(description = "카테고리 ID", example = "50") long categoryId,
            @Schema(description = "카테고리명", example = "티셔츠") String categoryName,
            @Schema(description = "표시명", example = "티셔츠") String displayName,
            @Schema(description = "부모 카테고리 ID", example = "1") long parentCategoryId,
            @Schema(description = "카테고리 깊이", example = "2") int categoryDepth) {}
}
