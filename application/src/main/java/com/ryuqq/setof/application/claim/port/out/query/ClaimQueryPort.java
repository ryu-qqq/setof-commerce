package com.ryuqq.setof.application.claim.port.out.query;

import com.ryuqq.setof.application.claim.dto.query.GetAdminClaimsQuery;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.vo.ClaimId;
import com.ryuqq.setof.domain.claim.vo.ClaimNumber;
import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
import com.ryuqq.setof.domain.order.vo.OrderId;
import java.util.List;
import java.util.Optional;

/**
 * ClaimQueryPort - Claim Query Port
 *
 * <p>Claim 조회를 위한 Outbound Port입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public interface ClaimQueryPort {

    /**
     * 클레임 ID로 조회
     *
     * @param claimId 클레임 ID (Value Object)
     * @return 클레임 Optional
     */
    Optional<Claim> findByClaimId(ClaimId claimId);

    /**
     * 클레임 번호로 조회
     *
     * @param claimNumber 클레임 번호 (Value Object)
     * @return 클레임 Optional
     */
    Optional<Claim> findByClaimNumber(ClaimNumber claimNumber);

    /**
     * 주문 ID로 클레임 목록 조회
     *
     * @param orderId 주문 ID (Value Object)
     * @return 클레임 목록
     */
    List<Claim> findByOrderId(OrderId orderId);

    /**
     * 상태로 클레임 목록 조회
     *
     * @param status 클레임 상태
     * @return 클레임 목록
     */
    List<Claim> findByStatus(ClaimStatus status);

    /**
     * 주문에 활성 클레임 존재 여부 확인
     *
     * @param orderId 주문 ID (Value Object)
     * @return 활성 클레임 존재 시 true
     */
    boolean existsActiveClaimByOrderId(OrderId orderId);

    /**
     * Admin 클레임 목록 조회
     *
     * <p>페이징을 위해 pageSize + 1 개를 조회하여 hasNext 판단에 사용
     *
     * @param query 조회 조건
     * @return 클레임 목록 (최대 pageSize + 1)
     */
    List<Claim> findByAdminQuery(GetAdminClaimsQuery query);
}
