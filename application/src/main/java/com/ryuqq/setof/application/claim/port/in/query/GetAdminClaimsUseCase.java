package com.ryuqq.setof.application.claim.port.in.query;

import com.ryuqq.setof.application.claim.dto.query.GetAdminClaimsQuery;
import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.application.common.response.SliceResponse;

/**
 * GetAdminClaimsUseCase - Admin 클레임 목록 조회 UseCase
 *
 * <p>Admin에서 클레임 목록을 조회합니다. 커서 기반 페이지네이션을 지원합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface GetAdminClaimsUseCase {

    /**
     * Admin 클레임 목록 조회
     *
     * @param query 조회 조건 (sellerId, memberId, claimStatuses, claimTypes, 기간, 페이징)
     * @return Slice 형태의 클레임 목록
     */
    SliceResponse<ClaimResponse> getClaims(GetAdminClaimsQuery query);
}
