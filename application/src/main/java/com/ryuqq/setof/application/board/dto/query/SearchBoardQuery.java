package com.ryuqq.setof.application.board.dto.query;

import java.time.Instant;

/**
 * Board 검색 Query
 *
 * @param boardType 게시판 타입 (null이면 전체)
 * @param status 상태 (null이면 전체)
 * @param displayableAt 표시 가능 시점 (Client용, null이면 필터 없음)
 * @param pinned 상단 고정 여부 (null이면 전체)
 * @param offset 페이징 시작 위치
 * @param limit 페이징 크기
 * @author development-team
 * @since 1.0.0
 */
public record SearchBoardQuery(
        String boardType,
        String status,
        Instant displayableAt,
        Boolean pinned,
        long offset,
        int limit) {

    /**
     * Admin용 검색 쿼리 생성
     *
     * @param boardType 게시판 타입
     * @param status 상태
     * @param pinned 상단 고정 여부
     * @param offset 페이징 시작 위치
     * @param limit 페이징 크기
     * @return 검색 쿼리
     */
    public static SearchBoardQuery forAdmin(
            String boardType, String status, Boolean pinned, long offset, int limit) {
        return new SearchBoardQuery(boardType, status, null, pinned, offset, limit);
    }

    /**
     * Client용 검색 쿼리 생성 (PUBLISHED 상태 + 표시 기간 내)
     *
     * @param boardType 게시판 타입
     * @param now 현재 시간
     * @param pinned 상단 고정 여부
     * @param offset 페이징 시작 위치
     * @param limit 페이징 크기
     * @return 검색 쿼리
     */
    public static SearchBoardQuery forClient(
            String boardType, Instant now, Boolean pinned, long offset, int limit) {
        return new SearchBoardQuery(boardType, "PUBLISHED", now, pinned, offset, limit);
    }
}
