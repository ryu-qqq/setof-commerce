package com.ryuqq.setof.domain.board.vo;

/**
 * BoardContents - 공지사항 내용 Value Object.
 *
 * <p>DOM-VO-001: Record + of() + Compact Constructor.
 *
 * @param value 공지사항 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BoardContents(String value) {

    public BoardContents {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("공지사항 내용은 필수입니다");
        }
    }

    public static BoardContents of(String value) {
        return new BoardContents(value);
    }
}
