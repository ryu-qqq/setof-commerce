package com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 일괄 재고 설정 API 요청 DTO
 *
 * <p>상품 그룹 내 여러 상품의 재고 일괄 설정 요청
 *
 * @param stocks 재고 설정 목록
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "일괄 재고 설정 요청")
public record BatchSetStockV2ApiRequest(
        @Schema(description = "재고 설정 목록") @NotEmpty(message = "재고 설정 목록은 비어있을 수 없습니다") @Valid
                List<StockItem> stocks) {

    /**
     * 개별 재고 설정 항목
     *
     * @param productId 상품 ID
     * @param quantity 설정할 재고 수량
     */
    @Schema(description = "개별 재고 설정 항목")
    public record StockItem(
            @Schema(description = "상품 ID", example = "1001") @NotNull(message = "상품 ID는 필수입니다")
                    Long productId,
            @Schema(description = "설정할 재고 수량", example = "100", minimum = "0")
                    @NotNull(message = "재고 수량은 필수입니다")
                    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다")
                    Integer quantity) {}
}
