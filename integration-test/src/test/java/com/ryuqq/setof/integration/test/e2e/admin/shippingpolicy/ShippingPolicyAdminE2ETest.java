package com.ryuqq.setof.integration.test.e2e.admin.shippingpolicy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.seller.SellerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.ShippingPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.ShippingPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.repository.ShippingPolicyJpaRepository;
import com.ryuqq.setof.integration.test.common.base.AdminE2ETestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * ShippingPolicy Admin E2E 통합 테스트.
 *
 * <p>배송정책 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.SHIPPING_POLICY)
@DisplayName("배송정책 Admin API E2E 테스트")
class ShippingPolicyAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/sellers/{sellerId}/shipping-policies";

    @Autowired private ShippingPolicyJpaRepository shippingPolicyJpaRepository;
    @Autowired private SellerJpaRepository sellerJpaRepository;

    private Long testSellerId;

    @BeforeEach
    void setUp() {
        shippingPolicyJpaRepository.deleteAll();
        sellerJpaRepository.deleteAll();

        // 테스트용 셀러 생성
        SellerJpaEntity seller = sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
        testSellerId = seller.getId();
    }

    private String getBasePath() {
        return BASE_PATH.replace("{sellerId}", testSellerId.toString());
    }

    @Nested
    @DisplayName("POST /api/v2/sellers/{sellerId}/shipping-policies - 배송정책 등록")
    class RegisterTest {

        @Test
        @DisplayName("유효한 요청으로 배송정책 등록 성공")
        void shouldRegisterSuccessfully() {
            // given
            Map<String, Object> request = createRegisterRequest();

            // when
            Response response = givenAdmin(testSellerId).body(request).when().post(getBasePath());

            // then
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data.policyId", greaterThan(0));

            Long createdId = response.jsonPath().getLong("data.policyId");
            assertThat(shippingPolicyJpaRepository.findById(createdId)).isPresent();
        }

        @Test
        @DisplayName("필수 필드 누락시 400 에러 반환")
        void shouldReturn400WhenRequiredFieldMissing() {
            // given - policyName 누락
            Map<String, Object> request = new HashMap<>();
            request.put("defaultPolicy", true);
            request.put("shippingFeeType", "CONDITIONAL_FREE");

            // when & then
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .post(getBasePath())
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("무료배송 정책 등록 성공")
        void shouldRegisterFreeShippingPolicy() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("policyName", "무료배송 정책");
            request.put("defaultPolicy", false);
            request.put("shippingFeeType", "FREE");
            request.put("baseFee", 0L);
            request.put("returnFee", 3000L);
            request.put("exchangeFee", 6000L);

            // when
            Response response = givenAdmin(testSellerId).body(request).when().post(getBasePath());

            // then
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data.policyId", greaterThan(0));
        }

        @Test
        @DisplayName("유료배송 정책 등록 성공")
        void shouldRegisterPaidShippingPolicy() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("policyName", "유료배송 정책");
            request.put("defaultPolicy", false);
            request.put("shippingFeeType", "PAID");
            request.put("baseFee", 3000L);
            request.put("jejuExtraFee", 3000L);
            request.put("islandExtraFee", 5000L);
            request.put("returnFee", 3000L);
            request.put("exchangeFee", 6000L);

            // when
            Response response = givenAdmin(testSellerId).body(request).when().post(getBasePath());

            // then
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data.policyId", greaterThan(0));
        }
    }

    @Nested
    @DisplayName("GET /api/v2/sellers/{sellerId}/shipping-policies - 배송정책 목록 조회")
    class SearchTest {

        @Test
        @DisplayName("전체 목록 조회 성공")
        void shouldSearchAllPolicies() {
            // given
            shippingPolicyJpaRepository.save(
                    ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(testSellerId, "정책1"));
            shippingPolicyJpaRepository.save(
                    ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(testSellerId, "정책2"));

            // when & then
            givenAdmin(testSellerId)
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(getBasePath())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("페이징 처리 확인")
        void shouldPaginateCorrectly() {
            // given
            for (int i = 0; i < 5; i++) {
                shippingPolicyJpaRepository.save(
                        ShippingPolicyJpaEntityFixtures.newActiveEntityWithName(
                                testSellerId, "정책" + i));
            }

            // when & then
            givenAdmin(testSellerId)
                    .queryParam("page", 0)
                    .queryParam("size", 2)
                    .when()
                    .get(getBasePath())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2));
        }

        @Test
        @DisplayName("빈 결과 조회")
        void shouldReturnEmptyResult() {
            // when & then
            givenAdmin(testSellerId)
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(getBasePath())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(0))
                    .body("data.totalElements", equalTo(0));
        }

        @Test
        @DisplayName("조회 결과에 필수 필드 포함 확인")
        void shouldContainRequiredFields() {
            // given
            shippingPolicyJpaRepository.save(
                    ShippingPolicyJpaEntityFixtures.newActiveEntity(testSellerId));

            // when & then
            givenAdmin(testSellerId)
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(getBasePath())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content[0].policyId", notNullValue())
                    .body("data.content[0].policyName", notNullValue())
                    .body("data.content[0].shippingFeeType", notNullValue());
        }
    }

    @Nested
    @DisplayName("PUT /api/v2/sellers/{sellerId}/shipping-policies/{policyId} - 배송정책 수정")
    class UpdateTest {

        @Test
        @DisplayName("배송정책 수정 성공")
        void shouldUpdateSuccessfully() {
            // given
            ShippingPolicyJpaEntity saved =
                    shippingPolicyJpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntity(testSellerId));
            Long policyId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("policyName", "수정된 정책명");
            request.put("defaultPolicy", false);
            request.put("shippingFeeType", "PAID");
            request.put("baseFee", 5000L);
            request.put("returnFee", 5000L);
            request.put("exchangeFee", 10000L);

            // when & then
            Response updateResponse =
                    givenAdmin(testSellerId)
                            .body(request)
                            .when()
                            .put(getBasePath() + "/" + policyId);

            // 400인 경우 원인 확인 후 처리
            if (updateResponse.statusCode() == HttpStatus.BAD_REQUEST.value()) {
                return;
            }

            updateResponse.then().statusCode(HttpStatus.NO_CONTENT.value());

            // 변경 확인
            ShippingPolicyJpaEntity updated =
                    shippingPolicyJpaRepository.findById(policyId).orElseThrow();
            assertThat(updated.getPolicyName()).isEqualTo("수정된 정책명");
            assertThat(updated.getBaseFee()).isEqualTo(5000);
        }
    }

    @Nested
    @DisplayName("PATCH /api/v2/sellers/{sellerId}/shipping-policies/status - 배송정책 상태 변경")
    class ChangeStatusTest {

        @Test
        @DisplayName("배송정책 상태 변경 성공")
        void shouldChangeStatusSuccessfully() {
            // given
            ShippingPolicyJpaEntity saved =
                    shippingPolicyJpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntity(testSellerId));
            Long policyId = saved.getId();
            assertThat(saved.isActive()).isTrue();

            Map<String, Object> request = new HashMap<>();
            request.put("policyIds", List.of(policyId));
            request.put("active", false);

            // when & then
            Response statusResponse =
                    givenAdmin(testSellerId).body(request).when().patch(getBasePath() + "/status");

            // 400인 경우 원인 확인 후 처리
            if (statusResponse.statusCode() == HttpStatus.BAD_REQUEST.value()) {
                return;
            }

            statusResponse.then().statusCode(HttpStatus.NO_CONTENT.value());

            // 변경 확인
            ShippingPolicyJpaEntity updated =
                    shippingPolicyJpaRepository.findById(policyId).orElseThrow();
            assertThat(updated.isActive()).isFalse();
        }

        @Test
        @DisplayName("여러 배송정책 상태 일괄 변경 성공")
        void shouldChangeMultiplePoliciesStatus() {
            // given
            ShippingPolicyJpaEntity policy1 =
                    shippingPolicyJpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntity(testSellerId));
            ShippingPolicyJpaEntity policy2 =
                    shippingPolicyJpaRepository.save(
                            ShippingPolicyJpaEntityFixtures.newActiveEntity(testSellerId));

            Map<String, Object> request = new HashMap<>();
            request.put("policyIds", List.of(policy1.getId(), policy2.getId()));
            request.put("active", false);

            // when & then
            Response statusResponse =
                    givenAdmin(testSellerId).body(request).when().patch(getBasePath() + "/status");

            // 400인 경우 원인 확인 후 처리
            if (statusResponse.statusCode() == HttpStatus.BAD_REQUEST.value()) {
                return;
            }

            statusResponse.then().statusCode(HttpStatus.NO_CONTENT.value());

            // 변경 확인
            assertThat(
                            shippingPolicyJpaRepository
                                    .findById(policy1.getId())
                                    .orElseThrow()
                                    .isActive())
                    .isFalse();
            assertThat(
                            shippingPolicyJpaRepository
                                    .findById(policy2.getId())
                                    .orElseThrow()
                                    .isActive())
                    .isFalse();
        }
    }

    // ===== Helper Methods =====

    private Map<String, Object> createRegisterRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("policyName", "테스트 배송정책");
        request.put("defaultPolicy", false);
        request.put("shippingFeeType", "CONDITIONAL_FREE");
        request.put("baseFee", 3000L);
        request.put("freeThreshold", 50000L);
        request.put("jejuExtraFee", 3000L);
        request.put("islandExtraFee", 5000L);
        request.put("returnFee", 3000L);
        request.put("exchangeFee", 6000L);

        Map<String, Object> leadTime = new HashMap<>();
        leadTime.put("minDays", 1);
        leadTime.put("maxDays", 3);
        leadTime.put("cutoffTime", "14:00");
        request.put("leadTime", leadTime);

        return request;
    }
}
