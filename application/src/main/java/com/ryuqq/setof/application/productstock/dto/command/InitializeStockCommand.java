package com.ryuqq.setof.application.productstock.dto.command;

/**
 * Initialize Stock Command
 *
 * <p>신규 재고 초기화 요청 데이터를 담는 불변 객체
 *
 * @param productId 상품 ID
 * @param initialQuantity 초기 재고 수량
 * @author development-team
 * @since 1.0.0
 */
public record InitializeStockCommand(Long productId, int initialQuantity) {

    /** Compact Constructor - 검증 */
    public InitializeStockCommand {
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다");
        }
        if (initialQuantity < 0) {
            throw new IllegalArgumentException("초기 재고 수량은 0 이상이어야 합니다");
        }
    }
}
