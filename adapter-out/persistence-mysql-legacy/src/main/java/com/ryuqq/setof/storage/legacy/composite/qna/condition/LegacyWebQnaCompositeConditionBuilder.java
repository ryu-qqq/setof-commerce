package com.ryuqq.setof.storage.legacy.composite.qna.condition;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaCompositeConditionBuilder - 내 Q&A 복합 조회 QueryDSL 조건 빌더.
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
public class LegacyWebQnaCompositeConditionBuilder {

    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyQnaEntity.userId.eq(userId) : null;
    }

    public BooleanExpression qnaTypeEq(String qnaType) {
        return qnaType != null ? legacyQnaEntity.qnaType.eq(qnaType) : null;
    }

    public BooleanExpression cursorLessThan(Long lastDomainId) {
        return lastDomainId != null ? legacyQnaEntity.id.lt(lastDomainId) : null;
    }

    public BooleanExpression qnaIdIn(List<Long> qnaIds) {
        if (qnaIds == null || qnaIds.isEmpty()) {
            return null;
        }
        return legacyQnaEntity.id.in(qnaIds);
    }

    public BooleanExpression insertDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }
        if (startDate != null && endDate != null) {
            return legacyQnaEntity.insertDate.between(startDate, endDate);
        }
        if (startDate != null) {
            return legacyQnaEntity.insertDate.goe(startDate);
        }
        return legacyQnaEntity.insertDate.loe(endDate);
    }

    public BooleanExpression notDeleted() {
        return legacyQnaEntity.deleteYn.eq(LegacyQnaEntity.Yn.N);
    }
}
