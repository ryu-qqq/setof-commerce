package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

/**
 * V1 상품 그룹 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 생성 요청")
public record CreateProductGroupV1ApiRequest(
        @Schema(description = "상품 그룹 ID", example = "1") Long productGroupId,
        @Schema(description = "상품 그룹명", example = "멋진 티셔츠",
                requiredMode = Schema.RequiredMode.REQUIRED) @Length(max = 200,
                        message = "상품 그룹명은 200자를 넘을 수 없습니다.") String productGroupName,
        @Schema(description = "셀러 ID", example = "100",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "셀러 ID는 필수입니다.") Long sellerId,
        @Schema(description = "옵션 타입", example = "SIZE_COLOR",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "옵션 타입은 필수입니다.") String optionType,
        @Schema(description = "관리 타입", example = "SELF",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "관리 타입은 필수입니다.") String managementType,
        @Schema(description = "카테고리 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "카테고리 ID는 필수입니다.") Long categoryId,
        @Schema(description = "브랜드 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "브랜드 ID는 필수입니다.") Long brandId,
        @Schema(description = "상품 상태") @Valid CreateProductStatusV1ApiRequest productStatus,
        @Schema(description = "가격 정보", requiredMode = Schema.RequiredMode.REQUIRED) @Valid @NotNull(
                message = "가격 정보는 필수입니다.") CreatePriceV1ApiRequest price,
        @Schema(description = "상품 고지 정보") @Valid CreateProductNoticeV1ApiRequest productNotice,
        @Schema(description = "의류 상세 정보") @Valid CreateClothesDetailV1ApiRequest clothesDetailInfo,
        @Schema(description = "배송 고지 정보") @Valid CreateDeliveryNoticeV1ApiRequest deliveryNotice,
        @Schema(description = "환불 고지 정보") @Valid CreateRefundNoticeV1ApiRequest refundNotice,
        @Schema(description = "상품 이미지 목록",
                requiredMode = Schema.RequiredMode.REQUIRED) @Valid @Size(min = 1, max = 10,
                        message = "상품 이미지는 1개 이상 10개 이하여야 합니다.") @NotNull(
                                message = "상품 이미지 목록은 필수입니다.") List<CreateProductImageV1ApiRequest> productImageList,
        @Schema(description = "상세 설명", requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                message = "상세 설명은 필수입니다.") String detailDescription,
        @Schema(description = "상품 옵션 목록", requiredMode = Schema.RequiredMode.REQUIRED) @Size(
                min = 1, message = "적어도 하나 이상의 상품 옵션이 필요합니다.") @Valid @NotNull(
                        message = "상품 옵션 목록은 필수입니다.") List<CreateOptionV1ApiRequest> productOptions) {
}
