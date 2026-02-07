package com.ryuqq.setof.storage.legacy.composite.web.faq.condition;

import static com.ryuqq.setof.storage.legacy.faq.entity.QLegacyFaqEntity.legacyFaqEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.legacy.faq.FaqType;
import org.springframework.stereotype.Component;

/**
 * LegacyWebFaqCompositeConditionBuilder - 레거시 Web FAQ Composite QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebFaqCompositeConditionBuilder {

    /**
     * FAQ 유형 일치 조건.
     *
     * <p>TOP 유형이 아닌 경우에만 적용.
     *
     * @param faqType FAQ 유형
     * @return BooleanExpression
     */
    public BooleanExpression faqTypeEq(FaqType faqType) {
        if (faqType == null || faqType == FaqType.TOP) {
            return null;
        }
        return legacyFaqEntity.faqType.eq(faqType);
    }

    /**
     * TOP FAQ 조건 (topDisplayOrder가 존재하는 항목).
     *
     * @param faqType FAQ 유형
     * @return BooleanExpression
     */
    public BooleanExpression hasTopDisplayOrder(FaqType faqType) {
        if (faqType != FaqType.TOP) {
            return null;
        }
        return legacyFaqEntity.topDisplayOrder.isNotNull();
    }
}
