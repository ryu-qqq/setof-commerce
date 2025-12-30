package com.ryuqq.setof.application.claim.port.in.query;

import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import java.util.List;

/**
 * GetClaimsByOrderUseCase - 주문별 클레임 목록 조회 UseCase
 *
 * @author development-team
 * @since 2.0.0
 */
public interface GetClaimsByOrderUseCase {

    /**
     * 주문 ID로 클레임 목록 조회
     *
     * @param orderId 주문 ID
     * @return 클레임 목록
     */
    List<ClaimResponse> getByOrderId(String orderId);
}
