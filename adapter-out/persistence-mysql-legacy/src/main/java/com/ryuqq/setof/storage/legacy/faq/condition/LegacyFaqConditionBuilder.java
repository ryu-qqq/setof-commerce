package com.ryuqq.setof.storage.legacy.faq.condition;

import static com.ryuqq.setof.storage.legacy.faq.entity.QLegacyFaqEntity.legacyFaqEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import org.springframework.stereotype.Component;

/**
 * LegacyFaqConditionBuilder - 레거시 FAQ QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * <p>신규 FaqType → 레거시 FaqType 변환 후 조건을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyFaqConditionBuilder {

    /**
     * FAQ 유형 일치 조건.
     *
     * <p>신규 FaqType을 레거시 FaqType으로 변환하여 조건을 생성합니다.
     *
     * @param faqType 신규 FaqType
     * @return BooleanExpression
     */
    public BooleanExpression faqTypeEq(FaqType faqType) {
        if (faqType == null) {
            return null;
        }
        return legacyFaqEntity.faqType.eq(faqType);
    }
}
