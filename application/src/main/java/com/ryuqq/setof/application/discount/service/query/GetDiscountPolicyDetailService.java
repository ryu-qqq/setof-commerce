package com.ryuqq.setof.application.discount.service.query;

import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyDetailUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import org.springframework.stereotype.Service;

/**
 * GetDiscountPolicyDetailService - 할인 정책 단건 조회 Service.
 *
 * <p>ReadManager로 정책을 조회한 후 결과 DTO로 변환하여 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetDiscountPolicyDetailService implements GetDiscountPolicyDetailUseCase {

    private final DiscountPolicyReadManager readManager;

    public GetDiscountPolicyDetailService(DiscountPolicyReadManager readManager) {
        this.readManager = readManager;
    }

    @Override
    public DiscountPolicyResult execute(long discountPolicyId) {
        // ID로 할인 정책 조회 (없으면 예외 발생)
        DiscountPolicy policy = readManager.getById(discountPolicyId);

        // 도메인 객체를 결과 DTO로 변환
        return DiscountPolicyResult.from(policy);
    }
}
