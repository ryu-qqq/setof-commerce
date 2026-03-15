package com.ryuqq.setof.application.discount.service.query;

import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyPageResult;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyQueryFactory;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.application.discount.port.in.query.SearchDiscountPoliciesUseCase;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * SearchDiscountPoliciesService - 할인 정책 목록 검색 Service.
 *
 * <p>QueryFactory로 Criteria 생성 → ReadManager로 목록 조회 및 카운트 → DiscountPolicyResult로 변환 →
 * DiscountPolicyPageResult 조립 후 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class SearchDiscountPoliciesService implements SearchDiscountPoliciesUseCase {

    private final DiscountPolicyReadManager readManager;
    private final DiscountPolicyQueryFactory queryFactory;

    public SearchDiscountPoliciesService(
            DiscountPolicyReadManager readManager, DiscountPolicyQueryFactory queryFactory) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public DiscountPolicyPageResult execute(DiscountPolicySearchParams params) {
        // 검색 파라미터를 도메인 Criteria로 변환
        DiscountPolicySearchCriteria criteria = queryFactory.create(params);

        // 목록 조회 및 전체 건수 조회
        List<DiscountPolicy> policies = readManager.findByCriteria(criteria);
        long totalCount = readManager.countByCriteria(criteria);

        // 도메인 객체를 결과 DTO로 변환
        List<DiscountPolicyResult> results =
                policies.stream().map(DiscountPolicyResult::from).toList();

        // 페이지 결과 DTO 조립
        return DiscountPolicyPageResult.of(results, totalCount, params.page(), params.size());
    }
}
