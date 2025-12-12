package com.ryuqq.setof.domain.seller.vo;

import com.ryuqq.setof.domain.seller.exception.InvalidLogoUrlException;
import java.util.regex.Pattern;

/**
 * 로고 URL Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>nullable 허용 - 선택적 필드
 *   <li>URL 형식 검증
 * </ul>
 *
 * @param value 로고 URL 값 (null 허용)
 */
public record LogoUrl(String value) {

    private static final int MAX_LENGTH = 500;
    private static final Pattern URL_PATTERN =
            Pattern.compile(
                    "^(https?://)[a-zA-Z0-9.-]+(:\\d+)?(/[a-zA-Z0-9._~:/?#\\[\\]@!$&'()*+,;%=-]*)?$");

    /** Compact Constructor - 검증 로직 */
    public LogoUrl {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 로고 URL 값 (null 허용)
     * @return LogoUrl 인스턴스
     * @throws InvalidLogoUrlException value가 빈 문자열이거나 잘못된 형식인 경우
     */
    public static LogoUrl of(String value) {
        return new LogoUrl(value);
    }

    /**
     * 값 존재 여부 확인
     *
     * @return 값이 있으면 true, null이면 false
     */
    public boolean hasValue() {
        return value != null;
    }

    private static void validate(String value) {
        if (value == null) {
            return;
        }

        if (value.isBlank()) {
            throw new InvalidLogoUrlException(value, "로고 URL은 빈 문자열일 수 없습니다.");
        }

        if (value.length() > MAX_LENGTH) {
            throw new InvalidLogoUrlException(value, "로고 URL은 500자를 초과할 수 없습니다.");
        }

        if (!URL_PATTERN.matcher(value).matches()) {
            throw new InvalidLogoUrlException(value, "올바른 URL 형식이어야 합니다.");
        }
    }
}
