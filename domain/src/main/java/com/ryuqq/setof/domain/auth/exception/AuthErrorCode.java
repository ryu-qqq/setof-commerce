package com.ryuqq.setof.domain.auth.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Auth Error Code - 인증 도메인 에러 코드
 *
 * <p>Auth Bounded Context의 모든 에러 코드를 정의합니다.
 *
 * <p><strong>코드 규칙:</strong>
 *
 * <ul>
 *   <li>형식: AUTH-{NUMBER}
 *   <li>001-099: 토큰 관련 오류
 *   <li>100-199: 인증 관련 오류
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public enum AuthErrorCode implements ErrorCode {

    // ===== Token 관련 오류 (001-099) =====

    /** 유효하지 않은 Refresh Token */
    INVALID_REFRESH_TOKEN("AUTH-001", 401, "유효하지 않은 Refresh Token입니다."),

    /** 만료된 Refresh Token */
    EXPIRED_REFRESH_TOKEN("AUTH-002", 401, "만료된 Refresh Token입니다."),

    /** 유효하지 않은 Access Token */
    INVALID_ACCESS_TOKEN("AUTH-003", 401, "유효하지 않은 Access Token입니다."),

    /** 만료된 Access Token */
    EXPIRED_ACCESS_TOKEN("AUTH-004", 401, "만료된 Access Token입니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    AuthErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
