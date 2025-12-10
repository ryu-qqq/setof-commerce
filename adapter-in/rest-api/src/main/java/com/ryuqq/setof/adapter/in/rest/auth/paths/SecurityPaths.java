package com.ryuqq.setof.adapter.in.rest.auth.paths;

/**
 * 보안 정책별 경로 그룹화
 *
 * <p>인증/인가 정책에 따라 경로를 그룹화합니다.
 *
 * <p>Spring Security에서 참조하여 접근 제어 설정에 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public final class SecurityPaths {

    private SecurityPaths() {}

    /**
     * 인증 불필요 엔드포인트 (Public)
     *
     * <p>로그인, 회원가입 등 인증 없이 접근 가능한 경로
     */
    public static final String[] PUBLIC_ENDPOINTS = {
        ApiV2Paths.Members.REGISTER, ApiV2Paths.Members.PASSWORD_RESET, ApiV2Paths.Auth.LOGIN,
    };

    /**
     * 인증 불필요 패턴 (Public Patterns)
     *
     * <p>와일드카드가 필요한 공개 경로
     */
    public static final String[] PUBLIC_PATTERNS = {
        "/oauth2/**",
        "/login/oauth2/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/actuator/**",
        ApiV2Paths.Health.BASE,
    };

    /** 관리자 전용 엔드포인트 (ROLE_ADMIN) */
    public static final String[] ADMIN_ENDPOINTS = {
        ApiV2Paths.API_V2 + "/admin/**",
    };

    /**
     * 인증 필요 엔드포인트 (Authenticated)
     *
     * <p>로그인한 사용자만 접근 가능한 경로
     */
    public static final String[] AUTHENTICATED_ENDPOINTS = {
        ApiV2Paths.Members.ME,
        ApiV2Paths.Members.WITHDRAW,
        ApiV2Paths.Members.KAKAO_LINK,
        ApiV2Paths.Auth.LOGOUT,
    };
}
