package com.ryuqq.setof.adapter.in.rest.auth.component;

import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * Security Context Authenticator
 *
 * <p>SecurityContext에 인증 정보를 설정하는 컴포넌트
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Access Token에서 memberId 추출
 *   <li>MemberPrincipal 생성
 *   <li>SecurityContext에 Authentication 설정
 * </ul>
 *
 * <p>사용 위치:
 *
 * <ul>
 *   <li>JwtAuthenticationFilter - JWT 인증 성공 시
 *   <li>OAuth2SuccessHandler - 카카오 로그인 성공 시 (필요 시)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SecurityContextAuthenticator {

    private final TokenProviderPort tokenProviderPort;

    public SecurityContextAuthenticator(TokenProviderPort tokenProviderPort) {
        this.tokenProviderPort = tokenProviderPort;
    }

    /**
     * Access Token으로 SecurityContext에 인증 설정
     *
     * @param request HttpServletRequest (인증 세부정보 설정용)
     * @param accessToken 검증된 Access Token
     * @return 인증된 memberId
     */
    public String authenticate(HttpServletRequest request, String accessToken) {
        String memberId = tokenProviderPort.extractMemberId(accessToken);
        MemberPrincipal principal = MemberPrincipal.of(memberId, null);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return memberId;
    }

    /**
     * MemberPrincipal로 직접 SecurityContext에 인증 설정
     *
     * <p>OAuth2 로그인 등에서 사용
     *
     * @param request HttpServletRequest (인증 세부정보 설정용)
     * @param principal 인증할 MemberPrincipal
     */
    public void authenticate(HttpServletRequest request, MemberPrincipal principal) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /** 현재 SecurityContext 인증 정보 해제 */
    public void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}
