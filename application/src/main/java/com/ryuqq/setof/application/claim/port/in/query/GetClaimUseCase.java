package com.ryuqq.setof.application.claim.port.in.query;

import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;

/**
 * GetClaimUseCase - 클레임 단건 조회 UseCase
 *
 * @author development-team
 * @since 2.0.0
 */
public interface GetClaimUseCase {

    /**
     * 클레임 ID로 조회
     *
     * @param claimId 클레임 ID (UUID)
     * @return 클레임 응답
     */
    ClaimResponse getByClaimId(String claimId);
}
