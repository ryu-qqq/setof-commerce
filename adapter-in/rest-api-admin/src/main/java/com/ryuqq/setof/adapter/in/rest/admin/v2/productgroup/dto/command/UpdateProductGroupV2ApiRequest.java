package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductDescriptionV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupV2ApiRequest.ProductNoticeV2ApiRequest;
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
 * 상품그룹 수정 요청 (Full 수정)
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 일괄 수정
 *
 * <p>수정 전략:
 *
 * <ul>
 *   <li>ID가 있는 항목: 수정
 *   <li>ID가 없는 항목: 신규 추가
 *   <li>기존에만 있는 항목: 삭제
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품그룹 수정 요청")
public record UpdateProductGroupV2ApiRequest(
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
        @Schema(
                        description = "상품 상태 (DRAFT, PENDING, APPROVED, REJECTED, DISCONTINUED)",
                        example = "APPROVED")
                @NotBlank(message = "상품 상태는 필수입니다")
                String status,
        @Schema(description = "배송 정책 ID (null이면 셀러 기본 정책)", example = "1") Long shippingPolicyId,
        @Schema(description = "환불 정책 ID (null이면 셀러 기본 정책)", example = "1") Long refundPolicyId,
        @Schema(description = "상품(SKU) 목록") @NotEmpty(message = "최소 1개 이상의 상품(SKU)이 필요합니다") @Valid
                List<UpdateProductSkuV2ApiRequest> products,
        @Schema(description = "이미지 목록") @NotEmpty(message = "최소 1개 이상의 이미지가 필요합니다") @Valid
                List<UpdateProductImageV2ApiRequest> images,
        @Schema(description = "상품 설명") @Valid UpdateProductDescriptionV2ApiRequest description,
        @Schema(description = "상품 고시정보") @Valid UpdateProductNoticeV2ApiRequest notice) {

    /** 상품(SKU) 수정 요청 DTO */
    @Schema(description = "상품(SKU) 수정 요청")
    public record UpdateProductSkuV2ApiRequest(
            @Schema(description = "옵션1 명", example = "색상") String option1Name,
            @Schema(description = "옵션1 값", example = "블랙") String option1Value,
            @Schema(description = "옵션2 명", example = "사이즈") String option2Name,
            @Schema(description = "옵션2 값", example = "M") String option2Value,
            @Schema(description = "추가금액", example = "0") @NotNull(message = "추가금액은 필수입니다")
                    BigDecimal additionalPrice,
            @Schema(description = "초기 재고", example = "100", minimum = "0")
                    @Min(value = 0, message = "재고는 0 이상이어야 합니다")
                    int initialStock) {}

    /** 이미지 수정 요청 DTO */
    @Schema(description = "상품 이미지 수정 요청")
    public record UpdateProductImageV2ApiRequest(
            @Schema(description = "이미지 ID (null이면 신규 추가)", example = "1") Long id,
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

    /** 상품 설명 수정 요청 DTO */
    @Schema(description = "상품 설명 수정 요청")
    public record UpdateProductDescriptionV2ApiRequest(
            @Schema(description = "설명 ID (null이면 신규 추가)", example = "1") Long id,
            @Schema(description = "HTML 컨텐츠", example = "<p>상품 상세 설명</p>")
                    @NotBlank(message = "HTML 컨텐츠는 필수입니다")
                    String htmlContent,
            @Schema(description = "설명 이미지 목록") @Valid
                    List<ProductDescriptionV2ApiRequest.DescriptionImageV2ApiRequest> images) {}

    /** 상품 고시정보 수정 요청 DTO */
    @Schema(description = "상품 고시정보 수정 요청")
    public record UpdateProductNoticeV2ApiRequest(
            @Schema(description = "고시 ID (null이면 신규 추가)", example = "1") Long id,
            @Schema(description = "템플릿 ID", example = "1") @NotNull(message = "템플릿 ID는 필수입니다")
                    Long templateId,
            @Schema(description = "고시 항목 목록") @NotEmpty(message = "최소 1개 이상의 고시 항목이 필요합니다") @Valid
                    List<ProductNoticeV2ApiRequest.NoticeItemV2ApiRequest> items) {}
}
