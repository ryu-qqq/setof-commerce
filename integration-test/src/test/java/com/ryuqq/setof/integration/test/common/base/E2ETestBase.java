package com.ryuqq.setof.integration.test.common.base;

import com.ryuqq.setof.integration.test.common.config.TestSecurityConfig;
import com.ryuqq.setof.integration.test.common.config.TestWebApplication;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * E2E 통합 테스트 베이스 클래스 (일반 사용자 API)
 *
 * <p>Bootstrap 애플리케이션(web-api) 전체 컨텍스트를 로드하여 REST API -> Application -> Domain -> Repository -> DB
 * 전체 흐름을 테스트합니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * class MemberE2ETest extends E2ETestBase {
 *
 *     @Nested
 *     @DisplayName("회원 가입 API")
 *     class RegisterTest {
 *
 *         @Test
 *         void shouldRegisterMember() {
 *             given()
 *                 .contentType(ContentType.JSON)
 *                 .body(request)
 *             .when()
 *                 .post("/v1/members")
 *             .then()
 *                 .statusCode(201);
 *         }
 *     }
 * }
 * }</pre>
 *
 * <p>실행:
 *
 * <pre>
 * ./gradlew :integration-test:e2eTest
 * ./gradlew :integration-test:apiE2ETest  # API E2E만
 * </pre>
 *
 * @see TestTags#E2E
 * @see TestTags#API
 */
@Tag(TestTags.E2E)
@Tag(TestTags.API)
@SpringBootTest(
        classes = TestWebApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class E2ETestBase {

    @LocalServerPort protected int port;

    /** 테스트 전 RestAssured 설정 */
    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    /**
     * JSON Content-Type이 설정된 RequestSpecification 반환
     *
     * @return JSON 요청 스펙
     */
    protected RequestSpecification givenJson() {
        return RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON);
    }

    /**
     * 인증 토큰이 포함된 RequestSpecification 반환
     *
     * @param token JWT 토큰
     * @return 인증된 요청 스펙
     */
    protected RequestSpecification givenAuthenticated(String token) {
        return givenJson().header("Authorization", "Bearer " + token);
    }

    /**
     * 테스트용 회원 등록 후 토큰 반환 (헬퍼 메서드)
     *
     * <p>서브클래스에서 오버라이드하여 사용
     *
     * @return JWT 토큰
     */
    protected String registerAndGetToken() {
        throw new UnsupportedOperationException("서브클래스에서 구현 필요");
    }
}
