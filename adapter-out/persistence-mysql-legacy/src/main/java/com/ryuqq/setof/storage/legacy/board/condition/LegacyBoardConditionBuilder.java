package com.ryuqq.setof.storage.legacy.board.condition;

import static com.ryuqq.setof.storage.legacy.board.entity.QLegacyBoardEntity.legacyBoardEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

/**
 * LegacyBoardConditionBuilder - 레거시 게시판 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * <p>Board는 단순 페이징 조회만 지원하므로 ID 조건만 제공합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyBoardConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? legacyBoardEntity.id.eq(id) : null;
    }
}
