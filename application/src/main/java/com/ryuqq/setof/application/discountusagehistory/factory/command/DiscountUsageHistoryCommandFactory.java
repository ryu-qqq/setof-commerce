package com.ryuqq.setof.application.discountusagehistory.factory.command;

import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.discount.aggregate.DiscountUsageHistory;
import com.ryuqq.setof.domain.discount.vo.CostShare;
import com.ryuqq.setof.domain.discount.vo.DiscountAmount;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.order.vo.OrderMoney;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * 할인 사용 이력 Command Factory
 *
 * <p>Checkout 완료 시점에 DiscountUsageHistory 도메인 객체를 생성합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountUsageHistoryCommandFactory {

    private final ClockHolder clockHolder;

    public DiscountUsageHistoryCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 할인 사용 이력 생성
     *
     * @param discountPolicyId 할인 정책 ID
     * @param memberId 회원 ID
     * @param checkoutId 결제 세션 ID
     * @param orderId 주문 ID
     * @param appliedAmount 적용된 할인 금액
     * @param originalAmount 할인 전 원래 금액
     * @param costShare 비용 분담 비율
     * @return 할인 사용 이력 도메인 객체
     */
    public DiscountUsageHistory create(
            DiscountPolicyId discountPolicyId,
            String memberId,
            CheckoutId checkoutId,
            OrderId orderId,
            DiscountAmount appliedAmount,
            OrderMoney originalAmount,
            CostShare costShare) {

        Instant now = Instant.now(clockHolder.getClock());

        return DiscountUsageHistory.forNew(
                discountPolicyId,
                memberId,
                checkoutId,
                orderId,
                appliedAmount,
                originalAmount,
                costShare,
                now);
    }
}
