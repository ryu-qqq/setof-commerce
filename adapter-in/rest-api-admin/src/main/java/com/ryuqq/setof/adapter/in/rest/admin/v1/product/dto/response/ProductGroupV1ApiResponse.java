package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * ProductGroupV1ApiResponse - 상품그룹 목록 응답 DTO.
 *
 * <p>레거시 ProductGroupDetailResponse 기반 변환.
 *
 * <p>GET /api/v1/products/group - 상품그룹 목록 조회
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
 * @see com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse
 */
@Schema(description = "상품그룹 목록 응답")
public record ProductGroupV1ApiResponse(
        @Schema(description = "상품그룹 ID", example = "12345") long productGroupId,
        @Schema(description = "상품그룹명", example = "프리미엄 티셔츠") String productGroupName,
        @Schema(description = "판매자 ID", example = "100") long sellerId,
        @Schema(description = "판매자명", example = "브랜드샵") String sellerName,
        @Schema(description = "카테고리 ID", example = "500") long categoryId,
        @Schema(description = "카테고리 전체 경로명", example = "패션 > 남성의류 > 티셔츠") String categoryFullName,
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
        @Schema(description = "상품 상태") ProductStatusResponse productStatus,
        @Schema(description = "메인 이미지 URL", example = "https://cdn.example.com/main.jpg")
                String productGroupMainImageUrl,
        @Schema(description = "등록일시", example = "2024-01-15T10:30:00") LocalDateTime insertDate,
        @Schema(description = "수정일시", example = "2024-01-20T14:20:00") LocalDateTime updateDate,
        @Schema(description = "크롤링 상품 정보") CrawlProductResponse crawlProductInfo,
        @Schema(description = "외부 연동 상품 정보 목록") Set<ExternalProductResponse> externalProductInfos,
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
