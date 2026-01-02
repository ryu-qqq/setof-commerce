package com.ryuqq.setof.integration.test.discount;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.RegisterDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountTargetsV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.response.DiscountPolicyV2ApiResponse;
import com.ryuqq.setof.integration.test.common.AdminIntegrationTestBase;
import com.ryuqq.setof.integration.test.discount.fixture.DiscountIntegrationTestFixture;
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
 * Discount Policy Integration Test
 *
 * <p>할인 정책 Admin API의 통합 테스트를 수행합니다.
 *
 * <h3>테스트 시나리오</h3>
 *
 * <ul>
 *   <li>DIS-001~002: 할인 정책 등록 (정률/정액)
 *   <li>DIS-003~004: 유효성 검증 실패 (필수값 누락, 잘못된 할인율)
 *   <li>DIS-005~008: 조회 테스트 (목록, 유효 정책, 상세, 404)
 *   <li>DIS-009~010: 수정 테스트 (정보 수정, 대상 수정)
 *   <li>DIS-011~013: 상태 변경 테스트 (활성화, 비활성화, 삭제)
 * </ul>
 *
 * @since 1.0.0
 */
@DisplayName("Discount Policy Integration Test")
class DiscountPolicyIntegrationTest extends AdminIntegrationTestBase {

    // ============================================================
    // 등록 테스트
    // ============================================================

    @Nested
    @DisplayName("할인 정책 등록")
    class RegisterDiscountPolicyTest {

        @Test
        @DisplayName("DIS-001: 정률 할인 정책을 등록한다")
        @Sql(
                scripts = "classpath:sql/discount/discount-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void shouldRegisterRateDiscountPolicy() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID);
            RegisterDiscountPolicyV2ApiRequest request =
                    DiscountIntegrationTestFixture.createRateDiscountRequest("신규 정률 할인 20%", 20);

            // when
            ResponseEntity<ApiResponse<Long>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            jsonEntity(request),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isPositive();
        }

        @Test
        @DisplayName("DIS-002: 정액 할인 정책을 등록한다")
        @Sql(
                scripts = "classpath:sql/discount/discount-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void shouldRegisterFixedDiscountPolicy() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID);
            RegisterDiscountPolicyV2ApiRequest request =
                    DiscountIntegrationTestFixture.createFixedDiscountRequest(
                            "신규 정액 할인 10000원", 10000L);

            // when
            ResponseEntity<ApiResponse<Long>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            jsonEntity(request),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isPositive();
        }

        @Test
        @DisplayName("DIS-003: 필수값 누락 시 등록 실패한다")
        @Sql(
                scripts = "classpath:sql/discount/discount-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void shouldFailWhenMissingRequiredField() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID);
            RegisterDiscountPolicyV2ApiRequest request =
                    DiscountIntegrationTestFixture.createInvalidRequestMissingPolicyName();

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, jsonEntity(request), String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("DIS-004: 잘못된 할인율로 등록 실패한다 (음수)")
        @Sql(
                scripts = "classpath:sql/discount/discount-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void shouldFailWhenNegativeDiscountRate() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID);
            RegisterDiscountPolicyV2ApiRequest request =
                    DiscountIntegrationTestFixture.createInvalidRequestNegativeRate();

            // when
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, jsonEntity(request), String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ============================================================
    // 조회 테스트
    // ============================================================

    @Nested
    @DisplayName("할인 정책 조회")
    @Sql(
            scripts = "classpath:sql/discount/discount-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class GetDiscountPolicyTest {

        @Test
        @DisplayName("DIS-005: 할인 정책 목록을 조회한다")
        void shouldReturnDiscountPolicies() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID);

            // when
            ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<DiscountPolicyV2ApiResponse> policies = response.getBody().data();
            // 삭제된 정책(ID:4)은 제외되므로 활성 정책들만 조회됨
            assertThat(policies).isNotEmpty();
        }

        @Test
        @DisplayName("DIS-006: 현재 유효한 할인 정책을 조회한다")
        void shouldReturnValidDiscountPolicies() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.validDiscountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID);

            // when
            ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<DiscountPolicyV2ApiResponse> policies = response.getBody().data();
            // 유효한 정책은 활성 + 유효 기간 내 (ID 1, 2만 해당)
            assertThat(policies).hasSize(DiscountIntegrationTestFixture.TOTAL_VALID_POLICIES);
        }

        @Test
        @DisplayName("DIS-007: 할인 정책 상세를 조회한다")
        void shouldReturnDiscountPolicyDetail() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPolicyDetailPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.RATE_DISCOUNT_ACTIVE_ID);

            // when
            ResponseEntity<ApiResponse<DiscountPolicyV2ApiResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            DiscountPolicyV2ApiResponse policy = response.getBody().data();
            assertThat(policy.policyName())
                    .isEqualTo(DiscountIntegrationTestFixture.RATE_DISCOUNT_ACTIVE_NAME);
            assertThat(policy.discountType())
                    .isEqualTo(DiscountIntegrationTestFixture.DISCOUNT_TYPE_RATE);
        }

        @Test
        @DisplayName("DIS-008: 존재하지 않는 할인 정책 조회 시 404를 반환한다")
        void shouldReturn404WhenPolicyNotFound() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPolicyDetailPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.NON_EXISTENT_DISCOUNT_ID);

            // when - 인증 헤더 포함 요청
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url, HttpMethod.GET, new HttpEntity<>(jsonHeaders()), String.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // 수정 테스트
    // ============================================================

    @Nested
    @DisplayName("할인 정책 수정")
    @Sql(
            scripts = "classpath:sql/discount/discount-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class UpdateDiscountPolicyTest {

        @Test
        @DisplayName("DIS-009: 할인 정책 정보를 수정한다")
        void shouldUpdateDiscountPolicy() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPolicyDetailPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.RATE_DISCOUNT_ACTIVE_ID);
            UpdateDiscountPolicyV2ApiRequest request =
                    DiscountIntegrationTestFixture.createUpdateRequest("수정된 할인 정책명", 15000L);

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PUT,
                            jsonEntity(request),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            // 수정 결과 확인
            ResponseEntity<ApiResponse<DiscountPolicyV2ApiResponse>> getResponse =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});
            DiscountPolicyV2ApiResponse policy = getResponse.getBody().data();
            assertThat(policy.policyName()).isEqualTo("수정된 할인 정책명");
        }

        @Test
        @DisplayName("DIS-010: 할인 대상을 수정한다")
        void shouldUpdateDiscountTargets() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountTargetsPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.RATE_DISCOUNT_ACTIVE_ID);
            UpdateDiscountTargetsV2ApiRequest request =
                    DiscountIntegrationTestFixture.createUpdateTargetsRequest(
                            List.of(100L, 200L, 300L));

            // when - PUT 메서드 사용 (PATCH가 아님)
            ResponseEntity<ApiResponse<Void>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PUT,
                            jsonEntity(request),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }
    }

    // ============================================================
    // 상태 변경 테스트
    // ============================================================

    @Nested
    @DisplayName("할인 정책 상태 변경")
    @Sql(
            scripts = "classpath:sql/discount/discount-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class ChangeDiscountPolicyStatusTest {

        @Test
        @DisplayName("DIS-011: 비활성 상태의 할인 정책을 활성화한다")
        void shouldActivateDiscountPolicy() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.activateDiscountPolicyPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.INACTIVE_DISCOUNT_ID);

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            // 활성화 결과 확인
            String detailUrl =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPolicyDetailPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.INACTIVE_DISCOUNT_ID);
            ResponseEntity<ApiResponse<DiscountPolicyV2ApiResponse>> getResponse =
                    restTemplate.exchange(
                            detailUrl,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});
            DiscountPolicyV2ApiResponse policy = getResponse.getBody().data();
            assertThat(policy.isActive()).isTrue();
        }

        @Test
        @DisplayName("DIS-012: 활성 상태의 할인 정책을 비활성화한다")
        void shouldDeactivateDiscountPolicy() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.deactivateDiscountPolicyPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.RATE_DISCOUNT_ACTIVE_ID);

            // when
            ResponseEntity<ApiResponse<Void>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            // 비활성화 결과 확인
            String detailUrl =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPolicyDetailPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.RATE_DISCOUNT_ACTIVE_ID);
            ResponseEntity<ApiResponse<DiscountPolicyV2ApiResponse>> getResponse =
                    restTemplate.exchange(
                            detailUrl,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});
            DiscountPolicyV2ApiResponse policy = getResponse.getBody().data();
            assertThat(policy.isActive()).isFalse();
        }

        @Test
        @DisplayName("DIS-013: 할인 정책을 삭제한다 (Soft Delete)")
        void shouldDeleteDiscountPolicy() {
            // given - Soft Delete 경로 사용 (PATCH /{id}/delete)
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.deleteDiscountPolicyPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.FIXED_DISCOUNT_ACTIVE_ID);

            // when - PATCH 메서드 사용 (DELETE가 아님)
            ResponseEntity<ApiResponse<Void>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            // 삭제 후 조회 시 404
            String detailUrl =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPolicyDetailPath(
                                    DiscountIntegrationTestFixture.SELLER_ID,
                                    DiscountIntegrationTestFixture.FIXED_DISCOUNT_ACTIVE_ID);
            ResponseEntity<String> getResponse =
                    restTemplate.exchange(
                            detailUrl,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            String.class);
            assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================================
    // 쿼리 파라미터 테스트
    // ============================================================

    @Nested
    @DisplayName("할인 정책 쿼리 파라미터")
    @Sql(
            scripts = "classpath:sql/discount/discount-test-data.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class QueryParameterTest {

        @Test
        @DisplayName("DIS-014: activeOnly 파라미터로 활성 정책만 조회한다")
        void shouldFilterByActiveOnly() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID)
                            + "?activeOnly=true";

            // when
            ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<DiscountPolicyV2ApiResponse> policies = response.getBody().data();
            // 모든 결과가 활성 상태인지 확인
            assertThat(policies).allMatch(DiscountPolicyV2ApiResponse::isActive);
        }

        @Test
        @DisplayName("DIS-015: validOnly 파라미터로 유효 기간 내 정책만 조회한다")
        void shouldFilterByValidOnly() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID)
                            + "?validOnly=true";

            // when
            ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<DiscountPolicyV2ApiResponse> policies = response.getBody().data();
            assertThat(policies).isNotEmpty();
        }

        @Test
        @DisplayName("DIS-016: includeDeleted 파라미터로 삭제된 정책도 조회한다")
        void shouldIncludeDeletedPolicies() {
            // given
            String url =
                    baseUrl()
                            + DiscountIntegrationTestFixture.discountPoliciesPath(
                                    DiscountIntegrationTestFixture.SELLER_ID)
                            + "?includeDeleted=true";

            // when
            ResponseEntity<ApiResponse<List<DiscountPolicyV2ApiResponse>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(jsonHeaders()),
                            new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<DiscountPolicyV2ApiResponse> policies = response.getBody().data();
            // 삭제된 정책이 포함되어야 함 (ID:4)
            assertThat(policies).isNotEmpty();
        }
    }
}
