package com.ryuqq.setof.application.claim.port.out.command;

import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.vo.ClaimId;

/**
 * ClaimPersistencePort - Claim 영속성 Port
 *
 * <p>Claim 저장/수정을 위한 Outbound Port입니다.
 *
 * <p>규칙:
 *
 * <ul>
 *   <li>persist(): 저장/수정 통합 (JPA merge 활용 - ID 유무로 INSERT/UPDATE 분기)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public interface ClaimPersistencePort {

    /**
     * 클레임 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param claim 저장/수정할 클레임
     * @return 저장된 클레임 ID (Value Object)
     */
    ClaimId persist(Claim claim);
}
