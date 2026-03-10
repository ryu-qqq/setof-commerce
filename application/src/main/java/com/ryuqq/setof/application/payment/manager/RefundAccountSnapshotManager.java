package com.ryuqq.setof.application.payment.manager;

import com.ryuqq.setof.application.payment.port.out.command.RefundAccountSnapshotCommandPort;
import com.ryuqq.setof.domain.payment.vo.RefundAccountSnapshot;
import org.springframework.stereotype.Component;

/**
 * RefundAccountSnapshotManager - 환불 계좌 저장 Manager.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountSnapshotManager {

    private final RefundAccountSnapshotCommandPort refundAccountSnapshotCommandPort;

    public RefundAccountSnapshotManager(
            RefundAccountSnapshotCommandPort refundAccountSnapshotCommandPort) {
        this.refundAccountSnapshotCommandPort = refundAccountSnapshotCommandPort;
    }

    public void persist(RefundAccountSnapshot refundAccountSnapshot) {
        refundAccountSnapshotCommandPort.persist(refundAccountSnapshot);
    }
}
