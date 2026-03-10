package com.ryuqq.setof.adapter.out.persistence.productnotice.condition;

import static com.ryuqq.setof.adapter.out.persistence.productnotice.entity.QProductNoticeJpaEntity.productNoticeJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeConditionBuilder - 상품고시 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductNoticeConditionBuilder {

    /** 상품그룹 ID 일치 조건 */
    public BooleanExpression productGroupIdEq(Long productGroupId) {
        return productGroupId != null
                ? productNoticeJpaEntity.productGroupId.eq(productGroupId)
                : null;
    }

    /** 상품그룹 ID 목록 포함 조건 */
    public BooleanExpression productGroupIdIn(List<Long> productGroupIds) {
        return productGroupIds != null && !productGroupIds.isEmpty()
                ? productNoticeJpaEntity.productGroupId.in(productGroupIds)
                : null;
    }
}
