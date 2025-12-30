package com.ryuqq.setof.application.claim.manager.query;

import com.ryuqq.setof.application.claim.port.out.query.ClaimQueryPort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.exception.ClaimNotFoundException;
import com.ryuqq.setof.domain.claim.vo.ClaimId;
import org.springframework.stereotype.Component;

/**
 * 클레임 조회 Manager
 *
 * <p>클레임 조회를 위한 Port 호출을 담당합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ClaimReadManager {

    private final ClaimQueryPort claimQueryPort;

    public ClaimReadManager(ClaimQueryPort claimQueryPort) {
        this.claimQueryPort = claimQueryPort;
    }

    /**
     * 클레임 ID로 조회
     *
     * @param claimId 클레임 ID (UUID String)
     * @return Claim 도메인 객체
     * @throws ClaimNotFoundException 클레임이 존재하지 않는 경우
     */
    public Claim findById(String claimId) {
        return claimQueryPort
                .findByClaimId(ClaimId.of(claimId))
                .orElseThrow(() -> ClaimNotFoundException.byId(claimId));
    }
}
