package com.ryuqq.setof.storage.legacy.composite.web.qna.condition;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaProductEntity.legacyQnaProductEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaEntity;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaProductConditionBuilder - 상품 Q&A QueryDSL 조건 빌더.
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
public class LegacyWebQnaProductConditionBuilder {

    public BooleanExpression productGroupIdEq(Long productGroupId) {
        return productGroupId != null
                ? legacyQnaProductEntity.productGroupId.eq(productGroupId)
                : null;
    }

    public BooleanExpression qnaIdIn(List<Long> qnaIds) {
        if (qnaIds == null || qnaIds.isEmpty()) {
            return null;
        }
        return legacyQnaEntity.id.in(qnaIds);
    }

    public BooleanExpression notDeleted() {
        return legacyQnaEntity.deleteYn.eq(LegacyQnaEntity.Yn.N);
    }
}
