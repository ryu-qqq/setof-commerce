package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 할인 대상 Response (Sealed Interface)
 *
 * @author development-team
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductDiscountTargetV1ApiResponse.class,
                name = "productDiscountTarget"),
        @JsonSubTypes.Type(value = SellerDiscountTargetV1ApiResponse.class,
                name = "sellerDiscountTarget")})
@Schema(description = "할인 대상 응답")
public sealed interface DiscountTargetV1ApiResponse
        permits ProductDiscountTargetV1ApiResponse, SellerDiscountTargetV1ApiResponse {

    @Schema(description = "발행 타입", example = "PRODUCT", requiredMode = Schema.RequiredMode.REQUIRED)
    String getType();
}


/**
 * V1 상품 할인 대상 Response
 */
@Schema(description = "상품 할인 대상 응답")
record ProductDiscountTargetV1ApiResponse(
        @Schema(description = "할인 정책 ID", example = "1") Long discountPolicyId,
        @Schema(description = "할인 대상 ID", example = "1") Long discountTargetId,
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime insertDate)
        implements DiscountTargetV1ApiResponse {

    @Override
    public String getType() {
        return "PRODUCT";
    }
}


/**
 * V1 셀러 할인 대상 Response
 */
@Schema(description = "셀러 할인 대상 응답")
record SellerDiscountTargetV1ApiResponse(
        @Schema(description = "할인 정책 ID", example = "1") Long discountPolicyId,
        @Schema(description = "할인 대상 ID", example = "1") Long discountTargetId,
        @Schema(description = "셀러명", example = "셀러명") String sellerName,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00") @JsonFormat(
                pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime insertDate)
        implements DiscountTargetV1ApiResponse {

    @Override
    public String getType() {
        return "SELLER";
    }
}
