package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * V1 상품 그룹 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 수정 요청")
public record UpdateProductGroupV1ApiRequest(
        @Schema(description = "배송 고지 정보") @Valid CreateDeliveryNoticeV1ApiRequest deliveryNotice,
        @Schema(description = "환불 고지 정보") @Valid CreateRefundNoticeV1ApiRequest refundNotice,
        @Schema(description = "상품 고지 정보") @Valid CreateProductNoticeV1ApiRequest productNotice,
        @Schema(description = "상품 이미지 목록") @Valid @Size(min = 1, max = 10,
                message = "상품 이미지는 1개 이상 10개 이하여야 합니다.") List<CreateProductImageV1ApiRequest> productImageList,
        @Schema(description = "상세 설명") UpdateProductDescriptionV1ApiRequest detailDescription,
        @Schema(description = "상품 옵션 목록") @Valid List<CreateOptionV1ApiRequest> productOptions,
        @Schema(description = "상품 그룹 상세 정보") ProductGroupDetailsV1ApiRequest productGroupDetails,
        @Schema(description = "수정 상태") UpdateStatusV1ApiRequest updateStatus) {

    @Schema(description = "상품 그룹 상세 정보")
    public record ProductGroupDetailsV1ApiRequest(
            @Schema(description = "상품 그룹명", example = "멋진 티셔츠") String productGroupName,
            @Schema(description = "옵션 타입", example = "SIZE_COLOR") String optionType,
            @Schema(description = "관리 타입", example = "SELF") String managementType,
            @Schema(description = "카테고리 ID", example = "1") Long categoryId,
            @Schema(description = "브랜드 ID", example = "1") Long brandId,
            @Schema(description = "상품 상태") CreateProductStatusV1ApiRequest productStatus,
            @Schema(description = "의류 상세 정보") CreateClothesDetailV1ApiRequest clothesDetailInfo) {
    }

    @Schema(description = "수정 상태")
    public record UpdateStatusV1ApiRequest(
            @Schema(description = "상품 상태 수정 여부", example = "true") Boolean productStatus,
            @Schema(description = "고지 상태 수정 여부", example = "true") Boolean noticeStatus,
            @Schema(description = "이미지 상태 수정 여부", example = "true") Boolean imageStatus,
            @Schema(description = "설명 상태 수정 여부", example = "true") Boolean descriptionStatus,
            @Schema(description = "재고 옵션 상태 수정 여부", example = "true") Boolean stockOptionStatus,
            @Schema(description = "배송 상태 수정 여부", example = "true") Boolean deliveryStatus,
            @Schema(description = "환불 상태 수정 여부", example = "true") Boolean refundStatus) {
    }
}
