package com.ryuqq.setof.application.discount.port.out.query;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
import java.util.Optional;

/**
 * 할인 정책 Query Port (Port-Out)
 *
 * <p>할인 정책 Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DiscountPolicyQueryPort {

    /**
     * ID로 할인 정책 단건 조회
     *
     * @param id 할인 정책 ID (Value Object)
     * @return 할인 정책 Domain (Optional)
     */
    Optional<DiscountPolicy> findById(DiscountPolicyId id);

    /**
     * 셀러 ID로 할인 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 할인 정책 목록
     */
    List<DiscountPolicy> findBySellerId(Long sellerId, boolean includeDeleted);

    /**
     * 셀러 ID와 그룹으로 할인 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @param discountGroup 할인 그룹
     * @param activeOnly 활성 정책만 조회 여부
     * @return 할인 정책 목록
     */
    List<DiscountPolicy> findBySellerIdAndGroup(
            Long sellerId, DiscountGroup discountGroup, boolean activeOnly);

    /**
     * 적용 대상 ID로 적용 가능한 할인 정책 조회
     *
     * @param targetType 적용 대상 타입
     * @param targetId 적용 대상 ID
     * @param activeOnly 활성 정책만 조회 여부
     * @return 할인 정책 목록
     */
    List<DiscountPolicy> findByTargetTypeAndTargetId(
            DiscountTargetType targetType, Long targetId, boolean activeOnly);

    /**
     * 현재 유효한 할인 정책 목록 조회 (유효 기간 내)
     *
     * @param sellerId 셀러 ID
     * @return 현재 유효한 할인 정책 목록
     */
    List<DiscountPolicy> findValidPolicies(Long sellerId);

    /**
     * 셀러의 할인 정책 개수 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 할인 정책 개수
     */
    long countBySellerId(Long sellerId, boolean includeDeleted);

    /**
     * 할인 정책 ID 존재 여부 확인
     *
     * @param id 할인 정책 ID
     * @return 존재 여부
     */
    boolean existsById(DiscountPolicyId id);
}
