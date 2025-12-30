package com.ryuqq.setof.domain.cms.vo;

/**
 * Content 제목 Value Object
 *
 * <p>최대 200자까지 허용
 *
 * @param value 제목 문자열
 * @author development-team
 * @since 1.0.0
 */
public record ContentTitle(String value) {

    private static final int MAX_LENGTH = 200;

    /** Compact Constructor */
    public ContentTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "제목은 " + MAX_LENGTH + "자를 초과할 수 없습니다: " + value.length());
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param value 제목 문자열
     * @return ContentTitle 인스턴스
     */
    public static ContentTitle of(String value) {
        return new ContentTitle(value);
    }
}
