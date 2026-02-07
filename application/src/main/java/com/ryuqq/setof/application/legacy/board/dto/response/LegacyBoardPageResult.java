package com.ryuqq.setof.application.legacy.board.dto.response;

import java.util.List;

/**
 * LegacyBoardPageResult - 레거시 게시판 페이지 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param content 게시글 목록
 * @param totalElements 전체 개수
 * @param totalPages 전체 페이지 수
 * @param pageNumber 현재 페이지 번호
 * @param pageSize 페이지 크기
 * @param first 첫 페이지 여부
 * @param last 마지막 페이지 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBoardPageResult(
        List<LegacyBoardResult> content,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        boolean first,
        boolean last) {

    /**
     * 팩토리 메서드.
     *
     * @param content 게시글 목록
     * @param totalElements 전체 개수
     * @param pageNumber 현재 페이지 번호
     * @param pageSize 페이지 크기
     * @return LegacyBoardPageResult
     */
    public static LegacyBoardPageResult of(
            List<LegacyBoardResult> content, long totalElements, int pageNumber, int pageSize) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean first = pageNumber == 0;
        boolean last = pageNumber >= totalPages - 1;

        return new LegacyBoardPageResult(
                content, totalElements, totalPages, pageNumber, pageSize, first, last);
    }

    /**
     * 빈 결과 생성.
     *
     * @param pageNumber 현재 페이지 번호
     * @param pageSize 페이지 크기
     * @return LegacyBoardPageResult
     */
    public static LegacyBoardPageResult empty(int pageNumber, int pageSize) {
        return new LegacyBoardPageResult(List.of(), 0, 0, pageNumber, pageSize, true, true);
    }
}
