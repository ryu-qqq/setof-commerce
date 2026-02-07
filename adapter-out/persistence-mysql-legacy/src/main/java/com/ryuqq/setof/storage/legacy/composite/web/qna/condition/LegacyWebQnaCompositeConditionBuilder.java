package com.ryuqq.setof.storage.legacy.composite.web.qna.condition;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaProductEntity.legacyQnaProductEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebQnaCompositeConditionBuilder - 레거시 Web Q&A Composite QueryDSL 조건 빌더.
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

    // ===== ID 조건 =====

    /**
     * Q&A ID 일치 조건.
     *
     * @param qnaId Q&A ID
     * @return BooleanExpression
     */
    public BooleanExpression qnaIdEq(Long qnaId) {
        return qnaId != null ? legacyQnaEntity.id.eq(qnaId) : null;
    }

    /**
     * Q&A ID 목록 포함 조건.
     *
     * @param qnaIds Q&A ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression qnaIdIn(List<Long> qnaIds) {
        return qnaIds != null && !qnaIds.isEmpty() ? legacyQnaEntity.id.in(qnaIds) : null;
    }

    /**
     * Q&A ID 미만 조건 (커서 기반 페이징).
     *
     * @param lastDomainId 마지막 도메인 ID
     * @return BooleanExpression
     */
    public BooleanExpression qnaIdLt(Long lastDomainId) {
        return lastDomainId != null ? legacyQnaEntity.id.lt(lastDomainId) : null;
    }

    // ===== 상품그룹 조건 =====

    /**
     * 상품그룹 ID 일치 조건 (QnaProduct 조인).
     *
     * @param productGroupId 상품그룹 ID
     * @return BooleanExpression
     */
    public BooleanExpression productGroupIdEq(Long productGroupId) {
        return productGroupId != null
                ? legacyQnaProductEntity.productGroupId.eq(productGroupId)
                : null;
    }

    // ===== 사용자 조건 =====

    /**
     * 사용자 ID 일치 조건.
     *
     * @param userId 사용자 ID
     * @return BooleanExpression
     */
    public BooleanExpression userIdEq(Long userId) {
        return userId != null ? legacyQnaEntity.userId.eq(userId) : null;
    }

    // ===== Q&A 유형 조건 =====

    /**
     * Q&A 유형 일치 조건.
     *
     * @param qnaType Q&A 유형 (PRODUCT/ORDER)
     * @return BooleanExpression
     */
    public BooleanExpression qnaTypeEq(String qnaType) {
        return qnaType != null && !qnaType.isBlank() ? legacyQnaEntity.qnaType.eq(qnaType) : null;
    }

    // ===== 기간 조건 =====

    /**
     * 등록일 기간 조건.
     *
     * @param startDate 시작일
     * @param endDate 종료일
     * @return BooleanExpression
     */
    public BooleanExpression betweenTime(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }
        BooleanExpression condition = null;
        if (startDate != null) {
            condition = legacyQnaEntity.insertDate.goe(startDate);
        }
        if (endDate != null) {
            BooleanExpression endCondition = legacyQnaEntity.insertDate.loe(endDate);
            condition = condition != null ? condition.and(endCondition) : endCondition;
        }
        return condition;
    }
}
