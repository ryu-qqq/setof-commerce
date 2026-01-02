package com.ryuqq.setof.domain.checkout.event;

import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import java.time.Instant;

/**
 * CheckoutExpiredEvent - 결제 세션 만료 이벤트
 *
 * <p>결제 세션이 만료되었을 때 발행됩니다. 이 이벤트 이후 재고 예약 해제 등의 작업이 수행됩니다.
 *
 * @param checkoutId 결제 세션 ID
 * @param memberId 회원 ID (UUIDv7 String)
 * @param occurredAt 이벤트 발생 시각
 */
public record CheckoutExpiredEvent(CheckoutId checkoutId, String memberId, Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Checkout Aggregate로부터 이벤트 생성
     *
     * @param checkout Checkout Aggregate
     * @param occurredAt 이벤트 발생 시각
     * @return CheckoutExpiredEvent 인스턴스
     */
    public static CheckoutExpiredEvent from(Checkout checkout, Instant occurredAt) {
        return new CheckoutExpiredEvent(checkout.id(), checkout.memberId(), occurredAt);
    }
}
