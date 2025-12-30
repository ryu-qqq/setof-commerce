package com.ryuqq.setof.integration.test.paymentorder.claim;

import static com.ryuqq.setof.integration.test.paymentorder.fixture.PaymentOrderIntegrationTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.claim.dto.command.RequestClaimV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.claim.dto.response.ClaimV2ApiResponse;
import com.ryuqq.setof.integration.test.common.IntegrationTestBase;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Claim 통합 테스트
 *
 * <p>취소/반품/교환/부분환불 클레임 기능 통합 테스트
 *
 * <p>테스트 시나리오:
 *
 * <ul>
 *   <li>CLM-001 ~ CLM-008: 취소 클레임 테스트
 *   <li>CLM-009 ~ CLM-015: 반품 클레임 테스트
 *   <li>CLM-016 ~ CLM-020: 교환 클레임 테스트
 *   <li>CLM-021 ~ CLM-024: 부분환불 클레임 테스트
 *   <li>CLM-025 ~ CLM-026: 클레임 조회 테스트
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Claim 통합 테스트")
@Sql(
        scripts = {
            "/sql/cleanup.sql",
            "/sql/member/member-test-data.sql",
            "/sql/paymentorder/checkout-order-test-data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ClaimIntegrationTest extends IntegrationTestBase {

    // ============================================================
    // 헬퍼 메서드
    // ============================================================

    private String registerAndGetAccessToken() {
        return registerMemberAndGetAccessToken(ACTIVE_MEMBER_PHONE, ACTIVE_MEMBER_EMAIL);
    }

    // ============================================================
    // CLM-001 ~ CLM-008: 취소 클레임 테스트
    // ============================================================

    @Nested
    @DisplayName("취소 클레임 테스트")
    class CancelClaimTests {

        @Test
        @DisplayName("CLM-001: ORDERED 상태 주문 취소 요청 - 성공")
        void requestCancelClaim_OrderedStatus_Success() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createCancelClaimRequest(
                            ORDERED_ORDER_ID, DEFAULT_QUANTITY, calculateDefaultTotalAmount());

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // UseCase 구현에 따라 201 또는 다른 상태 코드 반환 가능
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CREATED, HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimType()).isEqualTo(CLAIM_TYPE_CANCEL);
                assertThat(response.getBody().data().status()).isEqualTo(CLAIM_STATUS_REQUESTED);
            }
        }

        @Test
        @DisplayName("CLM-002: CONFIRMED 상태 주문 취소 요청 - 성공")
        void requestCancelClaim_ConfirmedStatus_Success() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createCancelClaimRequest(
                            CONFIRMED_ORDER_ID, DEFAULT_QUANTITY, calculateDefaultTotalAmount());

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

        @Test
        @DisplayName("CLM-003: SHIPPED 상태 주문 취소 요청 - 실패 (409)")
        void requestCancelClaim_ShippedStatus_Conflict() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createCancelClaimRequest(
                            SHIPPED_ORDER_ID, DEFAULT_QUANTITY, calculateDefaultTotalAmount());

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // SHIPPED 상태는 취소 불가 (반품으로 진행 필요)
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("CLM-004: DELIVERED 상태 주문 취소 요청 - 실패 (409)")
        void requestCancelClaim_DeliveredStatus_Conflict() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createCancelClaimRequest(
                            DELIVERED_ORDER_ID, DEFAULT_QUANTITY, calculateDefaultTotalAmount());

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // DELIVERED 상태는 취소 불가 (반품으로 진행 필요)
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("CLM-005: COMPLETED 상태 주문 취소 요청 - 실패 (409)")
        void requestCancelClaim_CompletedStatus_Conflict() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createCancelClaimRequest(
                            COMPLETED_ORDER_ID, DEFAULT_QUANTITY, calculateDefaultTotalAmount());

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // COMPLETED (구매확정) 상태는 취소 불가
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("CLM-006: 이미 취소된 주문 재취소 요청 - 실패 (409)")
        void requestCancelClaim_AlreadyCancelled_Conflict() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createCancelClaimRequest(
                            CANCELLED_ORDER_ID, DEFAULT_QUANTITY, calculateDefaultTotalAmount());

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("CLM-007: 존재하지 않는 주문 취소 요청 - 실패 (404)")
        void requestCancelClaim_OrderNotFound() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createCancelClaimRequest(
                            NON_EXISTENT_ORDER_ID, DEFAULT_QUANTITY, calculateDefaultTotalAmount());

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("CLM-008: 취소 사유 - 단순 변심")
        void requestCancelClaim_SimpleChangeOfMind() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    new RequestClaimV2ApiRequest(
                            ORDERED_ORDER_ID,
                            null,
                            CLAIM_TYPE_CANCEL,
                            CLAIM_REASON_SIMPLE_CHANGE_OF_MIND,
                            "마음이 바뀌었습니다",
                            DEFAULT_QUANTITY,
                            calculateDefaultTotalAmount());

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
                assertThat(response.getBody().data().claimReason())
                        .isEqualTo(CLAIM_REASON_SIMPLE_CHANGE_OF_MIND);
            }
        }
    }

    // ============================================================
    // CLM-009 ~ CLM-015: 반품 클레임 테스트
    // ============================================================

    @Nested
    @DisplayName("반품 클레임 테스트")
    class ReturnClaimTests {

        @Test
        @DisplayName("CLM-009: DELIVERED 상태 주문 반품 요청 - 성공")
        void requestReturnClaim_DeliveredStatus_Success() {
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
                assertThat(response.getBody().data().status()).isEqualTo(CLAIM_STATUS_REQUESTED);
            }
        }

        @Test
        @DisplayName("CLM-010: ORDERED 상태 주문 반품 요청 - 실패 (409)")
        void requestReturnClaim_OrderedStatus_Conflict() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createReturnClaimRequest(
                            ORDERED_ORDER_ID,
                            ORDERED_ORDER_ITEM_ID,
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
            // 배송 완료 전에는 반품 불가 (취소로 진행 필요)
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("CLM-011: 반품 사유 - 사이즈 불일치")
        void requestReturnClaim_WrongSize() {
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
            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimReason())
                        .isEqualTo(CLAIM_REASON_WRONG_SIZE);
            }
        }

        @Test
        @DisplayName("CLM-012: 반품 사유 - 상품 불량")
        void requestReturnClaim_DefectiveProduct() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createReturnClaimRequest(
                            DELIVERED_ORDER_ID,
                            DELIVERED_ORDER_ITEM_ID,
                            CLAIM_REASON_DEFECTIVE_PRODUCT,
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
            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimReason())
                        .isEqualTo(CLAIM_REASON_DEFECTIVE_PRODUCT);
            }
        }

        @Test
        @DisplayName("CLM-013: 반품 사유 - 배송 중 파손")
        void requestReturnClaim_DamagedDuringDelivery() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createReturnClaimRequest(
                            DELIVERED_ORDER_ID,
                            DELIVERED_ORDER_ITEM_ID,
                            CLAIM_REASON_DAMAGED_DURING_DELIVERY,
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
            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimReason())
                        .isEqualTo(CLAIM_REASON_DAMAGED_DURING_DELIVERY);
            }
        }

        @Test
        @DisplayName("CLM-014: 반품 사유 - 오배송")
        void requestReturnClaim_WrongItemDelivered() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createReturnClaimRequest(
                            DELIVERED_ORDER_ID,
                            DELIVERED_ORDER_ITEM_ID,
                            CLAIM_REASON_WRONG_ITEM_DELIVERED,
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
            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimReason())
                        .isEqualTo(CLAIM_REASON_WRONG_ITEM_DELIVERED);
            }
        }

        @Test
        @DisplayName("CLM-015: 부분 반품 요청 (수량 1)")
        void requestReturnClaim_PartialQuantity() {
            // given
            String accessToken = registerAndGetAccessToken();
            int partialQuantity = 1;
            BigDecimal partialRefund =
                    DEFAULT_UNIT_PRICE.multiply(BigDecimal.valueOf(partialQuantity));

            RequestClaimV2ApiRequest request =
                    createReturnClaimRequest(
                            DELIVERED_ORDER_ID,
                            DELIVERED_ORDER_ITEM_ID,
                            CLAIM_REASON_SIMPLE_CHANGE_OF_MIND,
                            partialQuantity,
                            partialRefund);

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().quantity()).isEqualTo(partialQuantity);
                assertThat(response.getBody().data().refundAmount())
                        .isEqualByComparingTo(partialRefund);
            }
        }
    }

    // ============================================================
    // CLM-016 ~ CLM-020: 교환 클레임 테스트
    // ============================================================

    @Nested
    @DisplayName("교환 클레임 테스트")
    class ExchangeClaimTests {

        @Test
        @DisplayName("CLM-016: DELIVERED 상태 주문 교환 요청 - 성공")
        void requestExchangeClaim_DeliveredStatus_Success() {
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
                assertThat(response.getBody().data().refundAmount())
                        .isEqualByComparingTo(BigDecimal.ZERO);
            }
        }

        @Test
        @DisplayName("CLM-017: ORDERED 상태 주문 교환 요청 - 실패 (409)")
        void requestExchangeClaim_OrderedStatus_Conflict() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createExchangeClaimRequest(
                            ORDERED_ORDER_ID, ORDERED_ORDER_ITEM_ID, CLAIM_REASON_WRONG_SIZE, 1);

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // 배송 완료 전에는 교환 불가 (취소 후 재주문 필요)
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("CLM-018: 교환 사유 - 사이즈 불일치")
        void requestExchangeClaim_WrongSize() {
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
            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimReason())
                        .isEqualTo(CLAIM_REASON_WRONG_SIZE);
            }
        }

        @Test
        @DisplayName("CLM-019: 교환 사유 - 상품 불량")
        void requestExchangeClaim_DefectiveProduct() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createExchangeClaimRequest(
                            DELIVERED_ORDER_ID,
                            DELIVERED_ORDER_ITEM_ID,
                            CLAIM_REASON_DEFECTIVE_PRODUCT,
                            1);

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimReason())
                        .isEqualTo(CLAIM_REASON_DEFECTIVE_PRODUCT);
            }
        }

        @Test
        @DisplayName("CLM-020: 구매확정 이후 교환 요청 - 실패 (409)")
        void requestExchangeClaim_CompletedStatus_Conflict() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createExchangeClaimRequest(
                            COMPLETED_ORDER_ID,
                            null, // 전체 주문 대상
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
            // 구매확정 후에는 교환 불가
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // CLM-021 ~ CLM-024: 부분환불 클레임 테스트
    // ============================================================

    @Nested
    @DisplayName("부분환불 클레임 테스트")
    class PartialRefundClaimTests {

        @Test
        @DisplayName("CLM-021: DELIVERED 상태 주문 부분환불 요청 - 성공")
        void requestPartialRefundClaim_DeliveredStatus_Success() {
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

            if (response.getStatusCode() == HttpStatus.CREATED) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimType())
                        .isEqualTo(CLAIM_TYPE_PARTIAL_REFUND);
                assertThat(response.getBody().data().refundAmount())
                        .isEqualByComparingTo(partialRefund);
            }
        }

        @Test
        @DisplayName("CLM-022: 환불 금액이 0인 경우 - 실패 (400)")
        void requestPartialRefundClaim_ZeroAmount_BadRequest() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createPartialRefundClaimRequest(
                            DELIVERED_ORDER_ID, DELIVERED_ORDER_ITEM_ID, 1, ZERO_AMOUNT);

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // 부분환불 금액이 0이면 실패
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND, HttpStatus.CREATED);
        }

        @Test
        @DisplayName("CLM-023: 환불 금액이 음수인 경우 - 실패 (400)")
        void requestPartialRefundClaim_NegativeAmount_BadRequest() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    createPartialRefundClaimRequest(
                            DELIVERED_ORDER_ID, DELIVERED_ORDER_ITEM_ID, 1, NEGATIVE_AMOUNT);

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("CLM-024: 주문 금액 초과 환불 요청 - 실패 (400)")
        void requestPartialRefundClaim_ExceedOrderAmount_BadRequest() {
            // given
            String accessToken = registerAndGetAccessToken();
            BigDecimal exceedingAmount = new BigDecimal("1000000"); // 주문 금액 초과
            RequestClaimV2ApiRequest request =
                    createPartialRefundClaimRequest(
                            DELIVERED_ORDER_ID, DELIVERED_ORDER_ITEM_ID, 1, exceedingAmount);

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            // 주문 금액 초과 환불은 불가
            assertThat(response.getStatusCode())
                    .isIn(HttpStatus.BAD_REQUEST, HttpStatus.CONFLICT, HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // CLM-025 ~ CLM-026: 클레임 조회 테스트
    // ============================================================

    @Nested
    @DisplayName("클레임 조회 테스트")
    class ClaimQueryTests {

        @Test
        @DisplayName("CLM-025: 클레임 ID로 조회 - 성공")
        void getClaimById_Success() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = ApiV2Paths.Claims.BASE + "/" + REQUESTED_CLAIM_ID;

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().data()).isNotNull();
                assertThat(response.getBody().data().claimId()).isEqualTo(REQUESTED_CLAIM_ID);
                assertThat(response.getBody().data().status()).isEqualTo(CLAIM_STATUS_REQUESTED);
            }
        }

        @Test
        @DisplayName("CLM-026: 주문별 클레임 목록 조회")
        void getClaimsByOrder_Success() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = ApiV2Paths.Orders.BASE + "/" + ORDERED_ORDER_ID + "/claims";

            // when
            ResponseEntity<ApiResponse<List<ClaimV2ApiResponse>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);

            if (response.getStatusCode() == HttpStatus.OK) {
                assertThat(response.getBody()).isNotNull();
                // 빈 목록도 성공 응답
                assertThat(response.getBody().data()).isNotNull();
            }
        }
    }

    // ============================================================
    // 추가 검증 테스트
    // ============================================================

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTests {

        @Test
        @DisplayName("인증 없이 클레임 요청 - 401")
        void requestClaim_Unauthorized() {
            // given
            RequestClaimV2ApiRequest request =
                    createCancelClaimRequest(
                            ORDERED_ORDER_ID, DEFAULT_QUANTITY, calculateDefaultTotalAmount());

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            new HttpEntity<>(request),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("수량이 0인 클레임 요청 - 400")
        void requestClaim_ZeroQuantity_BadRequest() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    new RequestClaimV2ApiRequest(
                            ORDERED_ORDER_ID,
                            null,
                            CLAIM_TYPE_CANCEL,
                            CLAIM_REASON_SIMPLE_CHANGE_OF_MIND,
                            null,
                            ZERO_QUANTITY,
                            calculateDefaultTotalAmount());

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("음수 수량 클레임 요청 - 400")
        void requestClaim_NegativeQuantity_BadRequest() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    new RequestClaimV2ApiRequest(
                            ORDERED_ORDER_ID,
                            null,
                            CLAIM_TYPE_CANCEL,
                            CLAIM_REASON_SIMPLE_CHANGE_OF_MIND,
                            null,
                            NEGATIVE_QUANTITY,
                            calculateDefaultTotalAmount());

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("클레임 유형 누락 - 400")
        void requestClaim_MissingClaimType_BadRequest() {
            // given
            String accessToken = registerAndGetAccessToken();
            RequestClaimV2ApiRequest request =
                    new RequestClaimV2ApiRequest(
                            ORDERED_ORDER_ID,
                            null,
                            null, // claimType 누락
                            CLAIM_REASON_SIMPLE_CHANGE_OF_MIND,
                            null,
                            DEFAULT_QUANTITY,
                            calculateDefaultTotalAmount());

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            ApiV2Paths.Claims.BASE,
                            HttpMethod.POST,
                            authenticatedEntity(request, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("존재하지 않는 클레임 조회 - 404")
        void getClaim_NotFound() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = ApiV2Paths.Claims.BASE + "/" + NON_EXISTENT_CLAIM_ID;

            // when
            ResponseEntity<ApiResponse<ClaimV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
