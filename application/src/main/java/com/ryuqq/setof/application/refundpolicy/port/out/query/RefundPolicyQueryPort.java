package com.ryuqq.setof.application.refundpolicy.port.out.query;

import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import java.util.List;
import java.util.Optional;

/**
 * 환불 정책 Query Port (Port-Out)
 *
 * <p>환불 정책 Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefundPolicyQueryPort {

    /**
     * ID로 환불 정책 단건 조회
     *
     * @param id 환불 정책 ID (Value Object)
     * @return 환불 정책 Domain (Optional)
     */
    Optional<RefundPolicy> findById(RefundPolicyId id);

    /**
     * 셀러 ID로 환불 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 환불 정책 목록
     */
    List<RefundPolicy> findBySellerId(Long sellerId, boolean includeDeleted);

    /**
     * 셀러의 기본 환불 정책 조회
     *
     * @param sellerId 셀러 ID
     * @return 기본 환불 정책 (Optional)
     */
    Optional<RefundPolicy> findDefaultBySellerId(Long sellerId);

    /**
     * 셀러의 환불 정책 개수 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 환불 정책 개수
     */
    long countBySellerId(Long sellerId, boolean includeDeleted);

    /**
     * 환불 정책 ID 존재 여부 확인
     *
     * @param id 환불 정책 ID
     * @return 존재 여부
     */
    boolean existsById(RefundPolicyId id);
}
