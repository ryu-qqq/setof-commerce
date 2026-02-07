package com.ryuqq.setof.storage.legacy.composite.web.board.condition;

import org.springframework.stereotype.Component;

/**
 * LegacyWebBoardCompositeConditionBuilder - 레거시 게시판 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>현재 Board 조회에는 특별한 조건이 없으나, 확장성을 위해 빌더 클래스 유지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebBoardCompositeConditionBuilder {

    // Board 조회는 조건 없이 전체 조회 + 페이징만 사용
    // 향후 검색어, 카테고리 등 조건 추가 시 여기에 메서드 추가
}
