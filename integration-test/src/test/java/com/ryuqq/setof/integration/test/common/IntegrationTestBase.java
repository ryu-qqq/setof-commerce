package com.ryuqq.setof.integration.test.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.SetofCommerceApplication;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.RegisterMemberApiRequest;
import com.ryuqq.setof.integration.test.config.IntegrationTestConfig;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * Integration Test Base Class
 *
 * <p>모든 통합 테스트의 공통 베이스 클래스입니다.
 *
 * <h3>특징</h3>
 *
 * <ul>
 *   <li>실제 서블릿 컨테이너 실행 (RANDOM_PORT)
 *   <li>TestRestTemplate을 통한 실제 HTTP 통신
 *   <li>테스트 후 데이터 정리 (@Sql cleanup)
 *   <li>외부 서비스 Mock 적용 (IntegrationTestConfig)
 * </ul>
 *
 * @since 1.0.0
 */
@SpringBootTest(
        classes = SetofCommerceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(IntegrationTestConfig.class)
@Sql(
        scripts = "classpath:sql/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
public abstract class IntegrationTestBase {

    @LocalServerPort protected int port;

    @Autowired protected TestRestTemplate restTemplate;

    /** 테스트 API 기본 URL 생성 */
    protected String baseUrl() {
        return "http://localhost:" + port;
    }

    /** API V2 URL 생성 */
    protected String apiV2Url(String path) {
        return baseUrl() + "/api/v2" + path;
    }

    /** API V1 URL 생성 */
    protected String apiV1Url(String path) {
        return baseUrl() + "/api/v1" + path;
    }

    /** JSON Content-Type 헤더 생성 */
    protected HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /** JSON Request Entity 생성 */
    protected <T> HttpEntity<T> jsonEntity(T body) {
        return new HttpEntity<>(body, jsonHeaders());
    }

    /** 인증 헤더가 포함된 Request Entity 생성 */
    protected <T> HttpEntity<T> authenticatedEntity(T body, String token) {
        HttpHeaders headers = jsonHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    /** GET 요청용 인증 헤더만 포함된 Entity 생성 */
    protected HttpEntity<Void> authenticatedEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    /**
     * 테스트용 회원가입 후 Access Token 반환
     *
     * @param phoneNumber 전화번호
     * @param email 이메일
     * @return 발급된 Access Token
     */
    protected String registerMemberAndGetAccessToken(String phoneNumber, String email) {
        RegisterMemberApiRequest request =
                new RegisterMemberApiRequest(
                        phoneNumber, email, "Password1!", "테스터", null, null, true, true, false);

        ResponseEntity<ApiResponse<Map<String, Object>>> response =
                restTemplate.exchange(
                        apiV2Url("/members"),
                        HttpMethod.POST,
                        new HttpEntity<>(request, jsonHeaders()),
                        new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode())
                .withFailMessage(
                        "회원가입 실패. Phone: %s, Email: %s, Status: %s",
                        phoneNumber, email, response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        return (String) response.getBody().data().get("accessToken");
    }

    /**
     * 고유한 전화번호와 이메일로 테스트용 회원가입 후 Access Token 반환
     *
     * @param prefix 테스트 구분 접두사
     * @return 발급된 Access Token
     */
    protected String registerUniqueAndGetAccessToken(String prefix) {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis() % 100000000);
        String phoneNumber = "010" + uniqueSuffix;
        String email = prefix + uniqueSuffix + "@example.com";
        return registerMemberAndGetAccessToken(phoneNumber, email);
    }
}
