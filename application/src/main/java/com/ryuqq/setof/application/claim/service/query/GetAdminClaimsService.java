package com.ryuqq.setof.application.claim.service.query;

import com.ryuqq.setof.application.claim.assembler.ClaimAssembler;
import com.ryuqq.setof.application.claim.dto.query.GetAdminClaimsQuery;
import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.application.claim.port.in.query.GetAdminClaimsUseCase;
import com.ryuqq.setof.application.claim.port.out.query.ClaimQueryPort;
import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Admin 클레임 목록 조회 Service
 *
 * <p>Admin에서 클레임 목록을 조회합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class GetAdminClaimsService implements GetAdminClaimsUseCase {

    private final ClaimQueryPort claimQueryPort;
    private final ClaimAssembler claimAssembler;

    public GetAdminClaimsService(ClaimQueryPort claimQueryPort, ClaimAssembler claimAssembler) {
        this.claimQueryPort = claimQueryPort;
        this.claimAssembler = claimAssembler;
    }

    /**
     * Admin 클레임 목록 조회
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param query Admin 조회 조건
     * @return Slice 형태의 클레임 목록
     */
    @Override
    @Transactional(readOnly = true)
    public SliceResponse<ClaimResponse> getClaims(GetAdminClaimsQuery query) {
        List<Claim> claims = claimQueryPort.findByAdminQuery(query);

        boolean hasNext = claims.size() > query.pageSize();
        List<Claim> content = hasNext ? claims.subList(0, query.pageSize()) : claims;

        List<ClaimResponse> responses = content.stream().map(claimAssembler::toResponse).toList();

        String nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            Claim lastClaim = content.get(content.size() - 1);
            nextCursor = lastClaim.claimId().value();
        }

        return SliceResponse.of(responses, query.pageSize(), hasNext, nextCursor);
    }
}
