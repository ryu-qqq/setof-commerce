package com.ryuqq.setof.domain.legacy.board.dto.query;

/**
 * LegacyBoardSearchCondition - 레거시 게시판 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param pageNumber 페이지 번호 (0부터 시작)
 * @param pageSize 페이지 크기
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBoardSearchCondition(int pageNumber, int pageSize) {

    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 페이지 정보로 조회하는 생성자.
     *
     * @param pageNumber 페이지 번호
     * @param pageSize 페이지 크기
     * @return LegacyBoardSearchCondition
     */
    public static LegacyBoardSearchCondition of(int pageNumber, int pageSize) {
        return new LegacyBoardSearchCondition(pageNumber, pageSize);
    }

    /**
     * 기본 페이지 크기로 조회.
     *
     * @param pageNumber 페이지 번호
     * @return LegacyBoardSearchCondition
     */
    public static LegacyBoardSearchCondition ofPage(int pageNumber) {
        return new LegacyBoardSearchCondition(pageNumber, DEFAULT_PAGE_SIZE);
    }

    /**
     * 첫 페이지 조회용.
     *
     * @return LegacyBoardSearchCondition
     */
    public static LegacyBoardSearchCondition firstPage() {
        return new LegacyBoardSearchCondition(0, DEFAULT_PAGE_SIZE);
    }

    /**
     * 오프셋 계산.
     *
     * @return 오프셋
     */
    public long offset() {
        return (long) pageNumber * pageSize;
    }
}
