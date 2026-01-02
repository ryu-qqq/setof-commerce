package com.ryuqq.setof.domain.cms.vo;

/**
 * Content 메모 Value Object
 *
 * <p>최대 500자까지 허용, nullable
 *
 * @param value 메모 문자열 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record ContentMemo(String value) {

    private static final int MAX_LENGTH = 500;

    /** Compact Constructor */
    public ContentMemo {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "메모는 " + MAX_LENGTH + "자를 초과할 수 없습니다: " + value.length());
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param value 메모 문자열 (nullable)
     * @return ContentMemo 인스턴스
     */
    public static ContentMemo of(String value) {
        return new ContentMemo(value);
    }

    /**
     * 빈 메모 생성
     *
     * @return null 값을 가진 ContentMemo
     */
    public static ContentMemo empty() {
        return new ContentMemo(null);
    }

    /**
     * 메모 존재 여부 확인
     *
     * @return 메모가 존재하면 true
     */
    public boolean hasValue() {
        return value != null && !value.isBlank();
    }
}
