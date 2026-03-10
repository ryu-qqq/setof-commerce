package com.ryuqq.setof.storage.legacy.payment.adapter;

import com.ryuqq.setof.application.payment.port.out.command.PaymentCommandPort;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.storage.legacy.payment.entity.LegacyPaymentBillEntity;
import com.ryuqq.setof.storage.legacy.payment.entity.LegacyPaymentEntity;
import com.ryuqq.setof.storage.legacy.payment.entity.LegacyPaymentStatus;
import com.ryuqq.setof.storage.legacy.payment.repository.LegacyPaymentBillJpaRepository;
import com.ryuqq.setof.storage.legacy.payment.repository.LegacyPaymentJpaRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentCreateAdapter - Payment 도메인 → Legacy 스키마 영속 어댑터.
 *
 * <p>Legacy 저장 순서: payment → payment_bill.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentCommandAdapter implements PaymentCommandPort {

    private static final String SITE_NAME = "SET-OF";

    private final LegacyPaymentJpaRepository paymentRepository;
    private final LegacyPaymentBillJpaRepository paymentBillRepository;

    public LegacyPaymentCommandAdapter(
            LegacyPaymentJpaRepository paymentRepository,
            LegacyPaymentBillJpaRepository paymentBillRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentBillRepository = paymentBillRepository;
    }

    @Override
    public PaymentCommandResult persist(Payment payment) {
        LegacyPaymentEntity paymentEntity =
                LegacyPaymentEntity.create(
                        payment.legacyUserIdValue(),
                        payment.paymentAmountValue(),
                        LegacyPaymentStatus.PAYMENT_PROCESSING,
                        SITE_NAME);
        LegacyPaymentEntity savedPayment = paymentRepository.save(paymentEntity);

        String paymentUniqueId = UUID.randomUUID().toString().replace("-", "");
        LegacyPaymentBillEntity paymentBill =
                LegacyPaymentBillEntity.create(
                        savedPayment.getId(),
                        payment.legacyUserIdValue(),
                        payment.paymentAmountValue(),
                        payment.usedMileage().amount(),
                        paymentUniqueId);
        paymentBillRepository.save(paymentBill);

        return new PaymentCommandResult(savedPayment.getId(), paymentUniqueId);
    }
}
