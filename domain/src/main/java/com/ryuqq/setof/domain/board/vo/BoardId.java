package com.ryuqq.setof.domain.board.vo;

/**
 * Board 식별자 Value Object
 *
 * <p>게시물의 고유 식별자를 나타내는 불변 Value Object입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - record 사용
 *   <li>검증 로직 포함
 * </ul>
 */
public record BoardId(Long value) {

    private static final Long NEW_ID_VALUE = null;

    /**
     * 컴팩트 생성자 - 검증 로직
     *
     * @throws IllegalArgumentException value가 null이 아니면서 0 이하일 때
     */
    public BoardId {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("BoardId must be positive: " + value);
        }
    }

    /**
     * 신규 생성용 Static Factory Method
     *
     * @return ID가 null인 BoardId (아직 영속화되지 않음)
     */
    public static BoardId forNew() {
        return new BoardId(NEW_ID_VALUE);
    }

    /**
     * 기존 ID 복원용 Static Factory Method
     *
     * @param value ID 값
     * @return BoardId 인스턴스
     */
    public static BoardId of(Long value) {
        return new BoardId(value);
    }

    /**
     * 신규 ID 여부 확인
     *
     * @return ID가 없으면 true
     */
    public boolean isNew() {
        return value == null;
    }
}
