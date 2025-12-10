package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * V1 옵션 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "옵션 생성 요청")
public record CreateOptionV1ApiRequest(
        @Schema(description = "상품 ID",
                example = "123") @JsonInclude(JsonInclude.Include.NON_NULL) Long productId,
        @Schema(description = "재고 수량", example = "100",
                requiredMode = Schema.RequiredMode.REQUIRED) @Max(value = 9999,
                        message = "재고 수량은 9999를 넘을 수 없습니다.") @Min(value = 0,
                                message = "재고 수량은 0 이상이어야 합니다.") Integer quantity,
        @Schema(description = "추가 가격", example = "5000") BigDecimal additionalPrice,
        @Schema(description = "옵션 상세 목록", requiredMode = Schema.RequiredMode.REQUIRED) @Valid @Size(
                max = 2, message = "옵션 상세는 최대 2개까지 가능합니다.") @NotNull(
                        message = "옵션 상세 목록은 필수입니다.") List<CreateOptionDetailV1ApiRequest> options) {

    @Schema(description = "옵션 상세 생성 요청")
    public record CreateOptionDetailV1ApiRequest(
            @Schema(description = "옵션 그룹 ID",
                    example = "1") @JsonInclude(JsonInclude.Include.NON_NULL) Long optionGroupId,
            @Schema(description = "옵션 상세 ID",
                    example = "1") @JsonInclude(JsonInclude.Include.NON_NULL) Long optionDetailId,
            @Schema(description = "옵션명", example = "SIZE",
                    requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                            message = "옵션명은 필수입니다.") String optionName,
            @Schema(description = "옵션 값", example = "M",
                    requiredMode = Schema.RequiredMode.REQUIRED) @org.hibernate.validator.constraints.Length(
                            max = 100, message = "옵션 값은 100자를 넘을 수 없습니다.") @NotNull(
                                    message = "옵션 값은 필수입니다.") String optionValue) {
    }
}
