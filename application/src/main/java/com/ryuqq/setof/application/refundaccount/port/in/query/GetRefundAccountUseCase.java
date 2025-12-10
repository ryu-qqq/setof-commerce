package com.ryuqq.setof.application.refundaccount.port.in.query;

import com.ryuqq.setof.application.refundaccount.dto.response.RefundAccountResponse;
import java.util.Optional;
import java.util.UUID;

/**
 * Get RefundAccount UseCase (Query)
 *
 * <p>환불계좌 조회를 담당하는 Inbound Port
 *
 * <p>회원당 최대 1개이므로 memberId로 단건 조회
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetRefundAccountUseCase {

    /**
     * 회원의 환불계좌 조회 실행
     *
     * @param memberId 회원 ID
     * @return 환불계좌 정보 (없으면 Optional.empty())
     */
    Optional<RefundAccountResponse> execute(UUID memberId);
}
