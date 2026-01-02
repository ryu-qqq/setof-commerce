package com.ryuqq.setof.domain.checkout.event;

import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.checkout.vo.CheckoutMoney;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import java.time.Instant;

/**
 * CheckoutCreatedEvent - 결제 세션 생성 이벤트
 *
 * <p>결제 세션이 생성되었을 때 발행됩니다.
 *
 * @param checkoutId 결제 세션 ID
 * @param memberId 회원 ID (UUIDv7 String)
 * @param totalAmount 총 결제 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record CheckoutCreatedEvent(
        CheckoutId checkoutId, String memberId, CheckoutMoney totalAmount, Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Checkout Aggregate로부터 이벤트 생성
     *
     * @param checkout Checkout Aggregate
     * @param occurredAt 이벤트 발생 시각
     * @return CheckoutCreatedEvent 인스턴스
     */
    public static CheckoutCreatedEvent from(Checkout checkout, Instant occurredAt) {
        return new CheckoutCreatedEvent(
                checkout.id(), checkout.memberId(), checkout.finalAmount(), occurredAt);
    }
}
