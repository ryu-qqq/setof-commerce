package com.ryuqq.setof.domain.product.event;

import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import java.time.Instant;

/**
 * 상품그룹 삭제 이벤트
 *
 * <p>상품그룹이 삭제되었을 때 발행되는 도메인 이벤트입니다.
 *
 * <p>Domain Event 규칙:
 *
 * <ul>
 *   <li>Record 타입 필수 (불변성 보장)
 *   <li>occurredAt 필드 필수 (Instant)
 *   <li>from() 정적 팩토리 메서드 필수
 *   <li>과거형 네이밍 (*DeletedEvent)
 * </ul>
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID
 * @param occurredAt 이벤트 발생 시각
 */
public record ProductGroupDeletedEvent(
        ProductGroupId productGroupId, SellerId sellerId, Instant occurredAt)
        implements DomainEvent {

    /**
     * ProductGroup으로부터 이벤트 생성
     *
     * @param productGroup 삭제된 상품그룹
     * @param occurredAt 이벤트 발생 시각
     * @return ProductGroupDeletedEvent 인스턴스
     */
    public static ProductGroupDeletedEvent from(ProductGroup productGroup, Instant occurredAt) {
        return new ProductGroupDeletedEvent(
                productGroup.getId(), productGroup.getSellerId(), occurredAt);
    }
}
