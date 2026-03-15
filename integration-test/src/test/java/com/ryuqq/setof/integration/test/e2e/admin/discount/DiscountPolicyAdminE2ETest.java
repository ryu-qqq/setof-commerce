package com.ryuqq.setof.integration.test.e2e.admin.discount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountTargetJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountTargetJpaRepository;
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
 * DiscountPolicy Admin E2E 통합 테스트.
 *
 * <p>할인 정책 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.DISCOUNT)
@DisplayName("할인 정책 Admin API E2E 테스트")
class DiscountPolicyAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_SINGLE_PATH = "/v1/discount";
    private static final String BASE_LIST_PATH = "/v1/discounts";

    @Autowired private DiscountPolicyJpaRepository discountPolicyJpaRepository;
    @Autowired private DiscountTargetJpaRepository discountTargetJpaRepository;

    @BeforeEach
    void setUp() {
        discountTargetJpaRepository.deleteAll();
        discountPolicyJpaRepository.deleteAll();
    }

    // =========================================================================
    // Query 테스트
    // =========================================================================

    @Nested
    @DisplayName("GET /api/v1/discount/{discountPolicyId} - 할인 정책 단건 조회")
    class GetDetailTest {

        @Test
        @DisplayName("존재하는 정책 단건 조회 성공 - 응답 필드 검증")
        void shouldGetDetailSuccessfully() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_SINGLE_PATH + "/" + policyId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.discountPolicyId", equalTo(policyId.intValue()))
                    .body("data.discountDetails", notNullValue())
                    .body(
                            "data.discountDetails.discountPolicyName",
                            equalTo(DiscountPolicyJpaEntityFixtures.DEFAULT_NAME))
                    .body("data.discountDetails.discountType", equalTo("RATE"))
                    .body("data.discountDetails.publisherType", equalTo("ADMIN"))
                    .body("data.discountDetails.activeYn", equalTo("Y"))
                    .body("data.discountDetails.priority", equalTo(1))
                    .body("data.insertDate", notNullValue())
                    .body("data.updateDate", notNullValue());
        }

        @Test
        @DisplayName("존재하지 않는 정책 조회 시 404 에러 반환")
        void shouldReturn404WhenPolicyNotFound() {
            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_SINGLE_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/discounts - 할인 정책 목록 조회")
    class SearchTest {

        @Test
        @DisplayName("전체 목록 조회 성공 - 응답 필드 검증")
        void shouldSearchAllPolicies() {
            // given
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책A"));
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책B"));

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(2))
                    .body("data.totalCount", equalTo(2))
                    .body("data.page", equalTo(0))
                    .body("data.size", equalTo(20))
                    .body("data.items[0].discountPolicyId", notNullValue())
                    .body("data.items[0].discountDetails.discountPolicyName", notNullValue())
                    .body("data.items[0].discountDetails.discountType", notNullValue())
                    .body("data.items[0].discountDetails.activeYn", notNullValue());
        }

        @Test
        @DisplayName("데이터 없을 때 빈 결과 반환")
        void shouldReturnEmptyResult() {
            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(0))
                    .body("data.totalCount", equalTo(0));
        }

        @Test
        @DisplayName("activeYn=Y 필터 적용 시 활성 정책만 반환")
        void shouldFilterByActiveYn() {
            // given - 활성 2개, 비활성 1개
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("활성정책1"));
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("활성정책2"));
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newInactiveRateEntity());

            // when & then - activeYn=Y 필터
            givenAdmin()
                    .queryParam("activeYn", "Y")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(2))
                    .body("data.totalCount", equalTo(2));
        }

        @Test
        @DisplayName("activeYn=N 필터 적용 시 비활성 정책만 반환")
        void shouldFilterByInactiveYn() {
            // given - 활성 1개, 비활성 2개
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("활성정책"));
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newInactiveRateEntity());
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newInactiveRateEntity());

            // when & then
            givenAdmin()
                    .queryParam("activeYn", "N")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(greaterThanOrEqualTo(2)));
        }

        @Test
        @DisplayName("publisherType=ADMIN 필터 적용 성공")
        void shouldFilterByPublisherType() {
            // given
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("어드민정책"));
            discountPolicyJpaRepository.save(
                    DiscountPolicyJpaEntityFixtures.newActiveSellerEntity());

            // when & then
            givenAdmin()
                    .queryParam("publisherType", "ADMIN")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(1))
                    .body("data.items[0].discountDetails.publisherType", equalTo("ADMIN"));
        }

        @Test
        @DisplayName("페이징 처리 확인 - size 제한 및 totalCount 정확성")
        void shouldPaginateCorrectly() {
            // given - 5개 생성
            for (int i = 0; i < 5; i++) {
                discountPolicyJpaRepository.save(
                        DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책" + i));
            }

            // when & then - page=0, size=2
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 2)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(2))
                    .body("data.totalCount", equalTo(5))
                    .body("data.page", equalTo(0))
                    .body("data.size", equalTo(2));
        }

        @Test
        @DisplayName("두 번째 페이지 조회 성공")
        void shouldReturnSecondPage() {
            // given - 5개 생성
            for (int i = 0; i < 5; i++) {
                discountPolicyJpaRepository.save(
                        DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책" + i));
            }

            // when & then - page=1, size=2
            givenAdmin()
                    .queryParam("page", 1)
                    .queryParam("size", 2)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(2))
                    .body("data.page", equalTo(1))
                    .body("data.totalCount", equalTo(5));
        }
    }

    // =========================================================================
    // Command 테스트
    // =========================================================================

    @Nested
    @DisplayName("POST /api/v1/discount - 할인 정책 생성")
    class CreateTest {

        @Test
        @DisplayName("RATE 타입 할인 정책 생성 성공 - 응답 및 DB 검증")
        void shouldCreateRatePolicySuccessfully() {
            // given
            Map<String, Object> request = createRateDiscountRequest("신규 회원 10% 할인");

            // when
            Response response = givenAdmin().body(request).when().post(BASE_SINGLE_PATH);

            // then - 응답 검증
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data.discountPolicyId", greaterThan(0))
                    .body("data.discountDetails.discountPolicyName", equalTo("신규 회원 10% 할인"))
                    .body("data.discountDetails.discountType", equalTo("RATE"))
                    .body("data.discountDetails.publisherType", equalTo("ADMIN"))
                    .body("data.discountDetails.activeYn", equalTo("Y"))
                    .body("data.discountDetails.discountRatio", equalTo(10.0f))
                    .body("data.discountDetails.discountLimitYn", equalTo("Y"))
                    .body("data.insertDate", notNullValue());

            // DB 상태 검증
            Long createdId = response.jsonPath().getLong("data.discountPolicyId");
            assertThat(discountPolicyJpaRepository.findById(createdId)).isPresent();
        }

        @Test
        @DisplayName("FIXED_AMOUNT(PRICE) 타입 할인 정책 생성 성공")
        void shouldCreateFixedAmountPolicySuccessfully() {
            // given
            Map<String, Object> request = createFixedAmountDiscountRequest("5000원 할인");

            // when
            Response response = givenAdmin().body(request).when().post(BASE_SINGLE_PATH);

            // then
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data.discountPolicyId", greaterThan(0))
                    .body("data.discountDetails.discountPolicyName", equalTo("5000원 할인"))
                    .body("data.discountDetails.discountType", equalTo("PRICE"))
                    .body("data.discountDetails.activeYn", equalTo("Y"));

            Long createdId = response.jsonPath().getLong("data.discountPolicyId");
            assertThat(discountPolicyJpaRepository.findById(createdId)).isPresent();
        }

        @Test
        @DisplayName("discountDetails 누락 시 400 에러 반환")
        void shouldReturn400WhenDiscountDetailsMissing() {
            // given - discountDetails 필드 없음
            Map<String, Object> request = new HashMap<>();
            request.put("discountPolicyId", null);
            // discountDetails 누락

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_SINGLE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("discountPolicyName 누락 시 400 에러 반환")
        void shouldReturn400WhenPolicyNameMissing() {
            // given - discountPolicyName 누락
            Map<String, Object> discountDetails = new HashMap<>();
            discountDetails.put("discountType", "RATE");
            discountDetails.put("publisherType", "ADMIN");
            discountDetails.put("issueType", "PRODUCT");
            discountDetails.put("discountLimitYn", "Y");
            discountDetails.put("maxDiscountPrice", 10000);
            discountDetails.put("shareYn", "N");
            discountDetails.put("shareRatio", 0.0);
            discountDetails.put("discountRatio", 10.0);
            discountDetails.put("policyStartDate", "2026-01-01 00:00:00");
            discountDetails.put("policyEndDate", "2026-12-31 23:59:59");
            discountDetails.put("priority", 1);
            discountDetails.put("activeYn", "Y");
            // discountPolicyName 누락

            Map<String, Object> request = new HashMap<>();
            request.put("discountDetails", discountDetails);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_SINGLE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/discount/{discountPolicyId} - 할인 정책 수정")
    class UpdateTest {

        @Test
        @DisplayName("할인 정책 수정 성공 - 응답 및 DB 검증")
        void shouldUpdatePolicySuccessfully() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            Map<String, Object> request = createUpdateRateDiscountRequest("수정된 할인 정책명", 20.0);

            // when
            Response response =
                    givenAdmin().body(request).when().put(BASE_SINGLE_PATH + "/" + policyId);

            // then - 응답 검증
            response.then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.discountPolicyId", equalTo(policyId.intValue()))
                    .body("data.discountDetails.discountPolicyName", equalTo("수정된 할인 정책명"))
                    .body("data.discountDetails.discountRatio", equalTo(20.0f));

            // DB 상태 검증
            DiscountPolicyJpaEntity updated =
                    discountPolicyJpaRepository.findById(policyId).orElseThrow();
            assertThat(updated.getName()).isEqualTo("수정된 할인 정책명");
            assertThat(updated.getDiscountRate()).isEqualTo(20.0);
        }

        @Test
        @DisplayName("존재하지 않는 정책 수정 시 404 에러 반환")
        void shouldReturn404WhenPolicyNotFound() {
            // given
            Map<String, Object> request = createUpdateRateDiscountRequest("수정 정책명", 15.0);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_SINGLE_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("discountDetails 누락 시 400 에러 반환")
        void shouldReturn400WhenDiscountDetailsMissing() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            // discountDetails 누락

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_SINGLE_PATH + "/" + policyId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/discounts - 할인 정책 상태 일괄 변경")
    class UpdateStatusTest {

        @Test
        @DisplayName("할인 정책 일괄 비활성화 성공 - DB 검증")
        void shouldDeactivatePoliciesSuccessfully() {
            // given - 활성 정책 2개 생성
            DiscountPolicyJpaEntity policy1 =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책1"));
            DiscountPolicyJpaEntity policy2 =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntityWithName("정책2"));

            assertThat(policy1.isActive()).isTrue();
            assertThat(policy2.isActive()).isTrue();

            Map<String, Object> request = new HashMap<>();
            request.put("discountPolicyIds", List.of(policy1.getId(), policy2.getId()));
            request.put("activeYn", "N");

            // when
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // then - DB 상태 검증
            assertThat(
                            discountPolicyJpaRepository
                                    .findById(policy1.getId())
                                    .orElseThrow()
                                    .isActive())
                    .isFalse();
            assertThat(
                            discountPolicyJpaRepository
                                    .findById(policy2.getId())
                                    .orElseThrow()
                                    .isActive())
                    .isFalse();
        }

        @Test
        @DisplayName("할인 정책 일괄 활성화 성공 - DB 검증")
        void shouldActivatePoliciesSuccessfully() {
            // given - 비활성 정책 2개 생성
            DiscountPolicyJpaEntity policy1 =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newInactiveRateEntity());
            DiscountPolicyJpaEntity policy2 =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newInactiveRateEntity());

            assertThat(policy1.isActive()).isFalse();
            assertThat(policy2.isActive()).isFalse();

            Map<String, Object> request = new HashMap<>();
            request.put("discountPolicyIds", List.of(policy1.getId(), policy2.getId()));
            request.put("activeYn", "Y");

            // when
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // then - DB 상태 검증
            assertThat(
                            discountPolicyJpaRepository
                                    .findById(policy1.getId())
                                    .orElseThrow()
                                    .isActive())
                    .isTrue();
            assertThat(
                            discountPolicyJpaRepository
                                    .findById(policy2.getId())
                                    .orElseThrow()
                                    .isActive())
                    .isTrue();
        }

        @Test
        @DisplayName("discountPolicyIds 목록이 비어있을 때 400 에러 반환")
        void shouldReturn400WhenPolicyIdsEmpty() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("discountPolicyIds", List.of());
            request.put("activeYn", "N");

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("discountPolicyIds 누락 시 400 에러 반환")
        void shouldReturn400WhenPolicyIdsMissing() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("activeYn", "N");
            // discountPolicyIds 누락

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("activeYn 누락 시 400 에러 반환")
        void shouldReturn400WhenActiveYnMissing() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());

            Map<String, Object> request = new HashMap<>();
            request.put("discountPolicyIds", List.of(saved.getId()));
            // activeYn 누락

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/discounts/excel - 엑셀 기반 할인 정책 일괄 생성")
    class CreateFromExcelTest {

        @Test
        @DisplayName("엑셀 기반 단건 할인 정책 일괄 생성 성공")
        void shouldCreateSinglePolicyFromExcelSuccessfully() {
            // given - 엑셀 1건
            List<Map<String, Object>> requests =
                    List.of(createExcelRequest("엑셀 할인 정책1", List.of(101L)));

            // when
            Response response = givenAdmin().body(requests).when().post(BASE_LIST_PATH + "/excel");

            // then
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data", hasSize(1))
                    .body("data[0].discountPolicyId", greaterThan(0))
                    .body("data[0].discountDetails.discountPolicyName", equalTo("엑셀 할인 정책1"));

            assertThat(discountPolicyJpaRepository.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("엑셀 기반 복수 할인 정책 일괄 생성 성공")
        void shouldCreateMultiplePoliciesFromExcelSuccessfully() {
            // given - 엑셀 2건
            List<Map<String, Object>> requests =
                    List.of(
                            createExcelRequest("엑셀 정책A", List.of(101L, 102L)),
                            createExcelRequest("엑셀 정책B", List.of(201L)));

            // when
            Response response = givenAdmin().body(requests).when().post(BASE_LIST_PATH + "/excel");

            // then
            response.then().statusCode(HttpStatus.CREATED.value()).body("data", hasSize(2));

            assertThat(discountPolicyJpaRepository.findAll()).hasSize(2);
        }

        @Test
        @DisplayName("targetIds 비어있을 때 400 에러 반환")
        void shouldReturn400WhenTargetIdsEmpty() {
            // given - targetIds 빈 리스트
            List<Map<String, Object>> requests = List.of(createExcelRequest("잘못된 정책", List.of()));

            // when & then
            givenAdmin()
                    .body(requests)
                    .when()
                    .post(BASE_LIST_PATH + "/excel")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/discount/{discountPolicyId}/targets - 할인 대상 추가")
    class AddTargetsTest {

        @Test
        @DisplayName("PRODUCT 타입 할인 대상 추가 성공 - DB 검증")
        void shouldAddProductTargetsSuccessfully() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("issueType", "PRODUCT");
            request.put("targetIds", List.of(101L, 102L, 103L));

            // when
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_SINGLE_PATH + "/" + policyId + "/targets")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // then - DB 상태 검증: 타겟 3건 생성 확인
            assertThat(discountTargetJpaRepository.findAll()).hasSize(3);
        }

        @Test
        @DisplayName("SELLER 타입 할인 대상 추가 성공")
        void shouldAddSellerTargetsSuccessfully() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("issueType", "SELLER");
            request.put("targetIds", List.of(1L));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_SINGLE_PATH + "/" + policyId + "/targets")
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("issueType 누락 시 400 에러 반환")
        void shouldReturn400WhenIssueTypeMissing() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("targetIds", List.of(101L));
            // issueType 누락

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_SINGLE_PATH + "/" + policyId + "/targets")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("targetIds 비어있을 때 400 에러 반환")
        void shouldReturn400WhenTargetIdsEmpty() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("issueType", "PRODUCT");
            request.put("targetIds", List.of());

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_SINGLE_PATH + "/" + policyId + "/targets")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/discount/{discountPolicyId}/targets - 할인 대상 교체")
    class ReplaceTargetsTest {

        @Test
        @DisplayName("할인 대상 교체 성공 - DB 검증")
        void shouldReplaceTargetsSuccessfully() {
            // given - 정책 생성 후 초기 대상 추가
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            discountTargetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policyId, 101L));
            discountTargetJpaRepository.save(
                    DiscountTargetJpaEntityFixtures.newActiveProductTarget(policyId, 102L));

            // 교체 요청: 새 대상 3개
            Map<String, Object> request = new HashMap<>();
            request.put("issueType", "PRODUCT");
            request.put("targetIds", List.of(201L, 202L, 203L));

            // when
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_SINGLE_PATH + "/" + policyId + "/targets")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // then - 새 대상 3건 존재 확인 (기존 2건은 비활성화되고 새 3건 추가)
            long activeCount =
                    discountTargetJpaRepository.findAll().stream()
                            .filter(t -> t.isActive())
                            .count();
            assertThat(activeCount).isEqualTo(3);
        }

        @Test
        @DisplayName("issueType 누락 시 400 에러 반환")
        void shouldReturn400WhenIssueTypeMissing() {
            // given
            DiscountPolicyJpaEntity saved =
                    discountPolicyJpaRepository.save(
                            DiscountPolicyJpaEntityFixtures.newActiveRateEntity());
            Long policyId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("targetIds", List.of(201L));
            // issueType 누락

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_SINGLE_PATH + "/" + policyId + "/targets")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    // =========================================================================
    // 전체 플로우 시나리오
    // =========================================================================

    @Nested
    @DisplayName("전체 플로우 시나리오")
    class FullFlowTest {

        @Test
        @DisplayName("할인 정책 생성 -> 조회 -> 수정 -> 조회 확인 (CRUD 전체 흐름)")
        void shouldCompleteCrudFlowSuccessfully() {
            // Step 1: 할인 정책 생성
            Map<String, Object> createRequest = createRateDiscountRequest("신규 회원 할인");
            Response createResponse =
                    givenAdmin().body(createRequest).when().post(BASE_SINGLE_PATH);

            createResponse.then().statusCode(HttpStatus.CREATED.value());
            Long policyId = createResponse.jsonPath().getLong("data.discountPolicyId");
            assertThat(policyId).isGreaterThan(0);

            // Step 2: 단건 조회 확인
            givenAdmin()
                    .when()
                    .get(BASE_SINGLE_PATH + "/" + policyId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.discountDetails.discountPolicyName", equalTo("신규 회원 할인"))
                    .body("data.discountDetails.discountRatio", equalTo(10.0f))
                    .body("data.discountDetails.activeYn", equalTo("Y"));

            // Step 3: 할인 정책 수정
            Map<String, Object> updateRequest = createUpdateRateDiscountRequest("수정된 할인 정책", 15.0);
            givenAdmin()
                    .body(updateRequest)
                    .when()
                    .put(BASE_SINGLE_PATH + "/" + policyId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.discountDetails.discountPolicyName", equalTo("수정된 할인 정책"))
                    .body("data.discountDetails.discountRatio", equalTo(15.0f));

            // Step 4: 수정 후 단건 조회 확인
            givenAdmin()
                    .when()
                    .get(BASE_SINGLE_PATH + "/" + policyId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.discountDetails.discountPolicyName", equalTo("수정된 할인 정책"))
                    .body("data.discountDetails.discountRatio", equalTo(15.0f));

            // DB 최종 상태 검증
            DiscountPolicyJpaEntity finalEntity =
                    discountPolicyJpaRepository.findById(policyId).orElseThrow();
            assertThat(finalEntity.getName()).isEqualTo("수정된 할인 정책");
            assertThat(finalEntity.getDiscountRate()).isEqualTo(15.0);
        }

        @Test
        @DisplayName("RATE 타입 정책 생성 -> 대상 추가 -> 대상 교체 -> 조회 확인 (타겟 관리 전체 흐름)")
        void shouldCompleteTargetManagementFlowSuccessfully() {
            // Step 1: RATE 타입 할인 정책 생성
            Map<String, Object> createRequest = createRateDiscountRequest("RATE 타입 정책");
            Response createResponse =
                    givenAdmin().body(createRequest).when().post(BASE_SINGLE_PATH);

            createResponse.then().statusCode(HttpStatus.CREATED.value());
            Long policyId = createResponse.jsonPath().getLong("data.discountPolicyId");

            // Step 2: 대상 추가 (PRODUCT 타입 3건)
            Map<String, Object> addTargetsRequest = new HashMap<>();
            addTargetsRequest.put("issueType", "PRODUCT");
            addTargetsRequest.put("targetIds", List.of(101L, 102L, 103L));

            givenAdmin()
                    .body(addTargetsRequest)
                    .when()
                    .post(BASE_SINGLE_PATH + "/" + policyId + "/targets")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            assertThat(discountTargetJpaRepository.findAll()).hasSize(3);

            // Step 3: 대상 교체 (새 PRODUCT 타입 2건으로 교체)
            Map<String, Object> replaceTargetsRequest = new HashMap<>();
            replaceTargetsRequest.put("issueType", "PRODUCT");
            replaceTargetsRequest.put("targetIds", List.of(201L, 202L));

            givenAdmin()
                    .body(replaceTargetsRequest)
                    .when()
                    .put(BASE_SINGLE_PATH + "/" + policyId + "/targets")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // Step 4: 단건 조회 - 정책 정보 확인
            givenAdmin()
                    .when()
                    .get(BASE_SINGLE_PATH + "/" + policyId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.discountPolicyId", equalTo(policyId.intValue()))
                    .body("data.discountDetails.discountType", equalTo("RATE"));

            // 활성 타겟 2건만 남아있어야 함
            long activeTargetCount =
                    discountTargetJpaRepository.findAll().stream()
                            .filter(t -> t.isActive())
                            .count();
            assertThat(activeTargetCount).isEqualTo(2);
        }

        @Test
        @DisplayName("할인 정책 생성 -> 목록 조회 -> 일괄 비활성화 -> 목록 조회 확인")
        void shouldCompleteBulkStatusChangeFlowSuccessfully() {
            // Step 1: 할인 정책 3개 생성
            Map<String, Object> req1 = createRateDiscountRequest("정책1");
            Map<String, Object> req2 = createRateDiscountRequest("정책2");
            Map<String, Object> req3 = createRateDiscountRequest("정책3");

            Long id1 =
                    givenAdmin()
                            .body(req1)
                            .when()
                            .post(BASE_SINGLE_PATH)
                            .jsonPath()
                            .getLong("data.discountPolicyId");
            Long id2 =
                    givenAdmin()
                            .body(req2)
                            .when()
                            .post(BASE_SINGLE_PATH)
                            .jsonPath()
                            .getLong("data.discountPolicyId");
            Long id3 =
                    givenAdmin()
                            .body(req3)
                            .when()
                            .post(BASE_SINGLE_PATH)
                            .jsonPath()
                            .getLong("data.discountPolicyId");

            // Step 2: 목록 조회 - 3건 모두 활성 상태 확인
            givenAdmin()
                    .queryParam("activeYn", "Y")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(3));

            // Step 3: 2개 정책 일괄 비활성화
            Map<String, Object> statusRequest = new HashMap<>();
            statusRequest.put("discountPolicyIds", List.of(id1, id2));
            statusRequest.put("activeYn", "N");

            givenAdmin()
                    .body(statusRequest)
                    .when()
                    .patch(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // Step 4: 목록 조회 - 활성 1건만 남아있는지 확인
            givenAdmin()
                    .queryParam("activeYn", "Y")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_LIST_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.items", hasSize(1));
        }
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /** RATE 타입 할인 정책 생성 요청 Map 생성. */
    private Map<String, Object> createRateDiscountRequest(String policyName) {
        Map<String, Object> discountDetails = new HashMap<>();
        discountDetails.put("discountPolicyName", policyName);
        discountDetails.put("discountType", "RATE");
        discountDetails.put("publisherType", "ADMIN");
        discountDetails.put("issueType", "PRODUCT");
        discountDetails.put("discountLimitYn", "Y");
        discountDetails.put("maxDiscountPrice", 10000);
        discountDetails.put("shareYn", "N");
        discountDetails.put("shareRatio", 0.0);
        discountDetails.put("discountRatio", 10.0);
        discountDetails.put("policyStartDate", "2026-01-01 00:00:00");
        discountDetails.put("policyEndDate", "2026-12-31 23:59:59");
        discountDetails.put("memo", "테스트 할인 메모");
        discountDetails.put("priority", 1);
        discountDetails.put("activeYn", "Y");

        Map<String, Object> request = new HashMap<>();
        request.put("discountDetails", discountDetails);
        return request;
    }

    /** FIXED_AMOUNT(PRICE) 타입 할인 정책 생성 요청 Map 생성. */
    private Map<String, Object> createFixedAmountDiscountRequest(String policyName) {
        Map<String, Object> discountDetails = new HashMap<>();
        discountDetails.put("discountPolicyName", policyName);
        discountDetails.put("discountType", "PRICE");
        discountDetails.put("publisherType", "ADMIN");
        discountDetails.put("issueType", "PRODUCT");
        discountDetails.put("discountLimitYn", "N");
        discountDetails.put("maxDiscountPrice", 0);
        discountDetails.put("shareYn", "N");
        discountDetails.put("shareRatio", 0.0);
        discountDetails.put("discountRatio", 5000.0);
        discountDetails.put("policyStartDate", "2026-01-01 00:00:00");
        discountDetails.put("policyEndDate", "2026-12-31 23:59:59");
        discountDetails.put("memo", "고정금액 할인 메모");
        discountDetails.put("priority", 1);
        discountDetails.put("activeYn", "Y");

        Map<String, Object> request = new HashMap<>();
        request.put("discountDetails", discountDetails);
        return request;
    }

    /** RATE 타입 할인 정책 수정 요청 Map 생성. */
    private Map<String, Object> createUpdateRateDiscountRequest(
            String policyName, double discountRatio) {
        Map<String, Object> discountDetails = new HashMap<>();
        discountDetails.put("discountPolicyName", policyName);
        discountDetails.put("discountType", "RATE");
        discountDetails.put("publisherType", "ADMIN");
        discountDetails.put("issueType", "PRODUCT");
        discountDetails.put("discountLimitYn", "Y");
        discountDetails.put("maxDiscountPrice", 15000);
        discountDetails.put("shareYn", "N");
        discountDetails.put("shareRatio", 0.0);
        discountDetails.put("discountRatio", discountRatio);
        discountDetails.put("policyStartDate", "2026-01-01 00:00:00");
        discountDetails.put("policyEndDate", "2026-12-31 23:59:59");
        discountDetails.put("memo", "수정된 메모");
        discountDetails.put("priority", 2);
        discountDetails.put("activeYn", "Y");

        Map<String, Object> request = new HashMap<>();
        request.put("discountDetails", discountDetails);
        return request;
    }

    /** 엑셀 기반 할인 정책 생성 요청 단건 Map 생성. */
    private Map<String, Object> createExcelRequest(String policyName, List<Long> targetIds) {
        Map<String, Object> discountDetails = new HashMap<>();
        discountDetails.put("discountPolicyName", policyName);
        discountDetails.put("discountType", "RATE");
        discountDetails.put("publisherType", "ADMIN");
        discountDetails.put("issueType", "PRODUCT");
        discountDetails.put("discountLimitYn", "Y");
        discountDetails.put("maxDiscountPrice", 10000);
        discountDetails.put("shareYn", "N");
        discountDetails.put("shareRatio", 0.0);
        discountDetails.put("discountRatio", 10.0);
        discountDetails.put("policyStartDate", "2026-01-01 00:00:00");
        discountDetails.put("policyEndDate", "2026-12-31 23:59:59");
        discountDetails.put("memo", "엑셀 일괄 생성");
        discountDetails.put("priority", 1);
        discountDetails.put("activeYn", "Y");

        Map<String, Object> request = new HashMap<>();
        request.put("discountDetails", discountDetails);
        request.put("targetIds", targetIds);
        return request;
    }
}
