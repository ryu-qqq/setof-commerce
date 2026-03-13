package com.ryuqq.setof.integration.test.e2e.admin.commoncodetype;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import com.ryuqq.setof.adapter.out.persistence.commoncodetype.CommonCodeTypeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.entity.CommonCodeTypeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.commoncodetype.repository.CommonCodeTypeJpaRepository;
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
 * CommonCodeType Admin E2E 통합 테스트.
 *
 * <p>공통 코드 타입 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.COMMON_CODE)
@DisplayName("공통 코드 타입 Admin API E2E 테스트")
class CommonCodeTypeAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/admin/common-code-types";

    @Autowired private CommonCodeTypeJpaRepository commonCodeTypeJpaRepository;

    @BeforeEach
    void setUp() {
        commonCodeTypeJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /v2/admin/common-code-types - 공통 코드 타입 등록")
    class RegisterTest {

        @Test
        @DisplayName("유효한 요청으로 공통 코드 타입 등록 성공")
        void shouldRegisterSuccessfully() {
            // given
            String uniqueSuffix = String.valueOf(System.currentTimeMillis());
            Map<String, Object> request = createRegisterRequest(uniqueSuffix);

            // when
            Response response = givenAdmin().body(request).when().post(BASE_PATH);

            // then
            response.then().statusCode(HttpStatus.CREATED.value()).body("data", greaterThan(0));

            Long createdId = response.jsonPath().getLong("data");
            assertThat(commonCodeTypeJpaRepository.findById(createdId)).isPresent();
        }

        @Test
        @DisplayName("필수 필드 누락시 400 에러 반환")
        void shouldReturn400WhenRequiredFieldMissing() {
            // given - code 누락
            Map<String, Object> request = new HashMap<>();
            request.put("name", "테스트 타입");
            request.put("displayOrder", 1);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("중복 코드로 등록시 409 에러 반환")
        void shouldReturn409WhenDuplicateCode() {
            // given - 기존 코드 타입 생성
            CommonCodeTypeJpaEntity existing =
                    commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            String existingCode = existing.getCode();

            // 동일한 코드로 등록 시도
            Map<String, Object> request = new HashMap<>();
            request.put("code", existingCode);
            request.put("name", "중복 테스트");
            request.put("displayOrder", 1);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    @DisplayName("GET /v2/admin/common-code-types - 공통 코드 타입 목록 조회")
    class SearchTest {

        @Test
        @DisplayName("전체 목록 조회 성공")
        void shouldSearchAllTypes() {
            // given
            commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("ORDER_STATUS", "주문상태"));

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(2))
                    .body("data.page", equalTo(0))
                    .body("data.size", equalTo(20));
        }

        @Test
        @DisplayName("active=true 필터로 활성 타입만 조회")
        void shouldFilterByActiveTrue() {
            // given - 활성 2개, 비활성 1개 저장
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("ACTIVE_TYPE_1", "활성타입1"));
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("ACTIVE_TYPE_2", "활성타입2"));
            commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newInactiveEntity());

            // when & then
            givenAdmin()
                    .queryParam("active", true)
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(2));
        }

        @Test
        @DisplayName("active=false 필터로 비활성 타입만 조회")
        void shouldFilterByActiveFalse() {
            // given - 활성 1개, 비활성 1개 저장
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("ACTIVE_TYPE", "활성타입"));
            commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newInactiveEntity());

            // when & then
            givenAdmin()
                    .queryParam("active", false)
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.totalElements", equalTo(1));
        }

        @Test
        @DisplayName("searchWord 키워드로 타입 이름 검색")
        void shouldFilterBySearchWord() {
            // given
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("PAYMENT_METHOD", "결제수단"));
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("ORDER_STATUS", "주문상태"));
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("DELIVERY_TYPE", "배송유형"));

            // when & then - "주문" 키워드가 포함된 타입만 조회
            givenAdmin()
                    .queryParam("searchWord", "주문")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.totalElements", equalTo(1));
        }

        @Test
        @DisplayName("searchField=code 로 코드 검색")
        void shouldFilterByCodeField() {
            // given
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("PAYMENT_METHOD", "결제수단"));
            commonCodeTypeJpaRepository.save(
                    CommonCodeTypeJpaEntityFixtures.newEntityWithCode("ORDER_STATUS", "주문상태"));

            // when & then - 코드 필드에서 "PAYMENT" 검색
            givenAdmin()
                    .queryParam("searchField", "code")
                    .queryParam("searchWord", "PAYMENT")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.totalElements", equalTo(1));
        }

        @Test
        @DisplayName("페이징 처리 확인")
        void shouldPaginateCorrectly() {
            // given
            for (int i = 0; i < 5; i++) {
                commonCodeTypeJpaRepository.save(
                        CommonCodeTypeJpaEntityFixtures.newEntityWithCode("TYPE_" + i, "타입" + i));
            }

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 2)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(5))
                    .body("data.page", equalTo(0))
                    .body("data.size", equalTo(2));
        }

        @Test
        @DisplayName("빈 결과 조회")
        void shouldReturnEmptyResult() {
            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(0))
                    .body("data.totalElements", equalTo(0));
        }
    }

    @Nested
    @DisplayName("PUT /v2/admin/common-code-types/{id} - 공통 코드 타입 수정")
    class UpdateTest {

        @Test
        @DisplayName("공통 코드 타입 수정 성공")
        void shouldUpdateSuccessfully() {
            // given
            CommonCodeTypeJpaEntity saved =
                    commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            Long typeId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("name", "수정된 이름");
            request.put("description", "수정된 설명");
            request.put("displayOrder", 99);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/" + typeId)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // 변경 확인
            CommonCodeTypeJpaEntity updated =
                    commonCodeTypeJpaRepository.findById(typeId).orElseThrow();
            assertThat(updated.getName()).isEqualTo("수정된 이름");
            assertThat(updated.getDescription()).isEqualTo("수정된 설명");
            assertThat(updated.getDisplayOrder()).isEqualTo(99);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 수정 시 404 에러 반환")
        void shouldReturn404WhenTypeNotFound() {
            // given - DB에 없는 ID
            Long nonExistentId = 999999L;

            Map<String, Object> request = new HashMap<>();
            request.put("name", "수정된 이름");
            request.put("displayOrder", 1);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/" + nonExistentId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("name 누락 시 400 에러 반환")
        void shouldReturn400WhenNameMissing() {
            // given
            CommonCodeTypeJpaEntity saved =
                    commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            Long typeId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("description", "설명만 있음");
            request.put("displayOrder", 1);
            // name 누락

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/" + typeId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("PATCH /v2/admin/common-code-types/active-status - 활성화 상태 변경")
    class ChangeActiveStatusTest {

        @Test
        @DisplayName("활성화 상태 변경 성공")
        void shouldChangeActiveStatusSuccessfully() {
            // given
            CommonCodeTypeJpaEntity saved =
                    commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            Long typeId = saved.getId();
            assertThat(saved.isActive()).isTrue();

            Map<String, Object> request = new HashMap<>();
            request.put("ids", List.of(typeId));
            request.put("active", false);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/active-status")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // 변경 확인
            CommonCodeTypeJpaEntity updated =
                    commonCodeTypeJpaRepository.findById(typeId).orElseThrow();
            assertThat(updated.isActive()).isFalse();
        }

        @Test
        @DisplayName("ids 빈 리스트 전달 시 400 에러 반환")
        void shouldReturn400WhenIdsIsEmpty() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("ids", List.of());
            request.put("active", false);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/active-status")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("active 필드 누락 시 400 에러 반환")
        void shouldReturn400WhenActiveIsMissing() {
            // given
            CommonCodeTypeJpaEntity saved =
                    commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
            Long typeId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("ids", List.of(typeId));
            // active 누락

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/active-status")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("여러 타입을 한 번에 활성화 성공")
        void shouldChangeMultipleTypesActiveStatusSuccessfully() {
            // given - 비활성 타입 2개 저장
            CommonCodeTypeJpaEntity type1 =
                    commonCodeTypeJpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newInactiveEntity());
            CommonCodeTypeJpaEntity type2 =
                    commonCodeTypeJpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newInactiveEntity());

            Map<String, Object> request = new HashMap<>();
            request.put("ids", List.of(type1.getId(), type2.getId()));
            request.put("active", true);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/active-status")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // 변경 확인
            assertThat(commonCodeTypeJpaRepository.findById(type1.getId()).orElseThrow().isActive())
                    .isTrue();
            assertThat(commonCodeTypeJpaRepository.findById(type2.getId()).orElseThrow().isActive())
                    .isTrue();
        }
    }

    // ===== Helper Methods =====

    private Map<String, Object> createRegisterRequest(String suffix) {
        Map<String, Object> request = new HashMap<>();
        request.put("code", "TEST_CODE_" + suffix);
        request.put("name", "테스트 타입 " + suffix);
        request.put("description", "테스트 설명입니다.");
        request.put("displayOrder", 1);
        return request;
    }
}
