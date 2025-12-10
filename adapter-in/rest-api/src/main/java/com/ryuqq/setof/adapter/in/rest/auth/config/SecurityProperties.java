package com.ryuqq.setof.adapter.in.rest.auth.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Security Properties
 *
 * <p>Spring Security 관련 외부 설정을 관리하는 Properties 클래스
 *
 * <p>설정 항목:
 *
 * <ul>
 *   <li>publicEndpoints: 인증 없이 접근 가능한 엔드포인트 목록
 *   <li>cors: CORS 관련 설정 (허용 도메인, 메서드, 헤더 등)
 * </ul>
 *
 * <p>사용 예시 (application.yml):
 *
 * <pre>{@code
 * security:
 *   public-endpoints:
 *     - method: POST
 *       pattern: /api/v1/members
 *     - method: POST
 *       pattern: /api/v1/auth/login
 *     - pattern: /oauth2/**
 *   cors:
 *     allowed-origins:
 *       - http://localhost:3000
 *     allowed-methods:
 *       - GET
 *       - POST
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private List<PublicEndpoint> publicEndpoints = new ArrayList<>();
    private CorsProperties cors = new CorsProperties();
    private CookieProperties cookie = new CookieProperties();
    private OAuth2Properties oauth2 = new OAuth2Properties();

    public List<PublicEndpoint> getPublicEndpoints() {
        return publicEndpoints;
    }

    public void setPublicEndpoints(List<PublicEndpoint> publicEndpoints) {
        this.publicEndpoints = publicEndpoints;
    }

    public CorsProperties getCors() {
        return cors;
    }

    public void setCors(CorsProperties cors) {
        this.cors = cors;
    }

    public CookieProperties getCookie() {
        return cookie;
    }

    public void setCookie(CookieProperties cookie) {
        this.cookie = cookie;
    }

    public OAuth2Properties getOauth2() {
        return oauth2;
    }

    public void setOauth2(OAuth2Properties oauth2) {
        this.oauth2 = oauth2;
    }

    /**
     * 공개 엔드포인트 설정
     *
     * <p>method가 null이면 모든 HTTP 메서드 허용
     */
    public static class PublicEndpoint {

        private String method;
        private String pattern;

        public PublicEndpoint() {
            // Default constructor for Spring Boot property binding
        }

        public PublicEndpoint(String method, String pattern) {
            this.method = method;
            this.pattern = pattern;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        /** HTTP 메서드가 지정되었는지 여부 */
        public boolean hasMethod() {
            return method != null && !method.isBlank();
        }
    }

    /** CORS 관련 설정 */
    public static class CorsProperties {

        private List<String> allowedOrigins = new ArrayList<>();
        private List<String> allowedMethods = new ArrayList<>();
        private List<String> allowedHeaders = new ArrayList<>();
        private List<String> exposedHeaders = new ArrayList<>();
        private boolean allowCredentials = true;

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public List<String> getExposedHeaders() {
            return exposedHeaders;
        }

        public void setExposedHeaders(List<String> exposedHeaders) {
            this.exposedHeaders = exposedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }
    }

    /**
     * Cookie 관련 설정
     *
     * <p>JWT 토큰 쿠키의 보안 속성 설정
     *
     * <p>설정 예시 (application.yml):
     *
     * <pre>{@code
     * security:
     *   cookie:
     *     domain: localhost
     *     secure: false
     *     same-site: lax
     * }</pre>
     */
    public static class CookieProperties {

        private String domain = "localhost";
        private boolean secure = false;
        private String sameSite = "lax";

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public boolean isSecure() {
            return secure;
        }

        public void setSecure(boolean secure) {
            this.secure = secure;
        }

        public String getSameSite() {
            return sameSite;
        }

        public void setSameSite(String sameSite) {
            this.sameSite = sameSite;
        }

        /** localhost가 아닌 도메인인지 여부 */
        public boolean hasCustomDomain() {
            return domain != null && !"localhost".equals(domain);
        }
    }

    /**
     * OAuth2 관련 설정
     *
     * <p>OAuth2 인증 흐름에 필요한 설정
     *
     * <p>설정 예시 (application.yml):
     *
     * <pre>{@code
     * security:
     *   oauth2:
     *     front-domain-url: https://example.com
     *     authorized-redirect-uris:
     *       - https://example.com/oauth/callback
     *     cookie-expire-seconds: 180
     * }</pre>
     */
    public static class OAuth2Properties {

        private String frontDomainUrl = "http://localhost:3000";
        private List<String> authorizedRedirectUris = new ArrayList<>();
        private int cookieExpireSeconds = 180;

        public String getFrontDomainUrl() {
            return frontDomainUrl;
        }

        public void setFrontDomainUrl(String frontDomainUrl) {
            this.frontDomainUrl = frontDomainUrl;
        }

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }

        public void setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
        }

        public int getCookieExpireSeconds() {
            return cookieExpireSeconds;
        }

        public void setCookieExpireSeconds(int cookieExpireSeconds) {
            this.cookieExpireSeconds = cookieExpireSeconds;
        }
    }
}
