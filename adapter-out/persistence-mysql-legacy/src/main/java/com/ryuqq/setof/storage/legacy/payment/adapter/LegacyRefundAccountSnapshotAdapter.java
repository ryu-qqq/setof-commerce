package com.ryuqq.setof.storage.legacy.payment.adapter;

import com.ryuqq.setof.application.payment.port.out.command.RefundAccountSnapshotCommandPort;
import com.ryuqq.setof.domain.payment.vo.RefundAccountSnapshot;
import com.ryuqq.setof.storage.legacy.refundaccount.entity.LegacyRefundAccountEntity;
import com.ryuqq.setof.storage.legacy.refundaccount.repository.LegacyRefundAccountJpaRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * LegacyRefundAccountSnapshotAdapter - 환불 계좌 Legacy 어댑터.
 *
 * <p>RefundAccountSnapshot에서 userId + 계좌 정보를 추출하여 refund_account 테이블에 저장합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyRefundAccountSnapshotAdapter implements RefundAccountSnapshotCommandPort {

    private final LegacyRefundAccountJpaRepository repository;

    public LegacyRefundAccountSnapshotAdapter(LegacyRefundAccountJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void persist(RefundAccountSnapshot refundAccountSnapshot) {
        LocalDateTime now = LocalDateTime.now();
        LegacyRefundAccountEntity entity =
                LegacyRefundAccountEntity.create(
                        refundAccountSnapshot.userId(),
                        refundAccountSnapshot.bankCode(),
                        refundAccountSnapshot.accountNumber(),
                        refundAccountSnapshot.accountHolderName(),
                        now,
                        now);
        repository.save(entity);
    }
}
