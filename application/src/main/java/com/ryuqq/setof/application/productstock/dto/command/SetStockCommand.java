package com.ryuqq.setof.application.productstock.dto.command;

/**
 * Set Stock Command
 *
 * <p>재고 설정 요청 데이터를 담는 불변 객체
 *
 * @param productId 상품 ID
 * @param quantity 설정할 재고 수량
 * @author development-team
 * @since 1.0.0
 */
public record SetStockCommand(Long productId, int quantity) {

    /** Compact Constructor - 검증 */
    public SetStockCommand {
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다");
        }
    }
}
