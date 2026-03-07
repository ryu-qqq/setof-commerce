package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/** 회원 도메인 에러 코드. */
public enum MemberErrorCode implements ErrorCode {

    // 회원 관련 (MBR-001 ~ MBR-049)
    MEMBER_NOT_FOUND("MBR-001", 404, "회원을 찾을 수 없습니다"),
    MEMBER_NOT_ACTIVE("MBR-002", 403, "로그인할 수 없는 회원 상태입니다"),
    MEMBER_ALREADY_REGISTERED("MBR-003", 409, "이미 가입된 회원입니다"),

    // 인증 관련 (MBR-050 ~ MBR-099)
    MEMBER_AUTH_NOT_FOUND("MBR-050", 404, "인증 정보를 찾을 수 없습니다"),
    INVALID_CREDENTIALS("MBR-051", 401, "아이디 또는 비밀번호가 일치하지 않습니다"),
    UNSUPPORTED_AUTH_PROVIDER("MBR-052", 400, "지원하지 않는 인증 제공자입니다"),
    SOCIAL_LOGIN_ALREADY_EXISTS("MBR-053", 409, "소셜 로그인으로 이미 가입되어 있습니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    MemberErrorCode(String code, int httpStatus, String message) {
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
