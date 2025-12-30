package com.ryuqq.setof.application.claim.service.query;

import com.ryuqq.setof.application.claim.assembler.ClaimAssembler;
import com.ryuqq.setof.application.claim.dto.response.ClaimResponse;
import com.ryuqq.setof.application.claim.port.in.query.GetClaimUseCase;
import com.ryuqq.setof.application.claim.port.out.query.ClaimQueryPort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.exception.ClaimNotFoundException;
import com.ryuqq.setof.domain.claim.vo.ClaimId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetClaimService - 클레임 단건 조회 Service
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
@Transactional(readOnly = true)
public class GetClaimService implements GetClaimUseCase {

    private final ClaimQueryPort claimQueryPort;
    private final ClaimAssembler claimAssembler;

    public GetClaimService(ClaimQueryPort claimQueryPort, ClaimAssembler claimAssembler) {
        this.claimQueryPort = claimQueryPort;
        this.claimAssembler = claimAssembler;
    }

    @Override
    public ClaimResponse getByClaimId(String claimId) {
        Claim claim =
                claimQueryPort
                        .findByClaimId(ClaimId.of(claimId))
                        .orElseThrow(() -> ClaimNotFoundException.byId(claimId));
        return claimAssembler.toResponse(claim);
    }
}
