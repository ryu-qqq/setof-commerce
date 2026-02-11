package com.ryuqq.setof.domain.board.vo;

/**
 * BoardTitle - 공지사항 제목 Value Object.
 *
 * <p>DOM-VO-001: Record + of() + Compact Constructor.
 *
 * @param value 공지사항 제목
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BoardTitle(String value) {

    public BoardTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("공지사항 제목은 필수입니다");
        }
    }

    public static BoardTitle of(String value) {
        return new BoardTitle(value);
    }
}
