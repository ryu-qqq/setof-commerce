package com.ryuqq.setof.domain.productdescription.vo;

import com.ryuqq.setof.domain.productdescription.exception.InvalidHtmlContentException;

/**
 * HTML 컨텐츠 Value Object
 *
 * <p>상품 상세설명의 HTML 컨텐츠를 나타냅니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Self-validation - Compact Constructor에서 검증
 * </ul>
 *
 * @param value HTML 문자열
 */
public record HtmlContent(String value) {

    private static final int MAX_CONTENT_LENGTH = 100_000;

    /** Compact Constructor - 검증 로직 */
    public HtmlContent {
        validate(value);
    }

    /**
     * Static Factory Method - HTML 문자열로 생성
     *
     * @param value HTML 문자열
     * @return HtmlContent 인스턴스
     * @throws InvalidHtmlContentException HTML이 유효하지 않은 경우
     */
    public static HtmlContent of(String value) {
        return new HtmlContent(value);
    }

    /**
     * Static Factory Method - 빈 HTML 컨텐츠 생성
     *
     * @return 빈 HTML 컨텐츠
     */
    public static HtmlContent empty() {
        return new HtmlContent("");
    }

    /**
     * 컨텐츠 존재 여부 확인
     *
     * @return 컨텐츠가 비어있지 않으면 true
     */
    public boolean hasContent() {
        return value != null && !value.isBlank();
    }

    /**
     * 컨텐츠 길이 반환
     *
     * @return HTML 문자열 길이
     */
    public int length() {
        return value != null ? value.length() : 0;
    }

    /**
     * 이미지 태그 포함 여부 확인
     *
     * @return img 태그가 포함되어 있으면 true
     */
    public boolean containsImages() {
        return value != null && value.toLowerCase().contains("<img");
    }

    /**
     * URL 치환하여 새 HtmlContent 반환
     *
     * @param originUrl 원본 URL
     * @param cdnUrl CDN URL
     * @return URL이 치환된 새 HtmlContent 인스턴스
     */
    public HtmlContent replaceUrl(String originUrl, String cdnUrl) {
        if (value == null || originUrl == null || cdnUrl == null) {
            return this;
        }
        return new HtmlContent(value.replace(originUrl, cdnUrl));
    }

    private static void validate(String value) {
        if (value == null) {
            throw new InvalidHtmlContentException("HTML 컨텐츠는 null일 수 없습니다");
        }
        if (value.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidHtmlContentException(
                    String.format(
                            "HTML 컨텐츠 길이가 %d자를 초과합니다: %d", MAX_CONTENT_LENGTH, value.length()));
        }
    }
}
