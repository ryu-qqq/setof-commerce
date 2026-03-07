package com.ryuqq.setof.application.refundaccount.port.out.command;

import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;

/**
 * RefundAccountCommandPort - 환불 계좌 명령 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다. 도메인 객체를 받아 persist하며, 하이버네이트 더티체킹으로 상태를 반영합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RefundAccountCommandPort {

    /**
     * 환불 계좌를 저장합니다.
     *
     * @param refundAccount 저장할 환불 계좌 도메인 객체
     * @return 환불 계좌 ID
     */
    Long persist(RefundAccount refundAccount);
}
