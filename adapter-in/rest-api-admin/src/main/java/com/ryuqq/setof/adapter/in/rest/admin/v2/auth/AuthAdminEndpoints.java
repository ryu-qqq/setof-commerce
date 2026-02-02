package com.ryuqq.setof.adapter.in.rest.admin.v2.auth;

/**
 * Auth Admin API 엔드포인트 상수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class AuthAdminEndpoints {

    private AuthAdminEndpoints() {}

    /** 인증 API 기본 경로. */
    public static final String BASE = "/api/v2/admin/auth";

    /** 로그인 경로. */
    public static final String LOGIN = "/login";

    /** 로그아웃 경로. */
    public static final String LOGOUT = "/logout";

    /** 내 정보 조회 경로. */
    public static final String ME = "/me";
}
