package com.ryuqq.setof.domain.productimage.vo;

import com.ryuqq.setof.domain.productimage.exception.InvalidImageUrlException;

/**
 * 이미지 URL Value Object
 *
 * <p>상품 이미지의 URL을 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Self-validation - Compact Constructor에서 검증
 * </ul>
 *
 * @param value URL 문자열
 */
public record ImageUrl(String value) {

    private static final int MAX_URL_LENGTH = 2000;

    /** Compact Constructor - 검증 로직 */
    public ImageUrl {
        validate(value);
    }

    /**
     * Static Factory Method - URL 문자열로 생성
     *
     * @param value URL 문자열
     * @return ImageUrl 인스턴스
     * @throws InvalidImageUrlException URL이 유효하지 않은 경우
     */
    public static ImageUrl of(String value) {
        return new ImageUrl(value);
    }

    /**
     * CDN URL 여부 확인
     *
     * @return CDN 도메인을 포함하면 true
     */
    public boolean isCdnUrl() {
        return value != null && (value.contains("cdn.") || value.contains("cloudfront.net"));
    }

    /**
     * HTTP 프로토콜 여부 확인
     *
     * @return http:// 또는 https://로 시작하면 true
     */
    public boolean isHttpProtocol() {
        return value != null && (value.startsWith("http://") || value.startsWith("https://"));
    }

    /**
     * 파일 확장자 추출
     *
     * @return 확장자 문자열 (점 제외), 없으면 빈 문자열
     */
    public String extractExtension() {
        if (value == null) {
            return "";
        }
        int lastDotIndex = value.lastIndexOf('.');
        int lastSlashIndex = value.lastIndexOf('/');
        if (lastDotIndex > lastSlashIndex && lastDotIndex < value.length() - 1) {
            String extension = value.substring(lastDotIndex + 1);
            int queryIndex = extension.indexOf('?');
            return queryIndex > 0 ? extension.substring(0, queryIndex) : extension;
        }
        return "";
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidImageUrlException("이미지 URL은 null이거나 빈 값일 수 없습니다");
        }
        if (value.length() > MAX_URL_LENGTH) {
            throw new InvalidImageUrlException(
                    String.format("이미지 URL 길이가 %d자를 초과합니다: %d", MAX_URL_LENGTH, value.length()));
        }
        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            throw new InvalidImageUrlException("이미지 URL은 http:// 또는 https://로 시작해야 합니다: " + value);
        }
    }
}
