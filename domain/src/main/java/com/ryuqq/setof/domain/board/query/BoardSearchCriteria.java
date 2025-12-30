package com.ryuqq.setof.domain.board.query;

import com.ryuqq.setof.domain.board.vo.BoardStatus;
import com.ryuqq.setof.domain.board.vo.BoardType;
import java.time.Instant;

/**
 * Board 검색 조건
 *
 * <p>Board 목록 조회 시 사용되는 검색 조건을 담는 Record입니다.
 *
 * @param boardType 게시판 타입 (nullable)
 * @param status 상태 (nullable)
 * @param displayableAt 현재 노출 가능 여부 확인용 시간 (nullable)
 * @param pinned 상단 고정 여부 (nullable)
 * @param offset 조회 시작 위치
 * @param limit 조회 개수
 */
public record BoardSearchCriteria(
        BoardType boardType,
        BoardStatus status,
        Instant displayableAt,
        Boolean pinned,
        long offset,
        int limit) {

    /** 기본 페이지 크기 */
    private static final int DEFAULT_LIMIT = 20;

    /** 최대 페이지 크기 */
    private static final int MAX_LIMIT = 100;

    /**
     * 검색 조건 생성자
     *
     * @param boardType 게시판 타입
     * @param status 상태
     * @param displayableAt 노출 가능 시간
     * @param pinned 상단 고정 여부
     * @param offset 조회 시작 위치
     * @param limit 조회 개수
     */
    public BoardSearchCriteria {
        if (offset < 0) {
            offset = 0;
        }
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        if (limit > MAX_LIMIT) {
            limit = MAX_LIMIT;
        }
    }

    /**
     * Admin용 검색 조건 생성
     *
     * @param boardType 게시판 타입
     * @param status 상태
     * @param offset 조회 시작 위치
     * @param limit 조회 개수
     * @return BoardSearchCriteria
     */
    public static BoardSearchCriteria forAdmin(
            BoardType boardType, BoardStatus status, long offset, int limit) {
        return new BoardSearchCriteria(boardType, status, null, null, offset, limit);
    }

    /**
     * Client용 검색 조건 생성 (Published + 현재 노출 가능)
     *
     * @param boardType 게시판 타입
     * @param now 현재 시간
     * @param offset 조회 시작 위치
     * @param limit 조회 개수
     * @return BoardSearchCriteria
     */
    public static BoardSearchCriteria forClient(
            BoardType boardType, Instant now, long offset, int limit) {
        return new BoardSearchCriteria(boardType, BoardStatus.PUBLISHED, now, null, offset, limit);
    }

    /**
     * 기본 검색 조건 생성
     *
     * @return BoardSearchCriteria
     */
    public static BoardSearchCriteria defaultCriteria() {
        return new BoardSearchCriteria(null, null, null, null, 0, DEFAULT_LIMIT);
    }
}
