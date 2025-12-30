package com.ryuqq.setof.domain.checkout.event;

import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.checkout.vo.CheckoutMoney;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import java.time.Instant;

/**
 * CheckoutCompletedEvent - 결제 세션 완료 이벤트
 *
 * <p>결제가 성공적으로 완료되었을 때 발행됩니다. 이 이벤트 이후 주문(Order)이 생성됩니다.
 *
 * @param checkoutId 결제 세션 ID
 * @param memberId 회원 ID (UUIDv7 String)
 * @param finalAmount 최종 결제 금액
 * @param occurredAt 이벤트 발생 시각
 */
public record CheckoutCompletedEvent(
        CheckoutId checkoutId, String memberId, CheckoutMoney finalAmount, Instant occurredAt)
        implements DomainEvent {

    /**
     * Static Factory Method - Checkout Aggregate로부터 이벤트 생성
     *
     * @param checkout Checkout Aggregate
     * @param occurredAt 이벤트 발생 시각
     * @return CheckoutCompletedEvent 인스턴스
     */
    public static CheckoutCompletedEvent from(Checkout checkout, Instant occurredAt) {
        return new CheckoutCompletedEvent(
                checkout.id(), checkout.memberId(), checkout.finalAmount(), occurredAt);
    }
}
