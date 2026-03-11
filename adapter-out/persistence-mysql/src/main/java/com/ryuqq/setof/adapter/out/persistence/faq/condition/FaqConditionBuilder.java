package com.ryuqq.setof.adapter.out.persistence.faq.condition;

import static com.ryuqq.setof.adapter.out.persistence.faq.entity.QFaqJpaEntity.faqJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import org.springframework.stereotype.Component;

/**
 * FaqConditionBuilder - FAQ QueryDSL 조건 빌더.
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
public class FaqConditionBuilder {

    /**
     * FAQ 유형 일치 조건.
     *
     * @param faqType FAQ 유형
     * @return BooleanExpression
     */
    public BooleanExpression faqTypeEq(FaqType faqType) {
        if (faqType == null) {
            return null;
        }
        return faqJpaEntity.faqType.eq(faqType.name());
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return faqJpaEntity.deletedAt.isNull();
    }
}
