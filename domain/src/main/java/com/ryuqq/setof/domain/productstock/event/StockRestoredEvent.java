package com.ryuqq.setof.domain.productstock.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import com.ryuqq.setof.domain.productstock.vo.ProductStockId;
import java.time.Instant;

/**
 * 재고 복원 이벤트
 *
 * <p>재고가 복원되었을 때 발행되는 도메인 이벤트입니다.
 *
 * <p>Domain Event 규칙:
 *
 * <ul>
 *   <li>Record 타입 필수 (불변성 보장)
 *   <li>occurredAt 필드 필수 (Instant)
 *   <li>from() 정적 팩토리 메서드 필수
 *   <li>과거형 네이밍 (*RestoredEvent)
 * </ul>
 *
 * @param productStockId 재고 ID
 * @param productId 상품 ID
 * @param previousQuantity 이전 재고 수량
 * @param restoredQuantity 복원된 수량
 * @param currentQuantity 현재 재고 수량
 * @param occurredAt 이벤트 발생 시각
 */
public record StockRestoredEvent(
        ProductStockId productStockId,
        ProductId productId,
        int previousQuantity,
        int restoredQuantity,
        int currentQuantity,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * ProductStock으로부터 이벤트 생성
     *
     * @param previousStock 복원 전 ProductStock
     * @param currentStock 복원 후 ProductStock
     * @param restoredQuantity 복원된 수량
     * @param occurredAt 이벤트 발생 시각
     * @return StockRestoredEvent 인스턴스
     */
    public static StockRestoredEvent from(
            ProductStock previousStock,
            ProductStock currentStock,
            int restoredQuantity,
            Instant occurredAt) {
        return new StockRestoredEvent(
                currentStock.getId(),
                currentStock.getProductId(),
                previousStock.getQuantityValue(),
                restoredQuantity,
                currentStock.getQuantityValue(),
                occurredAt);
    }
}
