package com.ryuqq.setof.application.refundaccount.port.out.query;

import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import java.util.Optional;

/**
 * RefundAccountQueryPort - 환불 계좌 조회 Port.
 *
 * <p>Adapter가 구현할 출력 포트입니다. 도메인 객체를 반환하며, Application 레이어에서 Result DTO로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface RefundAccountQueryPort {

    /**
     * userId로 활성 환불 계좌를 도메인 객체로 조회합니다 (deleteYn = 'N' 필터 적용).
     *
     * @param userId 사용자 ID
     * @return RefundAccount 도메인 객체 (Optional)
     */
    Optional<RefundAccount> fetchRefundAccount(long userId);

    /**
     * userId + refundAccountId로 도메인 객체를 조회합니다 (deleteYn 필터 없음).
     *
     * <p>수정/삭제 전 소유권 검증 및 엔티티 취득에 사용합니다.
     *
     * @param userId 사용자 ID
     * @param refundAccountId 환불 계좌 ID
     * @return RefundAccount 도메인 객체 (Optional)
     */
    Optional<RefundAccount> findByUserIdAndId(long userId, long refundAccountId);
}
