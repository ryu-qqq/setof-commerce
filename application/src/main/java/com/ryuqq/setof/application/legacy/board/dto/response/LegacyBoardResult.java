package com.ryuqq.setof.application.legacy.board.dto.response;

/**
 * LegacyBoardResult - 레거시 게시판 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param title 제목
 * @param contents 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBoardResult(String title, String contents) {

    /**
     * 팩토리 메서드.
     *
     * @param title 제목
     * @param contents 내용
     * @return LegacyBoardResult
     */
    public static LegacyBoardResult of(String title, String contents) {
        return new LegacyBoardResult(title, contents);
    }
}
