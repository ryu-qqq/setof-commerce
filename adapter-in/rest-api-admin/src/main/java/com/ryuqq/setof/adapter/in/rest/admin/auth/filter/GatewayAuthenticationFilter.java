package com.ryuqq.setof.adapter.in.rest.admin.auth.filter;

import com.ryuqq.setof.adapter.in.rest.admin.auth.context.SecurityContext;
import com.ryuqq.setof.adapter.in.rest.admin.auth.context.SecurityContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Gateway 헤더 기반 인증 필터
 *
 * <p>Gateway에서 전달하는 X-* 헤더를 파싱하여 SecurityContext를 설정합니다.
 *
 * <p><strong>인증 흐름:</strong>
 *
 * <ol>
 *   <li>X-User-Id 헤더가 있으면 Gateway 인증 사용 (프로덕션)
 *   <li>X-User-Id 없으면 Anonymous 처리
 * </ol>
 *
 * <p>하이브리드 권한 체계:
 *
 * <ul>
 *   <li>X-User-Roles: 역할 기반 권한 (콤마 구분, 예: SUPER_ADMIN,TENANT_ADMIN)
 *   <li>X-Permissions: 리소스 기반 권한 (콤마 구분, 예: seller:read,product:write)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(GatewayAuthenticationFilter.class);

    /** Gateway 헤더 상수 */
    private static final String HEADER_USER_ID = "X-User-Id";

    private static final String HEADER_TENANT_ID = "X-Tenant-Id";
    private static final String HEADER_ORGANIZATION_ID = "X-Organization-Id";
    private static final String HEADER_ROLES = "X-User-Roles";
    private static final String HEADER_PERMISSIONS = "X-Permissions";
    private static final String HEADER_TRACE_ID = "X-Trace-Id";

    /** ROLE_ prefix */
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            SecurityContext context = buildSecurityContext(request);
            SecurityContextHolder.setContext(context);

            if (context.isAuthenticated()) {
                synchronizeWithSpringSecurityContext(context);
            }

            filterChain.doFilter(request, response);

        } finally {
            SecurityContextHolder.clearContext();
            org.springframework.security.core.context.SecurityContextHolder.clearContext();
        }
    }

    /**
     * 요청 헤더에서 SecurityContext 생성
     *
     * @param request HTTP 요청
     * @return SecurityContext
     */
    private SecurityContext buildSecurityContext(HttpServletRequest request) {
        // 디버그: 인증 관련 헤더 로깅
        logAuthHeaders(request);

        // Gateway 인증 (X-User-Id 헤더)
        String userId = request.getHeader(HEADER_USER_ID);
        if (StringUtils.hasText(userId)) {
            log.debug("[AUTH] Gateway 인증 시도: userId={}", userId);
            return buildGatewaySecurityContext(request, userId);
        }

        // Anonymous
        log.debug("[AUTH] Anonymous 처리 (인증 헤더 없음)");
        return SecurityContext.anonymous();
    }

    /**
     * 인증 관련 헤더를 로깅합니다 (디버깅용).
     *
     * @param request HTTP 요청
     */
    private void logAuthHeaders(HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[AUTH-HEADERS] URI: {} {}", request.getMethod(), request.getRequestURI());
            log.debug("[AUTH-HEADERS] X-User-Id: {}", request.getHeader(HEADER_USER_ID));
            log.debug("[AUTH-HEADERS] X-Tenant-Id: {}", request.getHeader(HEADER_TENANT_ID));
            log.debug(
                    "[AUTH-HEADERS] X-Organization-Id: {}",
                    request.getHeader(HEADER_ORGANIZATION_ID));
            log.debug("[AUTH-HEADERS] X-User-Roles: {}", request.getHeader(HEADER_ROLES));
        }
    }

    /**
     * Gateway 인증 컨텍스트 생성
     *
     * <p>X-* 헤더에서 인증 정보를 추출합니다.
     */
    private SecurityContext buildGatewaySecurityContext(HttpServletRequest request, String userId) {
        String tenantId = parseStringHeader(request.getHeader(HEADER_TENANT_ID));
        String organizationId = parseStringHeader(request.getHeader(HEADER_ORGANIZATION_ID));
        Set<String> roles = parseRoles(request.getHeader(HEADER_ROLES));
        Set<String> permissions = parsePermissions(request.getHeader(HEADER_PERMISSIONS));
        String traceId = request.getHeader(HEADER_TRACE_ID);

        log.debug(
                "[AUTH] Gateway SecurityContext 생성: userId={}, tenantId={}, orgId={}, roles={}",
                userId,
                tenantId,
                organizationId,
                roles);

        return SecurityContext.builder()
                .userId(userId)
                .tenantId(tenantId)
                .organizationId(organizationId)
                .roles(roles)
                .permissions(permissions)
                .traceId(traceId)
                .build();
    }

    /**
     * 문자열 헤더 파싱
     *
     * <p>빈 값이면 null을 반환합니다.
     *
     * @param header 헤더 값
     * @return 유효한 문자열이면 그대로, 빈 값이면 null
     */
    private String parseStringHeader(String header) {
        return StringUtils.hasText(header) ? header : null;
    }

    /**
     * X-User-Roles 헤더 파싱
     *
     * <p>콤마로 구분된 역할 문자열을 파싱합니다. 예: "SUPER_ADMIN,TENANT_ADMIN"
     *
     * <p>Spring Security의 hasRole()은 자동으로 ROLE_ prefix를 추가하여 검사하므로, Gateway에서 prefix 없이 보내도 여기서
     * 자동으로 ROLE_ prefix를 추가합니다.
     *
     * @param rolesHeader 역할 헤더 값
     * @return ROLE_ prefix가 추가된 역할 Set
     */
    private Set<String> parseRoles(String rolesHeader) {
        if (!StringUtils.hasText(rolesHeader)) {
            return Set.of();
        }
        return Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(role -> role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role)
                .collect(Collectors.toSet());
    }

    /**
     * X-Permissions 헤더 파싱
     *
     * <p>콤마로 구분된 권한 문자열을 파싱합니다. 예: "seller:read,product:write"
     *
     * @param permissionsHeader 권한 헤더 값
     * @return 권한 Set
     */
    private Set<String> parsePermissions(String permissionsHeader) {
        if (!StringUtils.hasText(permissionsHeader)) {
            return Set.of();
        }
        return Arrays.stream(permissionsHeader.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet());
    }

    /**
     * Spring Security Context와 동기화
     *
     * <p>Spring Security의 @PreAuthorize 어노테이션이 동작하도록 Spring SecurityContext에도 인증 정보를 설정합니다.
     *
     * @param context 커스텀 SecurityContext
     */
    private void synchronizeWithSpringSecurityContext(SecurityContext context) {
        // Roles를 GrantedAuthority로 변환
        Set<GrantedAuthority> authorities =
                context.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());

        // Permissions도 GrantedAuthority로 추가 (Spring Security에서 hasAuthority() 사용 가능)
        context.getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(context.getUserId(), null, authorities);

        org.springframework.security.core.context.SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}
