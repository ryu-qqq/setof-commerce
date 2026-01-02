package com.ryuqq.setof.domain.cms.vo;

/**
 * 이미지 URL Value Object
 *
 * <p>최대 500자까지 허용, nullable
 *
 * @param value URL 문자열 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record ImageUrl(String value) {

    private static final int MAX_LENGTH = 500;

    /** Compact Constructor */
    public ImageUrl {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("이미지 URL은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param value URL 문자열
     * @return ImageUrl 인스턴스
     */
    public static ImageUrl of(String value) {
        return new ImageUrl(value);
    }

    /**
     * 빈 URL 생성
     *
     * @return null 값을 가진 ImageUrl
     */
    public static ImageUrl empty() {
        return new ImageUrl(null);
    }

    /**
     * URL 존재 여부 확인
     *
     * @return URL이 존재하면 true
     */
    public boolean hasValue() {
        return value != null && !value.isBlank();
    }
}
