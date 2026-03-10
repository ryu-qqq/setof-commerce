package com.ryuqq.setof.application.payment.internal;

import com.ryuqq.setof.application.payment.dto.bundle.PaymentCreationBundle;
import com.ryuqq.setof.application.payment.manager.PaymentCommandManager;
import com.ryuqq.setof.application.payment.manager.PaymentOrderCommandManager;
import com.ryuqq.setof.application.payment.manager.RefundAccountSnapshotManager;
import com.ryuqq.setof.application.payment.manager.ShippingSnapshotCommandManager;
import com.ryuqq.setof.application.payment.port.out.command.PaymentCommandPort.PaymentCommandResult;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * PaymentPersistFacade - 결제 영속 Facade.
 *
 * <p>Payment + Order + 배송지 스냅샷 + 환불 계좌를 하나의 @Transactional 경계로 묶습니다.
 *
 * <p>최상위 객체(Payment) 저장 → paymentId를 하위 도메인에 세팅 → 하위 도메인 저장.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentPersistFacade {

    private final PaymentCommandManager paymentCommandManager;
    private final PaymentOrderCommandManager orderCommandManager;
    private final ShippingSnapshotCommandManager shippingSnapshotCommandManager;
    private final RefundAccountSnapshotManager refundAccountSnapshotManager;

    public PaymentPersistFacade(
            PaymentCommandManager paymentCommandManager,
            PaymentOrderCommandManager orderCommandManager,
            ShippingSnapshotCommandManager shippingSnapshotCommandManager,
            RefundAccountSnapshotManager refundAccountSnapshotManager) {
        this.paymentCommandManager = paymentCommandManager;
        this.orderCommandManager = orderCommandManager;
        this.shippingSnapshotCommandManager = shippingSnapshotCommandManager;
        this.refundAccountSnapshotManager = refundAccountSnapshotManager;
    }

    /**
     * Payment + Order + 배송지 스냅샷 + 환불 계좌를 하나의 트랜잭션으로 영속화합니다.
     *
     * @param bundle 결제 생성 번들
     * @return 영속화 결과
     */
    @Transactional
    public PaymentPersistResult persist(PaymentCreationBundle bundle) {
        PaymentCommandResult paymentResult = paymentCommandManager.persist(bundle.payment());

        bundle.order().assignPaymentId(paymentResult.paymentId());

        List<Long> orderIds = orderCommandManager.persist(bundle.order());

        shippingSnapshotCommandManager.persist(bundle.order());

        if (bundle.refundAccountSnapshot() != null) {
            refundAccountSnapshotManager.persist(bundle.refundAccountSnapshot());
        }

        return new PaymentPersistResult(
                paymentResult.paymentId(), paymentResult.paymentUniqueId(), orderIds);
    }

    /**
     * 영속화 결과.
     *
     * @param paymentId 생성된 결제 ID
     * @param paymentUniqueId PG 결제용 고유 ID
     * @param orderIds 생성된 주문 아이템 ID 목록
     */
    public record PaymentPersistResult(
            long paymentId, String paymentUniqueId, List<Long> orderIds) {}
}
