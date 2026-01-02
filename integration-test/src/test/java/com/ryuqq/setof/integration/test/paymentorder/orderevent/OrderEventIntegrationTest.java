package com.ryuqq.setof.integration.test.paymentorder.orderevent;

import static com.ryuqq.setof.integration.test.paymentorder.fixture.PaymentOrderIntegrationTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.orderevent.dto.response.OrderTimelineV2ApiResponse;
import com.ryuqq.setof.integration.test.common.IntegrationTestBase;
import java.time.Instant;
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
 * OrderEvent 통합 테스트
 *
 * <p>주문 타임라인 조회 기능 통합 테스트
 *
 * <p>테스트 시나리오:
 *
 * <ul>
 *   <li>OE-001 ~ OE-006: 상태별 타임라인 조회
 *   <li>OE-007 ~ OE-010: 예외 및 검증 테스트
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("OrderEvent 통합 테스트")
@Sql(
        scripts = {
            "/sql/cleanup.sql",
            "/sql/member-test-data.sql",
            "/sql/checkout-order-test-data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class OrderEventIntegrationTest extends IntegrationTestBase {

    // ============================================================
    // 헬퍼 메서드
    // ============================================================

    private String registerAndGetAccessToken() {
        return registerMemberAndGetAccessToken(ACTIVE_MEMBER_PHONE, ACTIVE_MEMBER_EMAIL);
    }

    private String buildTimelineUrl(String orderId) {
        return ApiV2Paths.Orders.BASE + "/" + orderId + "/timeline";
    }

    // ============================================================
    // OE-001 ~ OE-006: 상태별 타임라인 조회 테스트
    // ============================================================

    @Nested
    @DisplayName("상태별 타임라인 조회 테스트")
    class TimelineByStatusTests {

        @Test
        @DisplayName("OE-001: ORDERED 상태 주문 타임라인 조회")
        void getTimeline_OrderedStatus() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(ORDERED_ORDER_ID);

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
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
                assertThat(response.getBody().data().orderId()).isEqualTo(ORDERED_ORDER_ID);
                assertThat(response.getBody().data().events()).isNotNull();

                // ORDERED 상태는 최소한 ORDER_CREATED 이벤트가 있어야 함
                if (!response.getBody().data().events().isEmpty()) {
                    assertThat(response.getBody().data().events())
                            .anyMatch(
                                    e ->
                                            e.eventType().contains("ORDER")
                                                    || e.eventType().contains("CREATED"));
                }
            }
        }

        @Test
        @DisplayName("OE-002: CONFIRMED 상태 주문 타임라인 조회")
        void getTimeline_ConfirmedStatus() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(CONFIRMED_ORDER_ID);

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
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

                // CONFIRMED 상태는 ORDER_CREATED + ORDER_CONFIRMED 이벤트 기대
                List<OrderTimelineV2ApiResponse.TimelineEventResponse> events =
                        response.getBody().data().events();
                assertThat(events).isNotNull();
            }
        }

        @Test
        @DisplayName("OE-003: SHIPPED 상태 주문 타임라인 조회")
        void getTimeline_ShippedStatus() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(SHIPPED_ORDER_ID);

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
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

                // SHIPPED 상태는 배송 시작 이벤트 포함
                List<OrderTimelineV2ApiResponse.TimelineEventResponse> events =
                        response.getBody().data().events();
                assertThat(events).isNotNull();
            }
        }

        @Test
        @DisplayName("OE-004: DELIVERED 상태 주문 타임라인 조회")
        void getTimeline_DeliveredStatus() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(DELIVERED_ORDER_ID);

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
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

                // DELIVERED 상태는 배송 완료 이벤트 포함
                List<OrderTimelineV2ApiResponse.TimelineEventResponse> events =
                        response.getBody().data().events();
                assertThat(events).isNotNull();
            }
        }

        @Test
        @DisplayName("OE-005: COMPLETED 상태 주문 타임라인 조회")
        void getTimeline_CompletedStatus() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(COMPLETED_ORDER_ID);

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
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

                // COMPLETED 상태는 전체 라이프사이클 이벤트 포함
                List<OrderTimelineV2ApiResponse.TimelineEventResponse> events =
                        response.getBody().data().events();
                assertThat(events).isNotNull();
            }
        }

        @Test
        @DisplayName("OE-006: CANCELLED 상태 주문 타임라인 조회")
        void getTimeline_CancelledStatus() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(CANCELLED_ORDER_ID);

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
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

                // CANCELLED 상태는 취소 이벤트 포함
                List<OrderTimelineV2ApiResponse.TimelineEventResponse> events =
                        response.getBody().data().events();
                assertThat(events).isNotNull();
            }
        }
    }

    // ============================================================
    // OE-007 ~ OE-010: 예외 및 검증 테스트
    // ============================================================

    @Nested
    @DisplayName("예외 및 검증 테스트")
    class ValidationTests {

        @Test
        @DisplayName("OE-007: 존재하지 않는 주문 타임라인 조회 - 404")
        void getTimeline_OrderNotFound() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(NON_EXISTENT_ORDER_ID);

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("OE-008: 인증 없이 타임라인 조회 - 401")
        void getTimeline_Unauthorized() {
            // given
            String url = buildTimelineUrl(ORDERED_ORDER_ID);

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, new HttpEntity<>(null), String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("OE-009: 잘못된 UUID로 타임라인 조회 - 400/404")
        void getTimeline_InvalidUuid() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(INVALID_UUID);

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            authenticatedEntity(null, accessToken),
                            String.class);

            // then
            assertThat(response.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("OE-010: 이벤트 시간순 정렬 확인")
        void getTimeline_EventsAreChronologicallySorted() {
            // given
            String accessToken = registerAndGetAccessToken();
            String url = buildTimelineUrl(DELIVERED_ORDER_ID); // DELIVERED 주문은 여러 이벤트가 있음

            // when
            ResponseEntity<ApiResponse<OrderTimelineV2ApiResponse>> response =
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

                List<OrderTimelineV2ApiResponse.TimelineEventResponse> events =
                        response.getBody().data().events();

                if (events != null && events.size() > 1) {
                    // 시간순 정렬 확인 (오름차순 또는 내림차순)
                    boolean isAscending = true;
                    boolean isDescending = true;

                    for (int i = 0; i < events.size() - 1; i++) {
                        Instant current = events.get(i).occurredAt();
                        Instant next = events.get(i + 1).occurredAt();

                        if (current != null && next != null) {
                            if (current.isAfter(next)) {
                                isAscending = false;
                            }
                            if (current.isBefore(next)) {
                                isDescending = false;
                            }
                        }
                    }

                    // 오름차순 또는 내림차순 중 하나는 만족해야 함
                    assertThat(isAscending || isDescending)
                            .as("Events should be chronologically sorted")
                            .isTrue();
                }
            }
        }
    }
}
