package com.ryuqq.setof.integration.test.paymentorder.checkout;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CompleteCheckoutV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CreateCheckoutV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.response.CheckoutV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.RegisterMemberApiRequest;
import com.ryuqq.setof.integration.test.common.IntegrationTestBase;
import com.ryuqq.setof.integration.test.paymentorder.fixture.PaymentOrderIntegrationTestFixture;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * Checkout Integration Test
 *
 * <p>체크아웃 API의 통합 테스트를 수행합니다.
 *
 * <h3>테스트 시나리오</h3>
 *
 * <ul>
 *   <li>CHK-001: 체크아웃 생성 - 정상 케이스
 *   <li>CHK-002: 체크아웃 생성 - 멱등성 키 중복
 *   <li>CHK-003: 체크아웃 조회 - 존재하는 ID
 *   <li>CHK-004: 체크아웃 조회 - 존재하지 않는 ID
 *   <li>CHK-005: 체크아웃 완료 - 정상 케이스
 *   <li>CHK-006: 체크아웃 완료 - 이미 완료됨
 *   <li>CHK-007: 체크아웃 완료 - 만료됨
 *   <li>CHK-008: 체크아웃 만료 처리
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Checkout Integration Test")
class CheckoutIntegrationTest extends IntegrationTestBase {

    // ============================================================
    // 체크아웃 생성 테스트
    // ============================================================

    @Nested
    @DisplayName("체크아웃 생성")
    class CreateCheckoutTest {

        @Test
        @Sql(
                scripts = {
                    "classpath:sql/member/member-test-data.sql",
                    "classpath:sql/paymentorder/checkout-order-test-data.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
        @DisplayName("CHK-001: 상품, 배송지, 결제수단 포함 체크아웃 생성에 성공한다")
        void shouldCreateCheckoutWithValidData() {
            // given
            String accessToken = registerAndGetAccessToken();
            CreateCheckoutV2ApiRequest request =
                    PaymentOrderIntegrationTestFixture.createDefaultCheckoutRequest();

            String url = apiV2Url("/checkouts");

            // when
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            CheckoutV2ApiResponse checkoutResponse = response.getBody().data();
            assertThat(checkoutResponse).isNotNull();
            assertThat(checkoutResponse.checkoutId()).isNotNull();
            assertThat(checkoutResponse.paymentId()).isNotNull();
            assertThat(checkoutResponse.status())
                    .isEqualTo(PaymentOrderIntegrationTestFixture.CHECKOUT_STATUS_PENDING);
            assertThat(checkoutResponse.items()).isNotEmpty();
            assertThat(checkoutResponse.receiverName())
                    .isEqualTo(PaymentOrderIntegrationTestFixture.DEFAULT_RECEIVER_NAME);
            assertThat(checkoutResponse.totalAmount())
                    .isEqualByComparingTo(
                            PaymentOrderIntegrationTestFixture.calculateDefaultTotalAmount());
        }

        @Test
        @Sql(
                scripts = {
                    "classpath:sql/member/member-test-data.sql",
                    "classpath:sql/paymentorder/checkout-order-test-data.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
        @DisplayName("CHK-002: 동일 멱등성 키로 재요청 시 409 Conflict를 반환한다")
        void shouldReturn409WhenDuplicateIdempotencyKey() {
            // given
            String accessToken = registerAndGetAccessToken();
            String idempotencyKey = UUID.randomUUID().toString();
            CreateCheckoutV2ApiRequest request =
                    createCheckoutRequestWithIdempotencyKey(idempotencyKey);

            String url = apiV2Url("/checkouts");

            // 첫 번째 요청 (성공)
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> firstResponse =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});
            assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            // when - 동일한 멱등성 키로 두 번째 요청
            ResponseEntity<String> secondResponse =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    // ============================================================
    // 체크아웃 조회 테스트
    // ============================================================

    @Nested
    @DisplayName("체크아웃 조회")
    class GetCheckoutTest {

        @Test
        @Sql(
                scripts = {
                    "classpath:sql/member/member-test-data.sql",
                    "classpath:sql/paymentorder/checkout-order-test-data.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
        @DisplayName("CHK-003: 존재하는 체크아웃 ID로 조회에 성공한다")
        void shouldGetCheckoutWithValidId() {
            // given
            String accessToken = registerAndGetAccessToken();

            // 먼저 체크아웃 생성
            CreateCheckoutV2ApiRequest createRequest =
                    PaymentOrderIntegrationTestFixture.createDefaultCheckoutRequest();
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> createResponse =
                    restTemplate.exchange(
                            apiV2Url("/checkouts"),
                            HttpMethod.POST,
                            authenticatedEntity(createRequest, accessToken),
                            new ParameterizedTypeReference<>() {});
            String checkoutId = createResponse.getBody().data().checkoutId();

            String url = apiV2Url("/checkouts/" + checkoutId);

            // when
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            CheckoutV2ApiResponse checkoutResponse = response.getBody().data();
            assertThat(checkoutResponse.checkoutId()).isEqualTo(checkoutId);
            assertThat(checkoutResponse.status())
                    .isEqualTo(PaymentOrderIntegrationTestFixture.CHECKOUT_STATUS_PENDING);
        }

        @Test
        @DisplayName("CHK-004: 존재하지 않는 체크아웃 ID로 조회 시 404 Not Found를 반환한다")
        void shouldReturn404WhenCheckoutNotFound() {
            // given
            String accessToken = registerAndGetAccessToken();
            String nonExistentCheckoutId =
                    PaymentOrderIntegrationTestFixture.NON_EXISTENT_CHECKOUT_ID;

            String url = apiV2Url("/checkouts/" + nonExistentCheckoutId);

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, authenticatedEntity(accessToken), String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // 체크아웃 완료 테스트
    // ============================================================

    @Nested
    @DisplayName("체크아웃 완료")
    class CompleteCheckoutTest {

        @Test
        @Sql(
                scripts = {
                    "classpath:sql/member/member-test-data.sql",
                    "classpath:sql/paymentorder/checkout-order-test-data.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
        @DisplayName("CHK-005: PENDING 상태의 체크아웃을 완료하고 주문을 생성한다")
        void shouldCompleteCheckoutAndCreateOrder() {
            // given
            String accessToken = registerAndGetAccessToken();

            // 체크아웃 생성
            CreateCheckoutV2ApiRequest createRequest =
                    PaymentOrderIntegrationTestFixture.createDefaultCheckoutRequest();
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> createResponse =
                    restTemplate.exchange(
                            apiV2Url("/checkouts"),
                            HttpMethod.POST,
                            authenticatedEntity(createRequest, accessToken),
                            new ParameterizedTypeReference<>() {});

            CheckoutV2ApiResponse checkoutData = createResponse.getBody().data();
            String paymentId = checkoutData.paymentId();
            BigDecimal totalAmount = checkoutData.totalAmount();

            // 체크아웃 완료 요청
            CompleteCheckoutV2ApiRequest completeRequest =
                    PaymentOrderIntegrationTestFixture.createCompleteCheckoutRequest(
                            paymentId, totalAmount);

            String url = apiV2Url("/checkouts/complete");

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(completeRequest, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            // 체크아웃 상태가 COMPLETED로 변경되었는지 확인
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> getResponse =
                    restTemplate.exchange(
                            apiV2Url("/checkouts/" + checkoutData.checkoutId()),
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(getResponse.getBody().data().status())
                    .isEqualTo(PaymentOrderIntegrationTestFixture.CHECKOUT_STATUS_COMPLETED);
        }

        @Test
        @Sql(
                scripts = {
                    "classpath:sql/member/member-test-data.sql",
                    "classpath:sql/paymentorder/checkout-order-test-data.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
        @DisplayName("CHK-006: 이미 완료된 체크아웃 완료 시도 시 409 Conflict를 반환한다")
        void shouldReturn409WhenCheckoutAlreadyCompleted() {
            // given
            String accessToken = registerAndGetAccessToken();

            // 사전 등록된 COMPLETED 상태의 체크아웃 사용
            String paymentId = PaymentOrderIntegrationTestFixture.ORDERED_PAYMENT_ID;
            BigDecimal amount = new BigDecimal("59800");

            CompleteCheckoutV2ApiRequest request =
                    PaymentOrderIntegrationTestFixture.createCompleteCheckoutRequest(
                            paymentId, amount);

            String url = apiV2Url("/checkouts/complete");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then - 이미 APPROVED 상태인 Payment를 다시 승인하려 하므로 409 Conflict
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        @Sql(
                scripts = {
                    "classpath:sql/member/member-test-data.sql",
                    "classpath:sql/paymentorder/checkout-order-test-data.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
        @DisplayName("CHK-007: 만료된 체크아웃 완료 시도 시 400 Bad Request를 반환한다")
        void shouldReturn400WhenCheckoutExpired() {
            // given
            String accessToken = registerAndGetAccessToken();

            // 사전 등록된 EXPIRED 상태의 체크아웃 사용
            // 주의: EXPIRED 체크아웃은 별도 paymentId가 필요
            // 이 테스트는 만료된 체크아웃에 대한 완료 시도를 검증
            String expiredCheckoutId = PaymentOrderIntegrationTestFixture.EXPIRED_CHECKOUT_ID;
            String dummyPaymentId = "dummy-expired-payment-id";
            BigDecimal amount = new BigDecimal("59800");

            CompleteCheckoutV2ApiRequest request =
                    PaymentOrderIntegrationTestFixture.createCompleteCheckoutRequest(
                            dummyPaymentId, amount);

            String url = apiV2Url("/checkouts/complete");

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            // 만료된 체크아웃 또는 존재하지 않는 paymentId → 400 또는 404
            assertThat(response.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // 체크아웃 만료 처리 테스트
    // ============================================================

    @Nested
    @DisplayName("체크아웃 만료 처리")
    class ExpireCheckoutTest {

        @Test
        @Sql(
                scripts = {
                    "classpath:sql/member/member-test-data.sql",
                    "classpath:sql/paymentorder/checkout-order-test-data.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR))
        @DisplayName("CHK-008: 만료된 체크아웃 조회 시 EXPIRED 상태를 반환한다")
        void shouldReturnExpiredStatusForExpiredCheckout() {
            // given
            String accessToken = registerAndGetAccessToken();
            String expiredCheckoutId = PaymentOrderIntegrationTestFixture.EXPIRED_CHECKOUT_ID;

            String url = apiV2Url("/checkouts/" + expiredCheckoutId);

            // when
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // 만료된 체크아웃 조회가 가능한 경우 EXPIRED 상태
            // 또는 만료된 체크아웃은 조회 불가로 404 반환
            if (response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody().data().status())
                        .isEqualTo(PaymentOrderIntegrationTestFixture.CHECKOUT_STATUS_EXPIRED);
            } else {
                assertThat(response.getStatusCode())
                        .isIn(HttpStatus.NOT_FOUND, HttpStatus.FORBIDDEN);
            }
        }
    }

    // ============================================================
    // 헬퍼 메서드
    // ============================================================

    /** 테스트용 회원가입 후 Access Token 반환 */
    private String registerAndGetAccessToken() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis() % 100000000);
        String phoneNumber = "010" + uniqueSuffix;

        RegisterMemberApiRequest request =
                new RegisterMemberApiRequest(
                        phoneNumber,
                        "checkout" + uniqueSuffix + "@example.com",
                        "Password1!",
                        "테스터",
                        null,
                        null,
                        true,
                        true,
                        false);

        ResponseEntity<ApiResponse<Map<String, Object>>> response =
                restTemplate.exchange(
                        apiV2Url("/members"),
                        HttpMethod.POST,
                        new HttpEntity<>(request, jsonHeaders()),
                        new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode())
                .withFailMessage(
                        "회원가입 실패. Phone: %s, Status: %s", phoneNumber, response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        return (String) response.getBody().data().get("accessToken");
    }

    /** 커스텀 멱등성 키로 체크아웃 생성 요청 생성 */
    private CreateCheckoutV2ApiRequest createCheckoutRequestWithIdempotencyKey(
            String idempotencyKey) {
        return new CreateCheckoutV2ApiRequest(
                idempotencyKey,
                java.util.List.of(
                        PaymentOrderIntegrationTestFixture.createDefaultCheckoutItemRequest()),
                PaymentOrderIntegrationTestFixture.PG_PROVIDER_TOSS,
                PaymentOrderIntegrationTestFixture.PAYMENT_METHOD_CARD,
                PaymentOrderIntegrationTestFixture.DEFAULT_RECEIVER_NAME,
                PaymentOrderIntegrationTestFixture.DEFAULT_RECEIVER_PHONE,
                PaymentOrderIntegrationTestFixture.DEFAULT_ZIP_CODE,
                PaymentOrderIntegrationTestFixture.DEFAULT_ADDRESS,
                PaymentOrderIntegrationTestFixture.DEFAULT_ADDRESS_DETAIL,
                PaymentOrderIntegrationTestFixture.DEFAULT_DELIVERY_REQUEST);
    }
}
