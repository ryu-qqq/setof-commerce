package com.ryuqq.setof.integration.test.common;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.admin.SetofCommerceAdminApplication;
import com.ryuqq.setof.integration.test.config.AdminIntegrationTestConfig;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * Admin Integration Test Base Class
 *
 * <p>Admin API 통합 테스트의 공통 베이스 클래스입니다.
 *
 * <h3>특징</h3>
 *
 * <ul>
 *   <li>실제 서블릿 컨테이너 실행 (RANDOM_PORT)
 *   <li>TestRestTemplate을 통한 실제 HTTP 통신
 *   <li>테스트 후 데이터 정리 (@Sql cleanup)
 *   <li>외부 서비스 Mock 적용 (AdminIntegrationTestConfig)
 *   <li>Gateway 인증 헤더 자동 포함 (X-User-Id, X-Tenant-Id 등)
 * </ul>
 *
 * @since 1.0.0
 */
@SpringBootTest(
        classes = SetofCommerceAdminApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(AdminIntegrationTestConfig.class)
@Sql(
        scripts = "classpath:sql/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
public abstract class AdminIntegrationTestBase {

    // ============================================================
    // Gateway Authentication Header Constants
    // ============================================================

    protected static final String HEADER_USER_ID = "X-User-Id";
    protected static final String HEADER_TENANT_ID = "X-Tenant-Id";
    protected static final String HEADER_ORGANIZATION_ID = "X-Organization-Id";
    protected static final String HEADER_USER_ROLES = "X-User-Roles";

    /** 테스트용 기본 사용자 ID */
    protected static final String DEFAULT_USER_ID = "test-admin-user-001";

    /** 테스트용 기본 테넌트 ID */
    protected static final String DEFAULT_TENANT_ID = "default";

    /** 테스트용 기본 조직 ID */
    protected static final String DEFAULT_ORGANIZATION_ID = "default";

    /** 테스트용 기본 역할 (SUPER_ADMIN 권한) */
    protected static final String DEFAULT_ROLES = "SUPER_ADMIN";

    @LocalServerPort protected int port;

    @Autowired protected TestRestTemplate restTemplate;

    /** 테스트 API 기본 URL 생성 */
    protected String baseUrl() {
        return "http://localhost:" + port;
    }

    /** Admin API V2 URL 생성 */
    protected String adminApiV2Url(String path) {
        return baseUrl() + "/api/v2/admin" + path;
    }

    /** Admin API V1 URL 생성 */
    protected String adminApiV1Url(String path) {
        return baseUrl() + "/api/v1/admin" + path;
    }

    // ============================================================
    // Header Builders
    // ============================================================

    /**
     * 인증 헤더가 포함된 JSON Content-Type 헤더 생성
     *
     * <p>기본 인증 정보 (SUPER_ADMIN 권한)가 포함됩니다.
     */
    protected HttpHeaders jsonHeaders() {
        return jsonHeadersWithAuth(DEFAULT_USER_ID, DEFAULT_ROLES);
    }

    /**
     * 특정 사용자/역할로 인증 헤더 생성
     *
     * @param userId 사용자 ID
     * @param roles 역할 (콤마 구분, 예: "SUPER_ADMIN,TENANT_ADMIN")
     */
    protected HttpHeaders jsonHeadersWithAuth(String userId, String roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HEADER_USER_ID, userId);
        headers.set(HEADER_TENANT_ID, DEFAULT_TENANT_ID);
        headers.set(HEADER_ORGANIZATION_ID, DEFAULT_ORGANIZATION_ID);
        headers.set(HEADER_USER_ROLES, roles);
        return headers;
    }

    /** 인증 없는 헤더 생성 (Anonymous 테스트용) */
    protected HttpHeaders jsonHeadersNoAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /** JSON Request Entity 생성 (인증 헤더 포함) */
    protected <T> HttpEntity<T> jsonEntity(T body) {
        return new HttpEntity<>(body, jsonHeaders());
    }

    /** 특정 인증 정보로 Request Entity 생성 */
    protected <T> HttpEntity<T> jsonEntityWithAuth(T body, String userId, String roles) {
        return new HttpEntity<>(body, jsonHeadersWithAuth(userId, roles));
    }

    // ============================================================
    // HTTP Methods (인증 헤더 포함)
    // ============================================================

    /** GET 요청 실행 (인증 헤더 포함) */
    protected ResponseEntity<ApiResponse<Map<String, Object>>> get(String url) {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(jsonHeaders()),
                new ParameterizedTypeReference<>() {});
    }

    /** POST 요청 실행 (인증 헤더 포함) */
    protected <T> ResponseEntity<ApiResponse<Map<String, Object>>> post(String url, T body) {
        return restTemplate.exchange(
                url, HttpMethod.POST, jsonEntity(body), new ParameterizedTypeReference<>() {});
    }

    /** PUT 요청 실행 (인증 헤더 포함) */
    protected <T> ResponseEntity<ApiResponse<Map<String, Object>>> put(String url, T body) {
        return restTemplate.exchange(
                url, HttpMethod.PUT, jsonEntity(body), new ParameterizedTypeReference<>() {});
    }

    /** PATCH 요청 실행 (인증 헤더 포함) */
    protected <T> ResponseEntity<ApiResponse<Map<String, Object>>> patch(String url, T body) {
        return restTemplate.exchange(
                url, HttpMethod.PATCH, jsonEntity(body), new ParameterizedTypeReference<>() {});
    }

    /** DELETE 요청 실행 (인증 헤더 포함) */
    protected ResponseEntity<ApiResponse<Map<String, Object>>> delete(String url) {
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(jsonHeaders()),
                new ParameterizedTypeReference<>() {});
    }

    // ============================================================
    // HTTP Methods (인증 없음 - 보안 테스트용)
    // ============================================================

    /** 인증 없이 GET 요청 실행 (401 테스트용) */
    protected ResponseEntity<ApiResponse<Map<String, Object>>> getWithoutAuth(String url) {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(jsonHeadersNoAuth()),
                new ParameterizedTypeReference<>() {});
    }

    /** 인증 없이 POST 요청 실행 (401 테스트용) */
    protected <T> ResponseEntity<ApiResponse<Map<String, Object>>> postWithoutAuth(
            String url, T body) {
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, jsonHeadersNoAuth()),
                new ParameterizedTypeReference<>() {});
    }
}
