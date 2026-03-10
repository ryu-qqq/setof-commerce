package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 상품 그룹 등록 API 요청.
 *
 * <p>Validation을 포함한 REST API Layer DTO입니다.
 *
 * <p>API-REQ-001: Record 패턴 사용
 *
 * <p>API-VAL-001: jakarta.validation 사용
 */
@Schema(description = "상품 그룹 등록 요청")
public record RegisterProductGroupApiRequest(
        @Schema(
                        description = "상품 그룹 ID (마이그레이션 시 레거시 DB ID 동기화용, 미입력 시 자동 생성)",
                        example = "12345",
                        nullable = true)
                Long productGroupId,
        @Schema(description = "셀러 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "셀러 ID는 필수입니다")
                @Min(value = 1, message = "셀러 ID는 1 이상이어야 합니다")
                Long sellerId,
        @Schema(description = "브랜드 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "브랜드 ID는 필수입니다")
                @Min(value = 1, message = "브랜드 ID는 1 이상이어야 합니다")
                Long brandId,
        @Schema(
                        description = "카테고리 ID",
                        example = "100",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "카테고리 ID는 필수입니다")
                @Min(value = 1, message = "카테고리 ID는 1 이상이어야 합니다")
                Long categoryId,
        @Schema(
                        description = "배송 정책 ID",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "배송 정책 ID는 필수입니다")
                @Min(value = 1, message = "배송 정책 ID는 1 이상이어야 합니다")
                Long shippingPolicyId,
        @Schema(
                        description = "환불 정책 ID",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "환불 정책 ID는 필수입니다")
                @Min(value = 1, message = "환불 정책 ID는 1 이상이어야 합니다")
                Long refundPolicyId,
        @Schema(
                        description = "상품 그룹명",
                        example = "나이키 에어맥스 90",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "상품 그룹명은 필수입니다")
                @Size(max = 200, message = "상품 그룹명은 200자 이하여야 합니다")
                String productGroupName,
        @Schema(
                        description =
                                "옵션 타입 (COMBINATION, SINGLE, NONE). 미입력 시 optionGroups 수로 자동 결정",
                        example = "COMBINATION",
                        nullable = true)
                String optionType,
        @Schema(description = "정가", example = "100000", requiredMode = Schema.RequiredMode.REQUIRED)
                @Min(value = 0, message = "정가는 0 이상이어야 합니다")
                int regularPrice,
        @Schema(description = "판매가", example = "89000", requiredMode = Schema.RequiredMode.REQUIRED)
                @Min(value = 0, message = "판매가는 0 이상이어야 합니다")
                int currentPrice,
        @Schema(description = "이미지 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotEmpty(message = "이미지는 최소 1개 이상 필요합니다")
                @Valid
                List<ImageApiRequest> images,
        @Schema(description = "옵션 그룹 목록") @Valid List<OptionGroupApiRequest> optionGroups,
        @Schema(description = "상품 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotEmpty(message = "상품은 최소 1개 이상 필요합니다")
                @Valid
                List<ProductApiRequest> products,
        @Schema(description = "상세 설명", nullable = true) @Valid DescriptionApiRequest description,
        @Schema(description = "고시정보", nullable = true) @Valid NoticeApiRequest notice) {

    /** 이미지 API Request. */
    @Schema(description = "이미지 데이터")
    public record ImageApiRequest(
            @Schema(
                            description = "이미지 유형 (THUMBNAIL, DETAIL)",
                            example = "THUMBNAIL",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이미지 타입은 필수입니다")
                    String imageType,
            @Schema(
                            description = "이미지 URL",
                            example = "https://example.com/image.jpg",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이미지 URL은 필수입니다")
                    String imageUrl,
            @Schema(
                            description = "정렬 순서",
                            example = "0",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다")
                    int sortOrder) {}

    /** 옵션 그룹 API Request. */
    @Schema(description = "옵션 그룹 데이터")
    public record OptionGroupApiRequest(
            @Schema(
                            description = "옵션 그룹명",
                            example = "색상",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "옵션 그룹명은 필수입니다")
                    String optionGroupName,
            @Schema(
                            description = "정렬 순서",
                            example = "0",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다")
                    int sortOrder,
            @Schema(description = "옵션 값 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotEmpty(message = "옵션 값은 최소 1개 이상 필요합니다")
                    @Valid
                    List<OptionValueApiRequest> optionValues) {}

    /** 옵션 값 API Request. */
    @Schema(description = "옵션 값 데이터")
    public record OptionValueApiRequest(
            @Schema(
                            description = "옵션 값명",
                            example = "블랙",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "옵션 값명은 필수입니다")
                    String optionValueName,
            @Schema(
                            description = "정렬 순서",
                            example = "0",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다")
                    int sortOrder) {}

    /** 선택된 옵션 API Request. */
    @Schema(description = "선택된 옵션 데이터")
    public record SelectedOptionApiRequest(
            @Schema(
                            description = "옵션 그룹명",
                            example = "색상",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "옵션 그룹명은 필수입니다")
                    String optionGroupName,
            @Schema(
                            description = "옵션 값명",
                            example = "블랙",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "옵션 값명은 필수입니다")
                    String optionValueName) {}

    /** 상품 API Request. */
    @Schema(description = "상품 데이터")
    public record ProductApiRequest(
            @Schema(description = "SKU 코드", example = "SKU-001", nullable = true) String skuCode,
            @Schema(
                            description = "정가",
                            example = "100000",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "정가는 0 이상이어야 합니다")
                    int regularPrice,
            @Schema(
                            description = "판매가",
                            example = "89000",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "판매가는 0 이상이어야 합니다")
                    int currentPrice,
            @Schema(
                            description = "재고 수량",
                            example = "100",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다")
                    int stockQuantity,
            @Schema(
                            description = "정렬 순서",
                            example = "0",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다")
                    int sortOrder,
            @Schema(description = "선택된 옵션 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "옵션 선택 목록은 필수입니다")
                    @Valid
                    List<SelectedOptionApiRequest> selectedOptions) {}

    /** 상세설명 API Request. */
    @Schema(description = "상세 설명 데이터")
    public record DescriptionApiRequest(
            @Schema(
                            description = "상세 설명 내용 (HTML)",
                            example = "<p>상품 상세 설명입니다.</p>",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "상세설명 내용은 필수입니다")
                    String content,
            @Schema(description = "상세설명 이미지 목록") @Valid
                    List<DescriptionImageApiRequest> descriptionImages) {}

    /** 상세설명 이미지 API Request. */
    @Schema(description = "상세설명 이미지 데이터")
    public record DescriptionImageApiRequest(
            @Schema(
                            description = "이미지 URL",
                            example = "https://example.com/desc-image.jpg",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이미지 URL은 필수입니다")
                    String imageUrl,
            @Schema(
                            description = "정렬 순서",
                            example = "0",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다")
                    int sortOrder) {}

    /** 고시정보 API Request. */
    @Schema(description = "고시정보 데이터")
    public record NoticeApiRequest(
            @Schema(description = "고시 항목 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                    @Valid
                    @NotNull(message = "고시 항목 목록은 필수입니다")
                    @Size(min = 1, message = "고시 항목은 최소 1개 이상이어야 합니다")
                    List<NoticeEntryApiRequest> entries) {}

    /** 고시정보 항목 API Request. */
    @Schema(description = "고시정보 항목 데이터")
    public record NoticeEntryApiRequest(
            @Schema(
                            description = "고시 필드 ID",
                            example = "1",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "고시 필드 ID는 필수입니다")
                    @Min(value = 1, message = "고시 필드 ID는 1 이상이어야 합니다")
                    Long noticeFieldId,
            @Schema(
                            description = "필드명",
                            example = "소재",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "필드명은 필수입니다")
                    String fieldName,
            @Schema(
                            description = "필드 값",
                            example = "면 100%",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "필드 값은 필수입니다")
                    String fieldValue) {}
}
