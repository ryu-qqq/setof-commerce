package com.ryuqq.setof.application.shippingpolicy.port.out.query;

import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.util.List;
import java.util.Optional;

/**
 * 배송 정책 Query Port (Port-Out)
 *
 * <p>배송 정책 Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShippingPolicyQueryPort {

    /**
     * ID로 배송 정책 단건 조회
     *
     * @param id 배송 정책 ID (Value Object)
     * @return 배송 정책 Domain (Optional)
     */
    Optional<ShippingPolicy> findById(ShippingPolicyId id);

    /**
     * 셀러 ID로 배송 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 배송 정책 목록
     */
    List<ShippingPolicy> findBySellerId(Long sellerId, boolean includeDeleted);

    /**
     * 셀러의 기본 배송 정책 조회
     *
     * @param sellerId 셀러 ID
     * @return 기본 배송 정책 (Optional)
     */
    Optional<ShippingPolicy> findDefaultBySellerId(Long sellerId);

    /**
     * 셀러의 배송 정책 개수 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 배송 정책 개수
     */
    long countBySellerId(Long sellerId, boolean includeDeleted);

    /**
     * 배송 정책 ID 존재 여부 확인
     *
     * @param id 배송 정책 ID
     * @return 존재 여부
     */
    boolean existsById(ShippingPolicyId id);
}
