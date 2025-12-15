package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 상품그룹 등록 요청 (Full 등록)
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 일괄 등록
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품그룹 등록 요청")
public record RegisterProductGroupV2ApiRequest(
        @Schema(description = "셀러 ID", example = "1") @NotNull(message = "셀러 ID는 필수입니다")
                Long sellerId,
        @Schema(description = "카테고리 ID", example = "100") @NotNull(message = "카테고리 ID는 필수입니다")
                Long categoryId,
        @Schema(description = "브랜드 ID", example = "10") @NotNull(message = "브랜드 ID는 필수입니다")
                Long brandId,
        @Schema(description = "상품그룹명", example = "프리미엄 코튼 티셔츠") @NotBlank(message = "상품그룹명은 필수입니다")
                String name,
        @Schema(description = "옵션 타입 (SINGLE, ONE_LEVEL, TWO_LEVEL)", example = "TWO_LEVEL")
                @NotBlank(message = "옵션 타입은 필수입니다")
                String optionType,
        @Schema(description = "정가", example = "50000")
                @NotNull(message = "정가는 필수입니다")
                @DecimalMin(value = "0", message = "정가는 0 이상이어야 합니다")
                BigDecimal regularPrice,
        @Schema(description = "판매가", example = "39000")
                @NotNull(message = "판매가는 필수입니다")
                @DecimalMin(value = "0", message = "판매가는 0 이상이어야 합니다")
                BigDecimal currentPrice,
        @Schema(description = "배송 정책 ID (null이면 셀러 기본 정책)", example = "1") Long shippingPolicyId,
        @Schema(description = "환불 정책 ID (null이면 셀러 기본 정책)", example = "1") Long refundPolicyId,
        @Schema(description = "상품(SKU) 목록") @NotEmpty(message = "최소 1개 이상의 상품(SKU)이 필요합니다") @Valid
                List<ProductSkuV2ApiRequest> products,
        @Schema(description = "이미지 목록") @NotEmpty(message = "최소 1개 이상의 이미지가 필요합니다") @Valid
                List<ProductImageV2ApiRequest> images,
        @Schema(description = "상품 설명") @Valid ProductDescriptionV2ApiRequest description,
        @Schema(description = "상품 고시정보") @Valid ProductNoticeV2ApiRequest notice) {

    /** 상품(SKU) 요청 DTO */
    @Schema(description = "상품(SKU) 요청")
    public record ProductSkuV2ApiRequest(
            @Schema(description = "옵션1 명", example = "색상") String option1Name,
            @Schema(description = "옵션1 값", example = "블랙") String option1Value,
            @Schema(description = "옵션2 명", example = "사이즈") String option2Name,
            @Schema(description = "옵션2 값", example = "M") String option2Value,
            @Schema(description = "추가금액", example = "0") @NotNull(message = "추가금액은 필수입니다")
                    BigDecimal additionalPrice,
            @Schema(description = "초기 재고", example = "100", minimum = "0")
                    @Min(value = 0, message = "재고는 0 이상이어야 합니다")
                    int initialStock) {}

    /** 이미지 요청 DTO */
    @Schema(description = "상품 이미지 요청")
    public record ProductImageV2ApiRequest(
            @Schema(description = "이미지 타입 (MAIN, SUB, DETAIL)", example = "MAIN")
                    @NotBlank(message = "이미지 타입은 필수입니다")
                    String imageType,
            @Schema(description = "원본 URL", example = "https://cdn.example.com/image.jpg")
                    @NotBlank(message = "원본 URL은 필수입니다")
                    String originUrl,
            @Schema(description = "CDN URL", example = "https://cdn.example.com/image.jpg")
                    String cdnUrl,
            @Schema(description = "표시 순서", example = "1", minimum = "0")
                    @Min(value = 0, message = "표시 순서는 0 이상이어야 합니다")
                    int displayOrder) {}

    /** 상품 설명 요청 DTO */
    @Schema(description = "상품 설명 요청")
    public record ProductDescriptionV2ApiRequest(
            @Schema(description = "HTML 컨텐츠", example = "<p>상품 상세 설명</p>")
                    @NotBlank(message = "HTML 컨텐츠는 필수입니다")
                    String htmlContent,
            @Schema(description = "설명 이미지 목록") @Valid List<DescriptionImageV2ApiRequest> images) {

        @Schema(description = "설명 이미지 요청")
        public record DescriptionImageV2ApiRequest(
                @Schema(description = "표시 순서", example = "1") int displayOrder,
                @Schema(description = "원본 URL", example = "https://cdn.example.com/desc.jpg")
                        @NotBlank(message = "원본 URL은 필수입니다")
                        String originUrl,
                @Schema(description = "CDN URL", example = "https://cdn.example.com/desc.jpg")
                        String cdnUrl) {}
    }

    /** 상품 고시정보 요청 DTO */
    @Schema(description = "상품 고시정보 요청")
    public record ProductNoticeV2ApiRequest(
            @Schema(description = "템플릿 ID", example = "1") @NotNull(message = "템플릿 ID는 필수입니다")
                    Long templateId,
            @Schema(description = "고시 항목 목록") @NotEmpty(message = "최소 1개 이상의 고시 항목이 필요합니다") @Valid
                    List<NoticeItemV2ApiRequest> items) {

        @Schema(description = "고시 항목 요청")
        public record NoticeItemV2ApiRequest(
                @Schema(description = "필드 키", example = "material")
                        @NotBlank(message = "필드 키는 필수입니다")
                        String fieldKey,
                @Schema(description = "필드 값", example = "면 100%") @NotBlank(message = "필드 값은 필수입니다")
                        String fieldValue,
                @Schema(description = "표시 순서", example = "1") int displayOrder) {}
    }
}
