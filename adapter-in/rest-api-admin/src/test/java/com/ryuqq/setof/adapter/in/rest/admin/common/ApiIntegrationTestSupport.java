package com.ryuqq.setof.adapter.in.rest.admin.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

/**
 * REST API Admin 통합 테스트 지원 추상 클래스
 *
 * <p>모든 Admin REST API 통합 테스트는 이 클래스를 상속받아 작성합니다.
 *
 * <p><strong>제공 기능:</strong>
 *
 * <ul>
 *   <li>TestContainers MySQL 자동 설정
 *   <li>TestRestTemplate 자동 주입
 *   <li>HTTP 요청 헬퍼 메서드
 *   <li>@Sql 어노테이션으로 테스트 데이터 설정
 * </ul>
 *
 * <h2>사용 예시:</h2>
 *
 * <pre>{@code
 * @DisplayName("Seller Admin API 통합 테스트")
 * class SellerAdminApiIntegrationTest extends ApiIntegrationTestSupport {
 *
 *     @Test
 *     @Sql("/sql/seller/sellers-test-data.sql")
 *     @DisplayName("GET /api/v2/admin/sellers/{sellerId} - 셀러 단건 조회")
 *     void getSeller_success() {
 *         // Given
 *         Long sellerId = 1L;
 *
 *         // When
 *         ResponseEntity<ApiResponse<SellerV2ApiResponse>> response = get(
 *             "/api/v2/admin/sellers/{sellerId}",
 *             new ParameterizedTypeReference<>() {},
 *             sellerId
 *         );
 *
 *         // Then
 *         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
 *     }
 * }
 * }</pre>
 *
 * @author Development Team
 * @since 1.0.0
 */
@SpringBootTest(
        classes =
                com.ryuqq.setof.adapter.in.rest.admin.integration.TestRestApiAdminApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({
    ApiIntegrationTestSupport.JacksonTestConfig.class,
    TestMockBeanConfig.class,
    TestSecurityConfig.class
})
public abstract class ApiIntegrationTestSupport {

    /**
     * MySQL TestContainer
     *
     * <p>모든 테스트에서 공유되는 단일 컨테이너입니다.
     *
     * <p>static 블록에서 수동으로 시작하여 {@code @DynamicPropertySource} 호출 전에 컨테이너가 준비되도록 합니다.
     */
    static MySQLContainer<?> mysql;

    static {
        mysql =
                new MySQLContainer<>("mysql:8.0")
                        .withDatabaseName("test")
                        .withUsername("test")
                        .withPassword("test")
                        .withCommand(
                                "--character-set-server=utf8mb4",
                                "--collation-server=utf8mb4_unicode_ci");
        mysql.start();
    }

    /**
     * TestRestTemplate - 실제 HTTP 요청 테스트
     *
     * <p>MockMvc와 달리 실제 HTTP 요청/응답을 테스트합니다.
     */
    @Autowired protected TestRestTemplate restTemplate;

    /**
     * Jackson 테스트 설정
     *
     * <p>LocalDateTime 역직렬화 시 타임존 정보가 포함된 ISO-8601 형식을 처리하도록 설정합니다.
     */
    @TestConfiguration
    static class JacksonTestConfig {

        @Bean
        @Primary
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();

            // JavaTimeModule을 먼저 등록
            mapper.registerModule(new JavaTimeModule());

            // 커스텀 LocalDateTime deserializer를 나중에 등록하여 기존 deserializer를 덮어씀
            SimpleModule customModule = new SimpleModule("CustomLocalDateTimeModule");
            customModule.addDeserializer(
                    LocalDateTime.class, new FlexibleLocalDateTimeDeserializer());
            mapper.registerModule(customModule);

            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper;
        }

        @Bean
        public RestTemplateBuilder restTemplateBuilder(ObjectMapper objectMapper) {
            MappingJackson2HttpMessageConverter converter =
                    new MappingJackson2HttpMessageConverter();
            converter.setObjectMapper(objectMapper);
            return new RestTemplateBuilder().additionalMessageConverters(converter);
        }
    }

    /**
     * 유연한 LocalDateTime Deserializer
     *
     * <p>타임존 정보가 포함된 ISO-8601 형식도 LocalDateTime으로 변환합니다.
     */
    static class FlexibleLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

        public FlexibleLocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            String dateString = p.getText();
            if (dateString == null || dateString.isEmpty()) {
                return null;
            }

            try {
                // 먼저 LocalDateTime으로 파싱 시도
                return LocalDateTime.parse(dateString);
            } catch (DateTimeParseException e) {
                try {
                    // 타임존 정보가 있으면 OffsetDateTime으로 파싱 후 LocalDateTime으로 변환
                    OffsetDateTime odt = OffsetDateTime.parse(dateString);
                    return odt.toLocalDateTime();
                } catch (DateTimeParseException e2) {
                    // ISO_DATE_TIME 형식으로 시도
                    return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
                }
            }
        }
    }

    /**
     * TestContainers 동적 프로퍼티 설정
     *
     * <p>컨테이너 시작 후 동적으로 DataSource 프로퍼티를 설정합니다.
     *
     * @param registry 동적 프로퍼티 레지스트리
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    }

    // ===== 테스트 인증 상수 =====

    /** 기본 테스트 관리자 ID */
    protected static final Long DEFAULT_TEST_ADMIN_ID = TestMockBeanConfig.DEFAULT_TEST_ADMIN_ID;

    /**
     * 기본 테스트 토큰
     *
     * <p>{@link TestMockBeanConfig#TEST_ADMIN_TOKEN_PREFIX} + {@link #DEFAULT_TEST_ADMIN_ID} 형식
     */
    protected static final String DEFAULT_TEST_TOKEN =
            TestMockBeanConfig.TEST_ADMIN_TOKEN_PREFIX + DEFAULT_TEST_ADMIN_ID;

    // ===== HTTP 요청 헬퍼 메서드 =====

    /**
     * GET 요청 헬퍼
     *
     * @param url 요청 URL
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> get(
            String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType, uriVariables);
    }

    /**
     * GET 요청 헬퍼 (Class 타입)
     *
     * @param url 요청 URL
     * @param responseType 응답 타입 클래스
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * POST 요청 헬퍼
     *
     * @param url 요청 URL
     * @param request 요청 본문
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> post(
            String url,
            Object request,
            ParameterizedTypeReference<T> responseType,
            Object... uriVariables) {
        return restTemplate.exchange(
                url, HttpMethod.POST, createJsonEntity(request), responseType, uriVariables);
    }

    /**
     * PUT 요청 헬퍼
     *
     * @param url 요청 URL
     * @param request 요청 본문
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> put(
            String url,
            Object request,
            ParameterizedTypeReference<T> responseType,
            Object... uriVariables) {
        return restTemplate.exchange(
                url, HttpMethod.PUT, createJsonEntity(request), responseType, uriVariables);
    }

    /**
     * PATCH 요청 헬퍼
     *
     * @param url 요청 URL
     * @param request 요청 본문
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> patch(
            String url,
            Object request,
            ParameterizedTypeReference<T> responseType,
            Object... uriVariables) {
        return restTemplate.exchange(
                url, HttpMethod.PATCH, createJsonEntity(request), responseType, uriVariables);
    }

    /**
     * DELETE 요청 헬퍼
     *
     * @param url 요청 URL
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> delete(
            String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, responseType, uriVariables);
    }

    // ===== 인증된 HTTP 요청 헬퍼 메서드 =====

    /**
     * 인증된 GET 요청 헬퍼 (기본 테스트 관리자)
     *
     * @param url 요청 URL
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> getAuthenticated(
            String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                createAuthenticatedEntity(DEFAULT_TEST_TOKEN),
                responseType,
                uriVariables);
    }

    /**
     * 인증된 POST 요청 헬퍼 (기본 테스트 관리자)
     *
     * @param url 요청 URL
     * @param request 요청 본문
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> postAuthenticated(
            String url,
            Object request,
            ParameterizedTypeReference<T> responseType,
            Object... uriVariables) {
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                createAuthenticatedJsonEntity(request, DEFAULT_TEST_TOKEN),
                responseType,
                uriVariables);
    }

    /**
     * 인증된 PUT 요청 헬퍼 (기본 테스트 관리자)
     *
     * @param url 요청 URL
     * @param request 요청 본문
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> putAuthenticated(
            String url,
            Object request,
            ParameterizedTypeReference<T> responseType,
            Object... uriVariables) {
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                createAuthenticatedJsonEntity(request, DEFAULT_TEST_TOKEN),
                responseType,
                uriVariables);
    }

    /**
     * 인증된 PATCH 요청 헬퍼 (기본 테스트 관리자)
     *
     * @param url 요청 URL
     * @param request 요청 본문
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> patchAuthenticated(
            String url,
            Object request,
            ParameterizedTypeReference<T> responseType,
            Object... uriVariables) {
        return restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                createAuthenticatedJsonEntity(request, DEFAULT_TEST_TOKEN),
                responseType,
                uriVariables);
    }

    /**
     * 인증된 DELETE 요청 헬퍼 (기본 테스트 관리자)
     *
     * @param url 요청 URL
     * @param responseType 응답 타입 참조
     * @param uriVariables URI 변수
     * @param <T> 응답 타입
     * @return ResponseEntity
     */
    protected <T> ResponseEntity<T> deleteAuthenticated(
            String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                createAuthenticatedEntity(DEFAULT_TEST_TOKEN),
                responseType,
                uriVariables);
    }

    // ===== Private 메서드 =====

    /**
     * JSON 요청 엔티티 생성
     *
     * @param body 요청 본문
     * @return HttpEntity
     */
    private HttpEntity<Object> createJsonEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    /**
     * 인증 헤더가 포함된 JSON 요청 엔티티 생성
     *
     * @param body 요청 본문
     * @param token 인증 토큰
     * @return HttpEntity
     */
    protected HttpEntity<Object> createAuthenticatedJsonEntity(Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    /**
     * 인증 헤더만 포함된 요청 엔티티 생성
     *
     * @param token 인증 토큰
     * @return HttpEntity
     */
    protected HttpEntity<Void> createAuthenticatedEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
