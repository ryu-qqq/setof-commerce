package com.ryuqq.setof.integration.test.e2e.admin.refundpolicy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.refundpolicy.RefundPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository.RefundPolicyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.seller.SellerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerJpaRepository;
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
 * RefundPolicy Admin E2E 통합 테스트.
 *
 * <p>환불정책 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.REFUND_POLICY)
@DisplayName("환불정책 Admin API E2E 테스트")
class RefundPolicyAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/sellers/{sellerId}/refund-policies";

    @Autowired private RefundPolicyJpaRepository refundPolicyJpaRepository;
    @Autowired private SellerJpaRepository sellerJpaRepository;

    private Long testSellerId;

    @BeforeEach
    void setUp() {
        refundPolicyJpaRepository.deleteAll();
        sellerJpaRepository.deleteAll();

        SellerJpaEntity seller = sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
        testSellerId = seller.getId();
    }

    private String getBasePath() {
        return BASE_PATH.replace("{sellerId}", testSellerId.toString());
    }

    @Nested
    @DisplayName("POST /api/v2/sellers/{sellerId}/refund-policies - 환불정책 등록")
    class RegisterTest {

        @Test
        @DisplayName("유효한 요청으로 환불정책 등록 성공")
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
            assertThat(refundPolicyJpaRepository.findById(createdId)).isPresent();
        }

        @Test
        @DisplayName("필수 필드(policyName) 누락시 400 에러 반환")
        void shouldReturn400WhenRequiredFieldMissing() {
            // given - policyName 누락
            Map<String, Object> request = new HashMap<>();
            request.put("defaultPolicy", true);
            request.put("returnPeriodDays", 7);
            request.put("exchangePeriodDays", 14);

            // when & then
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .post(getBasePath())
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("반품 가능 기간이 최대값(90일) 초과시 400 에러 반환")
        void shouldReturn400WhenInvalidReturnPeriodDays() {
            // given - returnPeriodDays 90일 초과
            Map<String, Object> request = createRegisterRequest();
            request.put("returnPeriodDays", 100);

            // when & then
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .post(getBasePath())
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("GET /api/v2/sellers/{sellerId}/refund-policies - 환불정책 목록 조회")
    class SearchTest {

        @Test
        @DisplayName("전체 목록 조회 성공")
        void shouldSearchAllPolicies() {
            // given
            refundPolicyJpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntityWithName(testSellerId, "정책1"));
            refundPolicyJpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntityWithName(testSellerId, "정책2"));

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
                refundPolicyJpaRepository.save(
                        RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
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
        @DisplayName("데이터 없을 때 빈 결과 반환")
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
            refundPolicyJpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newActiveEntity(testSellerId));

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
                    .body("data.content[0].returnPeriodDays", notNullValue())
                    .body("data.content[0].exchangePeriodDays", notNullValue());
        }
    }

    @Nested
    @DisplayName("PUT /api/v2/sellers/{sellerId}/refund-policies/{policyId} - 환불정책 수정")
    class UpdateTest {

        @Test
        @DisplayName("환불정책 수정 성공")
        void shouldUpdateSuccessfully() {
            // given
            RefundPolicyJpaEntity saved =
                    refundPolicyJpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntity(testSellerId));
            Long policyId = saved.getId();

            Map<String, Object> request = createUpdateRequest();

            // when
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .put(getBasePath() + "/" + policyId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // then - DB 상태 검증
            RefundPolicyJpaEntity updated =
                    refundPolicyJpaRepository.findById(policyId).orElseThrow();
            assertThat(updated.getPolicyName()).isEqualTo("수정된 정책명");
            assertThat(updated.getReturnPeriodDays()).isEqualTo(14);
            assertThat(updated.getExchangePeriodDays()).isEqualTo(21);
        }

        @Test
        @DisplayName("존재하지 않는 policyId로 수정 요청시 404 에러 반환")
        void shouldReturn404WhenPolicyNotFound() {
            // given
            Long nonExistentPolicyId = 999999L;
            Map<String, Object> request = createUpdateRequest();

            // when & then
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .put(getBasePath() + "/" + nonExistentPolicyId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("필수 필드(policyName) 누락시 400 에러 반환")
        void shouldReturn400WhenRequiredFieldMissing() {
            // given
            RefundPolicyJpaEntity saved =
                    refundPolicyJpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntity(testSellerId));
            Long policyId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("defaultPolicy", false);
            request.put("returnPeriodDays", 14);
            request.put("exchangePeriodDays", 21);

            // when & then
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .put(getBasePath() + "/" + policyId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("PATCH /api/v2/sellers/{sellerId}/refund-policies/status - 환불정책 상태 변경")
    class ChangeStatusTest {

        @Test
        @DisplayName("환불정책 상태 비활성화 성공")
        void shouldChangeStatusSuccessfully() {
            // given - 기본 정책은 유지하고, 비기본 정책을 비활성화 대상으로 생성
            refundPolicyJpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newDefaultEntity(testSellerId));
            RefundPolicyJpaEntity saved =
                    refundPolicyJpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    testSellerId, "비기본 정책"));
            Long policyId = saved.getId();
            assertThat(saved.isActive()).isTrue();

            Map<String, Object> request = new HashMap<>();
            request.put("policyIds", List.of(policyId));
            request.put("active", false);

            // when
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .patch(getBasePath() + "/status")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // then - DB 상태 검증
            RefundPolicyJpaEntity updated =
                    refundPolicyJpaRepository.findById(policyId).orElseThrow();
            assertThat(updated.isActive()).isFalse();
        }

        @Test
        @DisplayName("여러 환불정책 상태 일괄 비활성화 성공")
        void shouldChangeMultiplePoliciesStatus() {
            // given - 기본 정책은 유지하고, 비기본 정책 2개를 비활성화 대상으로 생성
            refundPolicyJpaRepository.save(
                    RefundPolicyJpaEntityFixtures.newDefaultEntity(testSellerId));
            RefundPolicyJpaEntity policy1 =
                    refundPolicyJpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    testSellerId, "비기본 정책1"));
            RefundPolicyJpaEntity policy2 =
                    refundPolicyJpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntityWithName(
                                    testSellerId, "비기본 정책2"));

            Map<String, Object> request = new HashMap<>();
            request.put("policyIds", List.of(policy1.getId(), policy2.getId()));
            request.put("active", false);

            // when
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .patch(getBasePath() + "/status")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // then - DB 상태 검증
            assertThat(refundPolicyJpaRepository.findById(policy1.getId()).orElseThrow().isActive())
                    .isFalse();
            assertThat(refundPolicyJpaRepository.findById(policy2.getId()).orElseThrow().isActive())
                    .isFalse();
        }

        @Test
        @DisplayName("policyIds 목록이 비어있을 때 400 에러 반환")
        void shouldReturn400WhenPolicyIdsEmpty() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("policyIds", List.of());
            request.put("active", false);

            // when & then
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .patch(getBasePath() + "/status")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("active 필드 누락시 400 에러 반환")
        void shouldReturn400WhenActiveFieldMissing() {
            // given
            RefundPolicyJpaEntity saved =
                    refundPolicyJpaRepository.save(
                            RefundPolicyJpaEntityFixtures.newActiveEntity(testSellerId));

            Map<String, Object> request = new HashMap<>();
            request.put("policyIds", List.of(saved.getId()));
            // active 누락

            // when & then
            givenAdmin(testSellerId)
                    .body(request)
                    .when()
                    .patch(getBasePath() + "/status")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    // ===== Helper Methods =====

    private Map<String, Object> createRegisterRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("policyName", "테스트 환불정책");
        request.put("defaultPolicy", false);
        request.put("returnPeriodDays", 7);
        request.put("exchangePeriodDays", 14);
        request.put("nonReturnableConditions", List.of("OPENED_PACKAGING", "USED_PRODUCT"));
        request.put("partialRefundEnabled", true);
        request.put("inspectionRequired", true);
        request.put("inspectionPeriodDays", 3);
        request.put("additionalInfo", "테스트 추가 안내 문구");
        return request;
    }

    private Map<String, Object> createUpdateRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("policyName", "수정된 정책명");
        request.put("defaultPolicy", true);
        request.put("returnPeriodDays", 14);
        request.put("exchangePeriodDays", 21);
        request.put("nonReturnableConditions", List.of("OPENED_PACKAGING"));
        request.put("partialRefundEnabled", false);
        request.put("inspectionRequired", false);
        request.put("inspectionPeriodDays", 0);
        return request;
    }
}
