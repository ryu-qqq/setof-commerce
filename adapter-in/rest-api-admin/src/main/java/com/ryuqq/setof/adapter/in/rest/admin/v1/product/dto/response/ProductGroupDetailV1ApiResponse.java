package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * ProductGroupDetailV1ApiResponse - 상품그룹 상세 응답 DTO.
 *
 * <p>레거시 ProductGroupFetchResponse 기반 변환.
 *
 * <p>GET /api/v1/product/group/{productGroupId} - 상품그룹 상세 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>Lombok @Getter → record 기본 접근자
 *   <li>Enum 타입 → String 타입
 *   <li>중첩 클래스 → 중첩 record
 *   <li>@Schema 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse
 */
@Schema(description = "상품그룹 상세 응답")
public record ProductGroupDetailV1ApiResponse(
        @Schema(description = "상품그룹 ID", example = "12345") long productGroupId,
        @Schema(description = "상품그룹명", example = "프리미엄 티셔츠") String productGroupName,
        @Schema(description = "판매자 ID", example = "100") long sellerId,
        @Schema(description = "판매자명", example = "브랜드샵") String sellerName,
        @Schema(description = "카테고리 ID", example = "500") long categoryId,
        @Schema(
                        description = "옵션 타입",
                        example = "COMBINATION",
                        allowableValues = {"SINGLE", "COMBINATION"})
                String optionType,
        @Schema(
                        description = "관리 유형",
                        example = "NORMAL",
                        allowableValues = {"NORMAL", "CRAWL", "EXTERNAL"})
                String managementType,
        @Schema(description = "브랜드 정보") BrandResponse brand,
        @Schema(description = "가격 정보") PriceResponse price,
        @Schema(description = "의류 상세 정보") ClothesDetailResponse clothesDetailInfo,
        @Schema(description = "배송 안내") DeliveryNoticeResponse deliveryNotice,
        @Schema(description = "환불 안내") RefundNoticeResponse refundNotice,
        @Schema(description = "상품 상태") ProductStatusResponse productStatus,
        @Schema(description = "메인 이미지 URL", example = "https://cdn.example.com/main.jpg")
                String productGroupMainImageUrl,
        @Schema(description = "등록일시", example = "2024-01-15T10:30:00") LocalDateTime insertDate,
        @Schema(description = "수정일시", example = "2024-01-20T14:20:00") LocalDateTime updateDate,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator,
        @Schema(description = "외부 상품 UUID", example = "abc-123-def-456") String externalProductUuId,
        @Schema(description = "크롤링 상품 정보") CrawlProductResponse crawlProductInfo,
        @Schema(description = "외부 연동 상품 정보 목록") Set<ExternalProductResponse> externalProductInfos,
        @Schema(description = "상품 고시 정보") ProductNoticeResponse productNotices,
        @Schema(description = "상품 이미지 목록") List<ProductImageResponse> productGroupImages,
        @Schema(description = "상세 설명 이미지 URL", example = "https://cdn.example.com/detail.html")
                String detailDescription,
        @Schema(description = "카테고리 경로 목록") List<CategoryResponse> categories,
        @Schema(description = "개별 상품(SKU) 목록") Set<ProductResponse> products) {

    /** 브랜드 정보 응답 DTO. */
    @Schema(description = "브랜드 정보")
    public record BrandResponse(
            @Schema(description = "브랜드 ID", example = "50") long brandId,
            @Schema(description = "브랜드명", example = "나이키") String brandName) {

        public static BrandResponse of(long brandId, String brandName) {
            return new BrandResponse(brandId, brandName);
        }
    }

    /** 가격 정보 응답 DTO. */
    @Schema(description = "가격 정보")
    public record PriceResponse(
            @Schema(description = "정가", example = "50000") BigDecimal regularPrice,
            @Schema(description = "판매가", example = "39000") BigDecimal salePrice,
            @Schema(description = "할인율 (%)", example = "22") long discountRate) {

        public static PriceResponse of(
                BigDecimal regularPrice, BigDecimal salePrice, long discountRate) {
            return new PriceResponse(regularPrice, salePrice, discountRate);
        }
    }

    /** 의류 상세 정보 응답 DTO. */
    @Schema(description = "의류 상세 정보")
    public record ClothesDetailResponse(
            @Schema(description = "성별", example = "UNISEX") String gender,
            @Schema(description = "시즌", example = "2024SS") String season,
            @Schema(description = "스타일", example = "캐주얼") String style) {

        public static ClothesDetailResponse of(String gender, String season, String style) {
            return new ClothesDetailResponse(gender, season, style);
        }
    }

    /** 배송 안내 응답 DTO. */
    @Schema(description = "배송 안내")
    public record DeliveryNoticeResponse(
            @Schema(description = "배송비", example = "3000") BigDecimal deliveryFee,
            @Schema(description = "무료배송 기준금액", example = "50000") BigDecimal freeDeliveryAmount,
            @Schema(description = "배송 안내 문구", example = "주문 후 2-3일 내 배송") String deliveryGuide) {

        public static DeliveryNoticeResponse of(
                BigDecimal deliveryFee, BigDecimal freeDeliveryAmount, String deliveryGuide) {
            return new DeliveryNoticeResponse(deliveryFee, freeDeliveryAmount, deliveryGuide);
        }
    }

    /** 환불 안내 응답 DTO. */
    @Schema(description = "환불 안내")
    public record RefundNoticeResponse(
            @Schema(description = "반품 배송비", example = "3000") BigDecimal returnDeliveryFee,
            @Schema(description = "교환 배송비", example = "6000") BigDecimal exchangeDeliveryFee,
            @Schema(description = "반품 주소", example = "서울시 강남구 테헤란로 123") String returnAddress,
            @Schema(description = "반품 안내 문구", example = "수령 후 7일 이내 반품 가능") String returnGuide) {

        public static RefundNoticeResponse of(
                BigDecimal returnDeliveryFee,
                BigDecimal exchangeDeliveryFee,
                String returnAddress,
                String returnGuide) {
            return new RefundNoticeResponse(
                    returnDeliveryFee, exchangeDeliveryFee, returnAddress, returnGuide);
        }
    }

    /** 상품 상태 응답 DTO. */
    @Schema(description = "상품 상태")
    public record ProductStatusResponse(
            @Schema(
                            description = "품절 여부",
                            example = "N",
                            allowableValues = {"Y", "N"})
                    String soldOutYn,
            @Schema(
                            description = "노출 여부",
                            example = "Y",
                            allowableValues = {"Y", "N"})
                    String displayYn,
            @Schema(description = "판매 시작일", example = "2024-01-01T00:00:00")
                    LocalDateTime sellStartDate,
            @Schema(description = "판매 종료일", example = "2024-12-31T23:59:59")
                    LocalDateTime sellEndDate) {

        public static ProductStatusResponse of(
                String soldOutYn,
                String displayYn,
                LocalDateTime sellStartDate,
                LocalDateTime sellEndDate) {
            return new ProductStatusResponse(soldOutYn, displayYn, sellStartDate, sellEndDate);
        }
    }

    /** 크롤링 상품 정보 응답 DTO. */
    @Schema(description = "크롤링 상품 정보")
    public record CrawlProductResponse(
            @Schema(description = "사이트명", example = "무신사") String siteName,
            @Schema(description = "크롤링 상품 SKU", example = "123456") long crawlProductSku,
            @Schema(description = "업데이트 상태", example = "COMPLETED") String updateStatus,
            @Schema(description = "등록일시", example = "2024-01-15T10:30:00") LocalDateTime insertDate,
            @Schema(description = "수정일시", example = "2024-01-20T14:20:00")
                    LocalDateTime updateDate) {

        public static CrawlProductResponse of(
                String siteName,
                long crawlProductSku,
                String updateStatus,
                LocalDateTime insertDate,
                LocalDateTime updateDate) {
            return new CrawlProductResponse(
                    siteName, crawlProductSku, updateStatus, insertDate, updateDate);
        }
    }

    /** 외부 연동 상품 정보 응답 DTO. */
    @Schema(description = "외부 연동 상품 정보")
    public record ExternalProductResponse(
            @Schema(description = "사이트명", example = "네이버") String siteName,
            @Schema(description = "외부 상품 ID", example = "EXT-12345") String externalIdx,
            @Schema(description = "매핑 상태", example = "MAPPED") String mappingStatus,
            @Schema(description = "등록일시", example = "2024-01-15T10:30:00") LocalDateTime insertDate,
            @Schema(description = "수정일시", example = "2024-01-20T14:20:00")
                    LocalDateTime updateDate) {

        public static ExternalProductResponse of(
                String siteName,
                String externalIdx,
                String mappingStatus,
                LocalDateTime insertDate,
                LocalDateTime updateDate) {
            return new ExternalProductResponse(
                    siteName, externalIdx, mappingStatus, insertDate, updateDate);
        }
    }

    /** 상품 고시 정보 응답 DTO. */
    @Schema(description = "상품 고시 정보")
    public record ProductNoticeResponse(
            @Schema(description = "소재", example = "면 100%") String material,
            @Schema(description = "색상", example = "블랙, 화이트, 네이비") String color,
            @Schema(description = "사이즈", example = "S, M, L, XL") String size,
            @Schema(description = "제조사", example = "나이키코리아") String maker,
            @Schema(
                            description = "원산지",
                            example = "KOREA",
                            allowableValues = {"KOREA", "CHINA", "USA", "ETC"})
                    String origin,
            @Schema(description = "세탁 방법", example = "물세탁 가능") String washingMethod,
            @Schema(description = "제조 연월", example = "2024-01") String yearMonth,
            @Schema(description = "품질 보증 기준", example = "제품 이상 시 무상 교환") String assuranceStandard,
            @Schema(description = "AS 전화번호", example = "1588-1234") String asPhone) {

        public static ProductNoticeResponse of(
                String material,
                String color,
                String size,
                String maker,
                String origin,
                String washingMethod,
                String yearMonth,
                String assuranceStandard,
                String asPhone) {
            return new ProductNoticeResponse(
                    material,
                    color,
                    size,
                    maker,
                    origin,
                    washingMethod,
                    yearMonth,
                    assuranceStandard,
                    asPhone);
        }
    }

    /** 상품 이미지 응답 DTO. */
    @Schema(description = "상품 이미지")
    public record ProductImageResponse(
            @Schema(
                            description = "이미지 타입",
                            example = "MAIN",
                            allowableValues = {"MAIN", "SUB", "DETAIL"})
                    String type,
            @Schema(description = "이미지 URL", example = "https://cdn.example.com/image.jpg")
                    String productImageUrl) {

        public static ProductImageResponse of(String type, String productImageUrl) {
            return new ProductImageResponse(type, productImageUrl);
        }
    }

    /** 카테고리 경로 응답 DTO. */
    @Schema(description = "카테고리 경로")
    public record CategoryResponse(
            @Schema(description = "카테고리 ID", example = "100") long categoryId,
            @Schema(description = "카테고리명", example = "패션") String categoryName,
            @Schema(description = "뎁스 (1: 대분류, 2: 중분류, ...)", example = "1") int depth) {

        public static CategoryResponse of(long categoryId, String categoryName, int depth) {
            return new CategoryResponse(categoryId, categoryName, depth);
        }
    }

    /** 개별 상품(SKU) 정보 응답 DTO. */
    @Schema(description = "개별 상품(SKU) 정보")
    public record ProductResponse(
            @Schema(description = "상품 ID", example = "10001") long productId,
            @Schema(description = "재고 수량", example = "50") int stockQuantity,
            @Schema(description = "상품 상태") ProductStatusResponse productStatus,
            @Schema(description = "옵션 조합 문자열", example = "블랙/M") String option,
            @Schema(description = "옵션 상세 목록") Set<OptionResponse> options,
            @Schema(description = "추가 금액", example = "0") BigDecimal additionalPrice) {

        public static ProductResponse of(
                long productId,
                int stockQuantity,
                ProductStatusResponse productStatus,
                String option,
                Set<OptionResponse> options,
                BigDecimal additionalPrice) {
            return new ProductResponse(
                    productId, stockQuantity, productStatus, option, options, additionalPrice);
        }
    }

    /** 옵션 정보 응답 DTO. */
    @Schema(description = "옵션 정보")
    public record OptionResponse(
            @Schema(description = "옵션 그룹명", example = "색상") String optionGroupName,
            @Schema(description = "옵션값", example = "블랙") String optionValue) {

        public static OptionResponse of(String optionGroupName, String optionValue) {
            return new OptionResponse(optionGroupName, optionValue);
        }
    }
}
