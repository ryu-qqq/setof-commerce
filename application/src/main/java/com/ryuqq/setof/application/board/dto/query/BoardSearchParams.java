package com.ryuqq.setof.application.board.dto.query;

import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;

/**
 * 공지사항 검색 파라미터.
 *
 * <p>APP-DTO-003: SearchParams CommonSearchParams 포함 필수.
 *
 * @param searchParams 공통 검색 파라미터 (정렬, 페이징 등)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BoardSearchParams(CommonSearchParams searchParams) {

    public static BoardSearchParams of(CommonSearchParams searchParams) {
        return new BoardSearchParams(searchParams);
    }

    public int page() {
        return searchParams.page();
    }

    public int size() {
        return searchParams.size();
    }

    public String sortKey() {
        return searchParams.sortKey();
    }

    public String sortDirection() {
        return searchParams.sortDirection();
    }
}
