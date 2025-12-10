package com.ryuqq.setof.application.refundaccount.port.out.command;

import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;

/**
 * RefundAccount Persistence Port (Command)
 *
 * <p>RefundAccount Aggregate를 저장하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefundAccountPersistencePort {

    /**
     * 환불계좌 저장 (신규 등록 또는 수정)
     *
     * <p>ID가 없으면 신규 등록, 있으면 수정
     *
     * @param refundAccount 저장할 환불계좌 도메인 객체
     * @return 저장된 환불계좌 ID
     */
    RefundAccountId persist(RefundAccount refundAccount);
}
