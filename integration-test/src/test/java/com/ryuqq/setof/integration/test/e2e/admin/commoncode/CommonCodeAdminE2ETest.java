package com.ryuqq.setof.integration.test.e2e.admin.commoncode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import com.ryuqq.setof.adapter.out.persistence.commoncode.CommonCodeJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.commoncode.entity.CommonCodeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.commoncode.repository.CommonCodeJpaRepository;
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
 * CommonCode Admin E2E 통합 테스트.
 *
 * <p>공통 코드 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.COMMON_CODE)
@DisplayName("공통 코드 Admin API E2E 테스트")
class CommonCodeAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/admin/common-codes";

    @Autowired private CommonCodeJpaRepository commonCodeJpaRepository;
    @Autowired private CommonCodeTypeJpaRepository commonCodeTypeJpaRepository;

    private Long savedTypeId;

    @BeforeEach
    void setUp() {
        // 외래키 제약 순서에 맞게 삭제
        commonCodeJpaRepository.deleteAll();
        commonCodeTypeJpaRepository.deleteAll();

        // 공통 코드가 참조할 타입 생성
        CommonCodeTypeJpaEntity savedType =
                commonCodeTypeJpaRepository.save(CommonCodeTypeJpaEntityFixtures.newEntity());
        savedTypeId = savedType.getId();
    }

    @Nested
    @DisplayName("POST /v2/admin/common-codes - 공통 코드 등록")
    class RegisterTest {

        @Test
        @DisplayName("유효한 요청으로 공통 코드 등록 성공")
        void shouldRegisterSuccessfully() {
            // given
            String uniqueSuffix = String.valueOf(System.currentTimeMillis());
            Map<String, Object> request = createRegisterRequest(savedTypeId, uniqueSuffix);

            // when
            Response response = givenAdmin().body(request).when().post(BASE_PATH);

            // then
            response.then().statusCode(HttpStatus.CREATED.value()).body("data", greaterThan(0));

            Long createdId = response.jsonPath().getLong("data");
            assertThat(commonCodeJpaRepository.findById(createdId)).isPresent();
        }

        @Test
        @DisplayName("필수 필드 누락시 400 에러 반환")
        void shouldReturn400WhenRequiredFieldMissing() {
            // given - code 누락
            Map<String, Object> request = new HashMap<>();
            request.put("commonCodeTypeId", savedTypeId);
            request.put("displayName", "테스트 표시명");
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
            // given - 기존 코드 생성
            CommonCodeJpaEntity existing =
                    commonCodeJpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId));
            String existingCode = existing.getCode();

            // 동일한 타입 내에서 동일한 코드로 등록 시도
            Map<String, Object> request = new HashMap<>();
            request.put("commonCodeTypeId", savedTypeId);
            request.put("code", existingCode);
            request.put("displayName", "중복 테스트");
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
    @DisplayName("GET /v2/admin/common-codes - 공통 코드 목록 조회")
    class SearchTest {

        @Test
        @DisplayName("전체 목록 조회 성공")
        void shouldSearchAllCodes() {
            // given
            commonCodeJpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            savedTypeId, "CODE_1", "코드1"));
            commonCodeJpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            savedTypeId, "CODE_2", "코드2"));

            // when & then
            givenAdmin()
                    .queryParam("commonCodeTypeId", savedTypeId)
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
        @DisplayName("타입별 필터링 조회 성공")
        void shouldSearchByTypeId() {
            // given - 다른 타입도 생성
            CommonCodeTypeJpaEntity anotherType =
                    commonCodeTypeJpaRepository.save(
                            CommonCodeTypeJpaEntityFixtures.newEntityWithCode(
                                    "ANOTHER_TYPE", "다른타입"));

            commonCodeJpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            savedTypeId, "CODE_1", "코드1"));
            commonCodeJpaRepository.save(
                    CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                            anotherType.getId(), "CODE_2", "코드2"));

            // when & then - 특정 타입만 조회
            givenAdmin()
                    .queryParam("commonCodeTypeId", savedTypeId)
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
                commonCodeJpaRepository.save(
                        CommonCodeJpaEntityFixtures.newEntityWithTypeIdAndCode(
                                savedTypeId, "CODE_" + i, "코드" + i));
            }

            // when & then
            givenAdmin()
                    .queryParam("commonCodeTypeId", savedTypeId)
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
                    .queryParam("commonCodeTypeId", savedTypeId)
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
    @DisplayName("PUT /v2/admin/common-codes/{id} - 공통 코드 수정")
    class UpdateTest {

        @Test
        @DisplayName("공통 코드 수정 성공")
        void shouldUpdateSuccessfully() {
            // given
            CommonCodeJpaEntity saved =
                    commonCodeJpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId));
            Long codeId = saved.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("displayName", "수정된 표시명");
            request.put("displayOrder", 99);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/" + codeId)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // 변경 확인
            CommonCodeJpaEntity updated = commonCodeJpaRepository.findById(codeId).orElseThrow();
            assertThat(updated.getDisplayName()).isEqualTo("수정된 표시명");
            assertThat(updated.getDisplayOrder()).isEqualTo(99);
        }
    }

    @Nested
    @DisplayName("PATCH /v2/admin/common-codes/active-status - 활성화 상태 변경")
    class ChangeActiveStatusTest {

        @Test
        @DisplayName("활성화 상태 변경 성공")
        void shouldChangeActiveStatusSuccessfully() {
            // given
            CommonCodeJpaEntity saved =
                    commonCodeJpaRepository.save(
                            CommonCodeJpaEntityFixtures.newEntityWithTypeId(savedTypeId));
            Long codeId = saved.getId();
            assertThat(saved.isActive()).isTrue();

            Map<String, Object> request = new HashMap<>();
            request.put("ids", List.of(codeId));
            request.put("active", false);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/active-status")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // 변경 확인
            CommonCodeJpaEntity updated = commonCodeJpaRepository.findById(codeId).orElseThrow();
            assertThat(updated.isActive()).isFalse();
        }
    }

    // ===== Helper Methods =====

    private Map<String, Object> createRegisterRequest(Long typeId, String suffix) {
        Map<String, Object> request = new HashMap<>();
        request.put("commonCodeTypeId", typeId);
        request.put("code", "TEST_" + suffix);
        request.put("displayName", "테스트 " + suffix);
        request.put("displayOrder", 1);
        return request;
    }
}
