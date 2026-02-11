package com.ryuqq.setof.domain.board.id;

/**
 * BoardId - 공지사항 식별자 Value Object.
 *
 * <p>DOM-ID-001: {Domain}Id Record로 정의.
 *
 * <p>DOM-ID-002: Long ID는 forNew()로 null 생성 (DB auto-increment 대기).
 *
 * @param value 공지사항 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BoardId(Long value) {

    public static BoardId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("BoardId 값은 null일 수 없습니다");
        }
        return new BoardId(value);
    }

    public static BoardId forNew() {
        return new BoardId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
