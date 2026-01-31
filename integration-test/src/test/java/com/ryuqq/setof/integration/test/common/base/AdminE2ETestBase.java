package com.ryuqq.setof.integration.test.common.base;

import com.ryuqq.setof.admin.SetofCommerceAdminApplication;
import com.ryuqq.setof.integration.test.common.config.TestSecurityConfig;
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
 * Admin E2E 통합 테스트 베이스 클래스 (관리자 API)
 *
 * <p>Bootstrap 애플리케이션(web-api-admin) 전체 컨텍스트를 로드하여 Admin REST API -> Application -> Domain ->
 * Repository -> DB 전체 흐름을 테스트합니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * class SellerAdminE2ETest extends AdminE2ETestBase {
 *
 *     @Nested
 *     @DisplayName("판매자 등록 API")
 *     class RegisterSellerTest {
 *
 *         @Test
 *         void shouldRegisterSeller() {
 *             givenAdmin()
 *             .when()
 *                 .post("/v2/sellers")
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
 * ./gradlew :integration-test:adminE2ETest  # Admin E2E만
 * </pre>
 *
 * @see TestTags#E2E
 * @see TestTags#ADMIN
 */
@Tag(TestTags.E2E)
@Tag(TestTags.ADMIN)
@SpringBootTest(
        classes = SetofCommerceAdminApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AdminE2ETestBase {

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
     * 관리자 인증이 포함된 RequestSpecification 반환
     *
     * <p>Gateway 헤더 방식 또는 JWT 토큰 방식 지원
     *
     * @return 관리자 인증된 요청 스펙
     */
    protected RequestSpecification givenAdmin() {
        return givenJson()
                .header("X-Seller-Id", getTestSellerId())
                .header("X-User-Id", getTestUserId());
    }

    /**
     * 특정 판매자 ID로 인증된 RequestSpecification 반환
     *
     * @param sellerId 판매자 ID
     * @return 인증된 요청 스펙
     */
    protected RequestSpecification givenAdmin(long sellerId) {
        return givenJson().header("X-Seller-Id", sellerId).header("X-User-Id", getTestUserId());
    }

    /**
     * 테스트용 판매자 ID 반환
     *
     * <p>서브클래스에서 오버라이드 가능
     *
     * @return 테스트 판매자 ID
     */
    protected long getTestSellerId() {
        return 1L;
    }

    /**
     * 테스트용 사용자 ID 반환
     *
     * <p>서브클래스에서 오버라이드 가능
     *
     * @return 테스트 사용자 ID
     */
    protected long getTestUserId() {
        return 1L;
    }
}
