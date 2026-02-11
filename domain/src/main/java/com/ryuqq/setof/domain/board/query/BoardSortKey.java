package com.ryuqq.setof.domain.board.query;

import com.ryuqq.setof.domain.common.vo.SortKey;

/**
 * BoardSortKey - 공지사항 정렬 키.
 *
 * <p>공지사항 목록 조회 시 사용 가능한 정렬 필드를 정의합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum BoardSortKey implements SortKey {

    /** 등록일시 순 (기본값) */
    CREATED_AT("createdAt"),

    /** 게시판 ID 순 */
    BOARD_ID("boardId");

    private final String fieldName;

    BoardSortKey(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    /** 기본 정렬 키 (등록일시) */
    public static BoardSortKey defaultKey() {
        return CREATED_AT;
    }
}
