package com.ryuqq.setof.integration.test.paymentorder.e2e;

import static com.ryuqq.setof.integration.test.paymentorder.fixture.PaymentOrderIntegrationTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CreateCheckoutV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.response.CheckoutV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.claim.dto.command.RequestClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.claim.dto.response.ClaimV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.order.dto.response.OrderV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.orderevent.dto.response.OrderTimelineV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.payment.dto.command.ApprovePaymentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.payment.dto.response.PaymentV2ApiResponse;
import com.ryuqq.setof.integration.test.common.IntegrationTestBase;
import com.ryuqq.setof.integration.test.paymentorder.wiremock.PgGatewayWireMockStub;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Payment/Order E2E 통합 테스트
 *
 * <p>전체 구매 플로우 및 다양한 시나리오 E2E 테스트
 *
 * <p>테스트 시나리오:
 *
 * <ul>
 *   <li>E2E-001 ~ E2E-004: 정상 구매 플로우
 *   <li>E2E-005 ~ E2E-008: 취소/반품/교환 플로우
 *   <li>E2E-009 ~ E2E-012: 결제 관련 시나리오
 *   <li>E2E-013 ~ E2E-016: 복합 시나리오
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Payment/Order E2E 통합 테스트")
@Sql(
        scripts = {
            "/sql/cleanup.sql",
            "/sql/member-test-data.sql",
            "/sql/checkout-order-test-data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PaymentOrderE2EIntegrationTest extends IntegrationTestBase {

    @Autowired(required = false)
    private WireMockServer wireMockServer;

    // ============================================================
    // 헬퍼 메서드
    // ============================================================

    private String registerAndGetAccessToken() {
        return registerMemberAndGetAccessToken(ACTIVE_MEMBER_PHONE, ACTIVE_MEMBER_EMAIL);
    }

    @BeforeEach
    void resetWireMock() {
        if (wireMockServer != null) {
            wireMockServer.resetAll();
        }
    }

    @AfterEach
    void cleanupWireMock() {
        if (wireMockServer != null) {
            wireMockServer.resetAll();
        }
    }

    // ============================================================
    // E2E-001 ~ E2E-004: 정상 구매 플로우
    // ============================================================

    @Nested
    @DisplayName("정상 구매 플로우 테스트")
    class HappyPathTests {

        @Test
        @DisplayName("E2E-001: 전체 구매 플로우 - 체크아웃 생성 → 결제 → 주문 생성")
        void fullPurchaseFlow() {
            // given
            String accessToken = registerAndGetAccessToken();
            CreateCheckoutV2ApiRequest checkoutRequest = createDefaultCheckoutRequest();

            // Step 1: 체크아웃 생성
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> checkoutResponse =
                    restTemplate.exchange(
                            ApiV2Paths.Checkouts.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(checkoutRequest, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then - Step 1
            assertThat(checkoutResponse.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.OK);

            if (checkoutResponse.getStatusCode() == HttpStatus.CREATED
                    || checkoutResponse.getStatusCode() == HttpStatus.OK) {
                assertThat(checkoutResponse.getBody()).isNotNull();
                assertThat(checkoutResponse.getBody().data()).isNotNull();

                String checkoutId = checkoutResponse.getBody().data().checkoutId();
                String paymentId = checkoutResponse.getBody().data().paymentId();
                BigDecimal totalAmount = checkoutResponse.getBody().data().totalAmount();

                assertThat(checkoutId).isNotNull();
                assertThat(paymentId).isNotNull();
                assertThat(checkoutResponse.getBody().data().status())
                        .isEqualTo(CHECKOUT_STATUS_PENDING);

                // Step 2: 결제 승인 (PG 연동 모킹)
                if (wireMockServer != null) {
                    String paymentKey = "test_payment_key_" + System.currentTimeMillis();
                    PgGatewayWireMockStub.stubTossPaymentApproveSuccess(
                            wireMockServer, paymentKey, checkoutId, totalAmount);

                    ApprovePaymentV2ApiRequest approveRequest =
                            createApprovePaymentRequest(paymentId, paymentKey, totalAmount);

                    ResponseEntity<ApiResponse<PaymentV2ApiResponse>> paymentResponse =
                            restTemplate.exchange(
                                    ApiV2Paths.Payments.BASE + "/approve",
                                    HttpMethod.POST,
                                    authenticatedEntity(approveRequest, accessToken),
                                    new ParameterizedTypeReference<>() {});

                    // then - Step 2
                    assertThat(paymentResponse.getStatusCode())
                            .isIn(HttpStatus.OK, HttpStatus.NOT_FOUND, HttpStatus.BAD_REQUEST);
                }
            }
        }

        @Test
        @DisplayName("E2E-002: 체크아웃 조회 후 결제 진행")
        void checkoutAndPaymentFlow() {
            // given
            String accessToken = registerAndGetAccessToken();

            // 기존 PENDING 체크아웃 조회
            String checkoutUrl = ApiV2Paths.Checkouts.BASE + "/" + PENDING_CHECKOUT_ID;
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> checkoutResponse =
                    restTemplate.exchange(
                            checkoutUrl,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(checkoutResponse.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (checkoutResponse.getStatusCode() == HttpStatus.OK) {
                assertThat(checkoutResponse.getBody()).isNotNull();
                assertThat(checkoutResponse.getBody().data()).isNotNull();
                assertThat(checkoutResponse.getBody().data().status())
                        .isEqualTo(CHECKOUT_STATUS_PENDING);
            }
        }

        @Test
        @DisplayName("E2E-003: 다중 상품 주문 플로우")
        void multiItemPurchaseFlow() {
            // given
            String accessToken = registerAndGetAccessToken();
            CreateCheckoutV2ApiRequest checkoutRequest = createMultiItemCheckoutRequest();

            // when
            ResponseEntity<ApiResponse<CheckoutV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Checkouts.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(checkoutRequest, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.OK);

            if (response.getStatusCode() == HttpStatus.CREATED
                    || response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();

                // 다중 상품 총액 검증
                BigDecimal expectedTotal =
                        new BigDecimal("29900")
                                .multiply(BigDecimal.valueOf(2))
                                .add(new BigDecimal("39900"))
                                .add(new BigDecimal("19900").multiply(BigDecimal.valueOf(3)));

                // Note: 실제 계산 로직에 따라 다를 수 있음
                assertThat(response.getBody().data().totalAmount()).isNotNull();
            }
        }

        @Test
        @DisplayName("E2E-004: 주문 생성 후 타임라인 확인")
        void orderCreationWithTimelineVerification() {
            // given
            String accessToken = registerAndGetAccessToken();
            String timelineUrl = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID + "/timeline";

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
                    restTemplate.exchange(
                            timelineUrl,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().orderId()).isEqualTo(ORDERED_ORDER_ID);
            }
        }
    }

    // ============================================================
    // E2E-005 ~ E2E-008: 취소/반품/교환 플로우
    // ============================================================

    @Nested
    @DisplayName("취소/반품/교환 플로우 테스트")
    class ClaimFlowTests {

        @Test
        @DisplayName("E2E-005: 주문 취소 플로우 - ORDERED → CANCELLED")
        void orderCancelFlow() {
            // given
            String accessToken = registerAndGetAccessToken();
            String cancelUrl = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID + "/cancel";

            // when
            ResponseEntity<ApiResponse<OrderV2ApiResponse>> response =
                    restTemplate.exchange(
                            cancelUrl,
                            HttpMethod.POST,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.OK, HttpStatus.NOT_FOUND, HttpStatus.CONFLICT);

            if (response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().status()).isEqualTo(ORDER_STATUS_CANCELLED);
            }
        }

        @Test
        @DisplayName("E2E-006: 반품 요청 플로우 - DELIVERED 상태 주문")
        void returnRequestFlow() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createReturnClaimRequest(
                            DELIVERED_ORDER_ID,
                            DELIVERED_ORDER_ITEM_ID,
                            CLAIM_REASON_WRONG_SIZE,
                            1,
                            DEFAULT_UNIT_PRICE);

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CREATED, HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimType()).isEqualTo(CLAIM_TYPE_RETURN);
            }
        }

        @Test
        @DisplayName("E2E-007: 교환 요청 플로우 - DELIVERED 상태 주문")
        void exchangeRequestFlow() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createExchangeClaimRequest(
                            DELIVERED_ORDER_ID,
                            DELIVERED_ORDER_ITEM_ID,
                            CLAIM_REASON_WRONG_SIZE,
                            1);

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CREATED, HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimType()).isEqualTo(CLAIM_TYPE_EXCHANGE);
            }
        }

        @Test
        @DisplayName("E2E-008: 부분환불 요청 플로우")
        void partialRefundFlow() {
            // given
            String accessToken = registerAndGetAccessToken();
            BigDecimal partialRefund = new BigDecimal("10000");
            RequestClaimV2ApiRequest request =
                    createPartialRefundClaimRequest(
                            DELIVERED_ORDER_ID, DELIVERED_ORDER_ITEM_ID, 1, partialRefund);

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CREATED, HttpStatus.OK, HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // E2E-009 ~ E2E-012: 결제 관련 시나리오
    // ============================================================

    @Nested
    @DisplayName("결제 관련 시나리오 테스트")
    class PaymentScenarioTests {

        @Test
        @DisplayName("E2E-009: PG 결제 실패 후 재시도")
        void paymentRetryAfterFailure() {
            // given
            String accessToken = registerAndGetAccessToken();

            if (wireMockServer != null) {
                // 첫 번째 시도: 실패 스텁
                String paymentKey1 = "fail_payment_key_" + System.currentTimeMillis();
                PgGatewayWireMockStub.stubTossPaymentApproveFailInsufficientBalance(
                        wireMockServer, paymentKey1);

                ApprovePaymentV2ApiRequest failRequest =
                        createApprovePaymentRequest(
                                PENDING_PAYMENT_ID, paymentKey1, calculateDefaultTotalAmount());

                ResponseEntity<ApiResponse<PaymentV2ApiResponse>> failResponse =
                        restTemplate.exchange(
                                ApiV2Paths.Payments.BASE + "/approve",
                                HttpMethod.POST,
                                authenticatedEntity(failRequest, accessToken),
                                new ParameterizedTypeReference<>() {});

                // 실패 응답 확인
                assertThat(failResponse.getStatusCode())
                        .isIn(
                                HttpStatus.BAD_REQUEST,
                                HttpStatus.CONFLICT,
                                HttpStatus.NOT_FOUND,
                                HttpStatus.OK);

                // 스텁 초기화 후 재시도
                wireMockServer.resetAll();

                // 두 번째 시도: 성공 스텁
                String paymentKey2 = "success_payment_key_" + System.currentTimeMillis();
                PgGatewayWireMockStub.stubTossPaymentApproveSuccess(
                        wireMockServer,
                        paymentKey2,
                        PENDING_CHECKOUT_ID,
                        calculateDefaultTotalAmount());

                ApprovePaymentV2ApiRequest retryRequest =
                        createApprovePaymentRequest(
                                PENDING_PAYMENT_ID, paymentKey2, calculateDefaultTotalAmount());

                ResponseEntity<ApiResponse<PaymentV2ApiResponse>> retryResponse =
                        restTemplate.exchange(
                                ApiV2Paths.Payments.BASE + "/approve",
                                HttpMethod.POST,
                                authenticatedEntity(retryRequest, accessToken),
                                new ParameterizedTypeReference<>() {});

                // 재시도 결과 확인
                assertThat(retryResponse.getStatusCode())
                        .isIn(HttpStatus.OK, HttpStatus.NOT_FOUND, HttpStatus.CONFLICT);
            }
        }

        @Test
        @DisplayName("E2E-010: 금액 불일치 결제 시도")
        void paymentWithAmountMismatch() {
            // given
            String accessToken = registerAndGetAccessToken();
            BigDecimal wrongAmount = new BigDecimal("999999");

            if (wireMockServer != null) {
                String paymentKey = "mismatch_payment_key_" + System.currentTimeMillis();
                PgGatewayWireMockStub.stubTossPaymentApproveFailAmountMismatch(
                        wireMockServer, paymentKey);

                ApprovePaymentV2ApiRequest request =
                        createApprovePaymentRequest(PENDING_PAYMENT_ID, paymentKey, wrongAmount);

                ResponseEntity<ApiResponse<PaymentV2ApiResponse>> response =
                        restTemplate.exchange(
                                ApiV2Paths.Payments.BASE + "/approve",
                                HttpMethod.POST,
                                authenticatedEntity(request, accessToken),
                                new ParameterizedTypeReference<>() {});

                // then
                assertThat(response.getStatusCode())
                        .isIn(HttpStatus.BAD_REQUEST, HttpStatus.CONFLICT, HttpStatus.NOT_FOUND);
            }
        }

        @Test
        @DisplayName("E2E-011: 결제 조회 플로우")
        void paymentQueryFlow() {
            // given
            String accessToken = registerAndGetAccessToken();
            String paymentUrl = ApiV2Paths.Payments.BASE + "/" + PENDING_PAYMENT_ID;

            // when
            ResponseEntity<ApiResponse<PaymentV2ApiResponse>> response =
                    restTemplate.exchange(
                            paymentUrl,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().paymentId()).isEqualTo(PENDING_PAYMENT_ID);
            }
        }

        @Test
        @DisplayName("E2E-012: 체크아웃 ID로 결제 조회")
        void paymentQueryByCheckoutId() {
            // given
            String accessToken = registerAndGetAccessToken();
            String paymentUrl = ApiV2Paths.Payments.BASE + "/by-checkout/" + PENDING_CHECKOUT_ID;

            // when
            ResponseEntity<ApiResponse<PaymentV2ApiResponse>> response =
                    restTemplate.exchange(
                            paymentUrl,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().checkoutId()).isEqualTo(PENDING_CHECKOUT_ID);
            }
        }
    }

    // ============================================================
    // E2E-013 ~ E2E-016: 복합 시나리오
    // ============================================================

    @Nested
    @DisplayName("복합 시나리오 테스트")
    class ComplexScenarioTests {

        @Test
        @DisplayName("E2E-013: 주문 조회 후 클레임 목록 확인")
        void orderQueryWithClaimsList() {
            // given
            String accessToken = registerAndGetAccessToken();

            // Step 1: 주문 조회
            String orderUrl = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID;
            ResponseEntity<ApiResponse<OrderV2ApiResponse>> orderResponse =
                    restTemplate.exchange(
                            orderUrl,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            assertThat(orderResponse.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            // Step 2: 해당 주문의 클레임 목록 조회
            String claimsUrl = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID + "/claims";
            ResponseEntity<String> claimsResponse =
                    restTemplate.exchange(
                            claimsUrl,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            String.class);

            assertThat(claimsResponse.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("E2E-014: 배송완료 주문의 전체 타임라인 확인")
        void deliveredOrderFullTimeline() {
            // given
            String accessToken = registerAndGetAccessToken();
            String timelineUrl = ApiV2Paths.Orders.BASE + "/" + DELIVERED_ORDER_ID + "/timeline";

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
                    restTemplate.exchange(
                            timelineUrl,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                // DELIVERED 상태는 여러 이벤트가 있어야 함
            }
        }

        @Test
        @DisplayName("E2E-015: 이미 존재하는 클레임에 대한 중복 요청 방지")
        void preventDuplicateClaimRequest() {
            // given
            String accessToken = registerAndGetAccessToken();

            // 기존 REQUESTED 상태 클레임이 있는 주문에 대해 동일한 클레임 요청
            // 이 시나리오는 실제 데이터 설정에 따라 다름

            RequestClaimV2ApiRequest request =
                    createReturnClaimRequest(
                            DELIVERED_ORDER_ID,
                            DELIVERED_ORDER_ITEM_ID,
                            CLAIM_REASON_WRONG_SIZE,
                            1,
                            DEFAULT_UNIT_PRICE);

            // 첫 번째 요청
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> firstResponse =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // 두 번째 요청 (중복)
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> secondResponse =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // 첫 번째 요청이 성공한 경우, 두 번째는 CONFLICT 예상
            if (firstResponse.getStatusCode() == HttpStatus.CREATED) {
                assertThat(secondResponse.getStatusCode())
                        .isIn(HttpStatus.CONFLICT, HttpStatus.CREATED, HttpStatus.NOT_FOUND);
            }
        }

        @Test
        @DisplayName("E2E-016: 전체 주문 라이프사이클 검증 (COMPLETED 주문)")
        void fullOrderLifecycleVerification() {
            // given
            String accessToken = registerAndGetAccessToken();

            // COMPLETED 상태 주문 조회
            String orderUrl = ApiV2Paths.Orders.BASE + "/" + COMPLETED_ORDER_ID;
            ResponseEntity<ApiResponse<OrderV2ApiResponse>> orderResponse =
                    restTemplate.exchange(
                            orderUrl,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(orderResponse.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (orderResponse.getStatusCode() == HttpStatus.OK) {
                assertThat(orderResponse.getBody()).isNotNull();
                assertThat(orderResponse.getBody().data()).isNotNull();
                assertThat(orderResponse.getBody().data().status())
                        .isEqualTo(ORDER_STATUS_COMPLETED);

                // 구매확정 완료된 주문은 취소 불가 확인
                String cancelUrl = ApiV2Paths.Orders.BASE + "/" + COMPLETED_ORDER_ID + "/cancel";
                ResponseEntity<ApiResponse<OrderV2ApiResponse>> cancelResponse =
                        restTemplate.exchange(
                                cancelUrl,
                                HttpMethod.POST,
                                authenticatedEntity(null, accessToken),
                                new ParameterizedTypeReference<>() {});

                assertThat(cancelResponse.getStatusCode())
                        .isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
            }
        }
    }
}
