package com.ryuqq.setof.application.payment.port.out.command;

import com.ryuqq.setof.domain.payment.vo.RefundAccountSnapshot;

/**
 * 결제 시점 환불 계좌 저장 Port.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RefundAccountSnapshotCommandPort {

    /**
     * 환불 계좌를 저장합니다.
     *
     * @param refundAccountSnapshot 환불 계좌 스냅샷 (userId 포함)
     */
    void persist(RefundAccountSnapshot refundAccountSnapshot);
}
