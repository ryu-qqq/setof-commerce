package com.ryuqq.setof.domain.member.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Member Bounded Context 에러 코드
 *
 * <p>회원 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: MBR-XXX
 *
 * <ul>
 *   <li>MBR-001 ~ MBR-099: 회원 조회/인증 관련
 *   <li>MBR-100 ~ MBR-199: 회원 정보 유효성 관련
 *   <li>MBR-200 ~ MBR-299: 비밀번호 관련
 *   <li>MBR-300 ~ MBR-399: 소셜 로그인 관련
 *   <li>MBR-400 ~ MBR-499: 회원 상태 관련
 * </ul>
 */
public enum MemberErrorCode implements ErrorCode {

    // === 회원 조회/인증 관련 (MBR-001 ~ MBR-099) ===
    MEMBER_NOT_FOUND("MBR-001", 404, "회원을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN("MBR-002", 401, "유효하지 않은 Refresh Token입니다."),

    // === 회원 정보 유효성 관련 (MBR-100 ~ MBR-199) ===
    INVALID_MEMBER_ID("MBR-100", 400, "회원 ID는 null이 아닌 유효한 UUID여야 합니다."),
    INVALID_EMAIL("MBR-101", 400, "이메일 형식이 올바르지 않습니다."),
    INVALID_PHONE_NUMBER("MBR-102", 400, "핸드폰 번호는 010으로 시작하는 11자리 숫자여야 합니다."),
    DUPLICATE_PHONE_NUMBER("MBR-103", 409, "이미 등록된 핸드폰 번호입니다."),
    INVALID_MEMBER_NAME("MBR-104", 400, "회원 이름이 올바르지 않습니다."),
    INVALID_WITHDRAWAL_INFO("MBR-105", 400, "탈퇴 정보가 올바르지 않습니다."),
    REQUIRED_CONSENT_MISSING("MBR-106", 400, "필수 동의 항목이 누락되었습니다."),

    // === 비밀번호 관련 (MBR-200 ~ MBR-299) ===
    INVALID_PASSWORD("MBR-200", 400, "비밀번호가 올바르지 않습니다."),
    PASSWORD_POLICY_VIOLATION("MBR-201", 400, "비밀번호는 8자 이상이며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."),

    // === 소셜 로그인 관련 (MBR-300 ~ MBR-399) ===
    INVALID_SOCIAL_ID("MBR-300", 400, "소셜 ID가 올바르지 않습니다."),
    ALREADY_KAKAO_MEMBER("MBR-301", 409, "이미 카카오로 연동된 회원입니다. 카카오 회원은 카카오 로그인을 이용해주세요."),
    KAKAO_MEMBER_CANNOT_CHANGE_PASSWORD("MBR-302", 400, "카카오 회원은 비밀번호를 변경할 수 없습니다."),

    // === 회원 상태 관련 (MBR-400 ~ MBR-499) ===
    INACTIVE_MEMBER("MBR-400", 403, "휴면 또는 정지된 회원입니다. 고객센터에 문의하세요."),
    ALREADY_WITHDRAWN_MEMBER("MBR-401", 400, "이미 탈퇴한 회원입니다.");

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
