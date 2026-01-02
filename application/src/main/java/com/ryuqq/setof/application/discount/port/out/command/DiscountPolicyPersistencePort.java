package com.ryuqq.setof.application.discount.port.out.command;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;

/**
 * 할인 정책 Persistence Port (Port-Out)
 *
 * <p>할인 정책 Aggregate의 영속성을 담당하는 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DiscountPolicyPersistencePort {

    /**
     * 할인 정책 저장 (신규 생성 또는 수정)
     *
     * <p>Domain의 ID 유무로 신규/수정 구분하여 처리
     *
     * @param discountPolicy 저장할 할인 정책 도메인
     * @return 저장된 할인 정책 ID
     */
    DiscountPolicyId persist(DiscountPolicy discountPolicy);
}
