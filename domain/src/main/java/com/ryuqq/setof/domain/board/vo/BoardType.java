package com.ryuqq.setof.domain.board.vo;

/**
 * 게시판 타입 Enum
 *
 * <p>게시물의 종류를 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>Spring 의존성 금지
 * </ul>
 */
public enum BoardType {

    /** 공지사항 */
    NOTICE("공지사항"),

    /** 이벤트 */
    EVENT("이벤트"),

    /** 뉴스 */
    NEWS("뉴스");

    private final String displayName;

    BoardType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 표시용 이름 반환
     *
     * @return 한글 표시 이름
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 문자열로부터 BoardType 변환
     *
     * @param value 타입 문자열
     * @return BoardType
     * @throws IllegalArgumentException 유효하지 않은 값일 때
     */
    public static BoardType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("BoardType value cannot be null or blank");
        }
        try {
            return BoardType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid BoardType: " + value);
        }
    }
}
