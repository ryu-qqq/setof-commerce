package com.ryuqq.setof.adapter.in.rest.v1.auth;

/**
 * AuthV1Endpoints - 인증/회원 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 UserController 경로 호환: /api/v1/user/*
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public final class AuthV1Endpoints {

    private AuthV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 사용자 기본 경로 */
    public static final String USER = BASE_V1 + "/user";

    /** 로그인 (POST /api/v1/user/login) */
    public static final String LOGIN = USER + "/login";

    /** 회원가입 (POST /api/v1/user/join) */
    public static final String JOIN = USER + "/join";

    /** 회원탈퇴 (POST /api/v1/user/withdrawl) - 레거시 URL 오타 유지 */
    public static final String WITHDRAWAL = USER + "/withdrawl";

    /** 비밀번호 재설정 (PATCH /api/v1/user/password) */
    public static final String RESET_PASSWORD = USER + "/password";

    /** 사용자 존재 여부 (GET /api/v1/user/exists) */
    public static final String EXISTS = USER + "/exists";

    /** 로그아웃 (POST /api/v1/user/logout) */
    public static final String LOGOUT = USER + "/logout";

    /** 마이페이지 (GET /api/v1/user/my-page) */
    public static final String MY_PAGE = USER + "/my-page";
}
