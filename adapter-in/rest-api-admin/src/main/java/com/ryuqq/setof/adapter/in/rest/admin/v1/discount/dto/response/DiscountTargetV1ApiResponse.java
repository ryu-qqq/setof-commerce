package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 할인 대상 Response (Interface)
 *
 * @author development-team
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = ProductDiscountTargetV1ApiResponse.class,
            name = "productDiscountTarget"),
    @JsonSubTypes.Type(
            value = SellerDiscountTargetV1ApiResponse.class,
            name = "sellerDiscountTarget")
})
@Schema(description = "할인 대상 응답")
public interface DiscountTargetV1ApiResponse {

    @Schema(description = "발행 타입", example = "PRODUCT", requiredMode = Schema.RequiredMode.REQUIRED)
    String getType();
}
