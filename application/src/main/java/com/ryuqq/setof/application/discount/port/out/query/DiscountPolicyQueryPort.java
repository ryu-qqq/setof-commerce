package com.ryuqq.setof.application.discount.port.out.query;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import java.util.List;
import java.util.Optional;

/**
 * 할인 정책 조회 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountPolicyQueryPort {

    /**
     * 특정 타겟에 적용 가능한 활성 할인 정책 목록 조회.
     *
     * @param targetType 대상 유형
     * @param targetId 대상 ID
     * @return 적용 가능한 할인 정책 목록
     */
    List<DiscountPolicy> findActiveByTarget(DiscountTargetType targetType, long targetId);

    /**
     * ID로 할인 정책 단건 조회.
     *
     * @param id 할인 정책 ID
     * @return 할인 정책 Optional
     */
    Optional<DiscountPolicy> findById(long id);

    /**
     * 검색 조건으로 할인 정책 목록 조회 (페이징).
     *
     * @param criteria 검색 조건
     * @return 할인 정책 목록
     */
    List<DiscountPolicy> findByCriteria(DiscountPolicySearchCriteria criteria);

    /**
     * 검색 조건으로 할인 정책 총 건수 조회.
     *
     * @param criteria 검색 조건
     * @return 총 건수
     */
    long countByCriteria(DiscountPolicySearchCriteria criteria);
}
