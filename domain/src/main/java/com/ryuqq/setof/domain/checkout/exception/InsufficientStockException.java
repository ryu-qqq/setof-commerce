package com.ryuqq.setof.domain.checkout.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.List;
import java.util.Map;

/**
 * InsufficientStockException - 재고 부족 예외
 *
 * <p>결제 세션 생성 또는 완료 시 재고가 부족한 경우 발생합니다.
 */
public class InsufficientStockException extends DomainException {

    /**
     * 단일 상품 재고 부족 예외 생성
     *
     * @param productStockId 재고 부족한 상품 재고 ID
     * @param requestedQuantity 요청 수량
     * @param availableQuantity 가용 수량
     * @return InsufficientStockException 인스턴스
     */
    public static InsufficientStockException forProduct(
            Long productStockId, int requestedQuantity, int availableQuantity) {
        return new InsufficientStockException(productStockId, requestedQuantity, availableQuantity);
    }

    /**
     * 복수 상품 재고 부족 예외 생성
     *
     * @param insufficientProductIds 재고 부족한 상품 재고 ID 목록
     * @return InsufficientStockException 인스턴스
     */
    public static InsufficientStockException forProducts(List<Long> insufficientProductIds) {
        return new InsufficientStockException(insufficientProductIds);
    }

    private InsufficientStockException(
            Long productStockId, int requestedQuantity, int availableQuantity) {
        super(
                CheckoutErrorCode.INSUFFICIENT_STOCK,
                String.format(
                        "재고 부족 - productStockId: %d, 요청: %d, 가용: %d",
                        productStockId, requestedQuantity, availableQuantity),
                Map.of(
                        "productStockId", productStockId,
                        "requestedQuantity", requestedQuantity,
                        "availableQuantity", availableQuantity));
    }

    private InsufficientStockException(List<Long> insufficientProductIds) {
        super(
                CheckoutErrorCode.INSUFFICIENT_STOCK,
                String.format("재고 부족 - productStockIds: %s", insufficientProductIds),
                Map.of("insufficientProductStockIds", insufficientProductIds));
    }
}
