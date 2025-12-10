package com.ryuqq.setof.domain.auth.vo;

/**
 * Token Value - Refresh Token 값 VO
 *
 * <p>JWT 형식의 Refresh Token 값을 캡슐화합니다.
 *
 * <p><strong>불변 객체:</strong>
 *
 * <ul>
 *   <li>생성 후 값 변경 불가
 *   <li>검증은 생성 시점에 수행
 * </ul>
 *
 * @param value 토큰 값 (JWT 형식)
 * @author development-team
 * @since 1.0.0
 */
public record TokenValue(String value) {

    private static final int MAX_TOKEN_LENGTH = 512;

    public TokenValue {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("token value must not be blank");
        }
        if (value.length() > MAX_TOKEN_LENGTH) {
            throw new IllegalArgumentException(
                    "token value must not exceed " + MAX_TOKEN_LENGTH + " characters");
        }
    }

    public static TokenValue of(String value) {
        return new TokenValue(value);
    }
}
