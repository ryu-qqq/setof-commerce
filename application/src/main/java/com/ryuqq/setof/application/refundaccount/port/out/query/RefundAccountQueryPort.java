package com.ryuqq.setof.application.refundaccount.port.out.query;

import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import java.util.Optional;
import java.util.UUID;

/**
 * RefundAccount Query Port (Query)
 *
 * <p>RefundAccount Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefundAccountQueryPort {

    /**
     * ID로 환불계좌 단건 조회 (삭제되지 않은 것만)
     *
     * @param id 환불계좌 ID
     * @return RefundAccount Domain (Optional)
     */
    Optional<RefundAccount> findById(RefundAccountId id);

    /**
     * 회원 ID로 환불계좌 조회 (삭제되지 않은 것만)
     *
     * <p>회원당 최대 1개이므로 단건 조회
     *
     * @param memberId 회원 ID
     * @return RefundAccount Domain (Optional)
     */
    Optional<RefundAccount> findByMemberId(UUID memberId);

    /**
     * 회원의 환불계좌 존재 여부 확인 (삭제되지 않은 것만)
     *
     * @param memberId 회원 ID
     * @return 존재하면 true
     */
    boolean existsByMemberId(UUID memberId);
}
