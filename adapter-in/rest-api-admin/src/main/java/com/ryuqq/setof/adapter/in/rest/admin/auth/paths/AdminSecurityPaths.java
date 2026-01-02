package com.ryuqq.setof.adapter.in.rest.admin.auth.paths;

import java.util.List;

/**
 * Admin 보안 경로 분류 상수 정의
 *
 * <p>접근 권한별 경로를 정의합니다. SecurityConfig에서 참조하여 권한 설정에 사용됩니다.
 *
 * <p>경로 분류:
 *
 * <ul>
 *   <li>PUBLIC: 인증 불필요 (헬스체크, API 문서)
 *   <li>AUTHENTICATED: 인증된 사용자 + @PreAuthorize 권한 검사 (관리 API)
 * </ul>
 *
 * <p>권한 처리:
 *
 * <ul>
 *   <li>URL 기반 역할 검사 제거 → @PreAuthorize 어노테이션으로 대체
 *   <li>ResourceAccessChecker SpEL 함수로 리소스 접근 제어
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public final class AdminSecurityPaths {

    private AdminSecurityPaths() {}

    /**
     * 인증 불필요 경로 (Public)
     *
     * <p>인증 없이 접근 가능한 경로입니다.
     */
    public static final class Public {

        /** Public 경로 목록 */
        public static final List<String> PATTERNS =
                List.of(
                        // 헬스체크
                        AdminApiPaths.Actuator.BASE + "/**", AdminApiPaths.Health.CHECK);

        private Public() {}
    }

    /**
     * API 문서 경로 (Swagger, REST Docs)
     *
     * <p>개발 편의를 위해 인증 없이 접근 가능한 API 문서 경로입니다.
     */
    public static final class Docs {

        /** API 문서 경로 목록 (Gateway 경로 + SpringDoc 기본 경로) */
        public static final List<String> PATTERNS =
                List.of(
                        // Gateway 라우팅용 경로 (서비스별 prefix)
                        AdminApiPaths.OpenApi.SWAGGER_REDIRECT,
                        AdminApiPaths.OpenApi.SWAGGER_UI,
                        AdminApiPaths.OpenApi.SWAGGER_UI_HTML,
                        AdminApiPaths.OpenApi.DOCS,
                        // SpringDoc 기본 경로 (로컬 개발용)
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        // REST Docs
                        AdminApiPaths.Docs.BASE,
                        AdminApiPaths.Docs.ALL);

        private Docs() {}
    }

    /**
     * 헤더 상수
     *
     * <p>Gateway에서 전달하는 인증 정보 헤더입니다.
     */
    public static final class Headers {

        /** 사용자 ID 헤더 - Gateway에서 JWT userId 클레임 추출 */
        public static final String USER_ID = "X-User-Id";

        /** 테넌트 ID 헤더 - Gateway에서 JWT tenantId 클레임 추출 */
        public static final String TENANT_ID = "X-Tenant-Id";

        /** 조직 ID 헤더 - Gateway에서 JWT organizationId 클레임 추출 */
        public static final String ORGANIZATION_ID = "X-Organization-Id";

        /** 역할 헤더 - 콤마 구분 (예: "SUPER_ADMIN,TENANT_ADMIN") */
        public static final String ROLES = "X-User-Roles";

        /** 권한 헤더 - 콤마 구분 (예: "seller:read,product:write") */
        public static final String PERMISSIONS = "X-Permissions";

        /** 추적 ID 헤더 - 분산 추적용 */
        public static final String TRACE_ID = "X-Trace-Id";

        private Headers() {}
    }
}
