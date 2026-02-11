package com.ryuqq.setof.domain.board.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;

/**
 * BoardSearchCriteria - 공지사항 검색 조건 Criteria.
 *
 * <p>DOM-CRI-001: Record로 정의 + of() 팩토리 메서드.
 *
 * <p>공지사항 목록 조회 시 사용하는 페이징 정보를 정의합니다.
 *
 * @param queryContext 정렬 및 페이징 정보
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BoardSearchCriteria(QueryContext<BoardSortKey> queryContext) {

    /** Compact Constructor - null 방어 */
    public BoardSearchCriteria {
        if (queryContext == null) {
            queryContext = QueryContext.defaultOf(BoardSortKey.defaultKey());
        }
    }

    /**
     * 검색 조건 생성.
     *
     * @param queryContext 정렬 및 페이징 정보
     * @return BoardSearchCriteria
     */
    public static BoardSearchCriteria of(QueryContext<BoardSortKey> queryContext) {
        return new BoardSearchCriteria(queryContext);
    }

    /**
     * 기본 검색 조건 생성 (첫 페이지, 기본 사이즈, 등록일시 내림차순).
     *
     * @return BoardSearchCriteria
     */
    public static BoardSearchCriteria defaultOf() {
        return new BoardSearchCriteria(QueryContext.defaultOf(BoardSortKey.defaultKey()));
    }

    /**
     * 페이지/사이즈 지정 검색 조건 생성.
     *
     * @param page 페이지 번호 (0-based)
     * @param size 페이지 크기
     * @return BoardSearchCriteria
     */
    public static BoardSearchCriteria ofPage(int page, int size) {
        return new BoardSearchCriteria(
                QueryContext.firstPage(BoardSortKey.defaultKey(), null, size));
    }

    /** 페이지 크기 반환 (편의 메서드). */
    public int size() {
        return queryContext.size();
    }

    /** 오프셋 반환 (편의 메서드). */
    public long offset() {
        return queryContext.offset();
    }

    /** 현재 페이지 번호 반환 (편의 메서드). */
    public int page() {
        return queryContext.page();
    }
}
