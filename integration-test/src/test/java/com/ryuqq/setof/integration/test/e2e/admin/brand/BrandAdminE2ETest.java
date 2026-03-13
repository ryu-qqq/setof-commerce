package com.ryuqq.setof.integration.test.e2e.admin.brand;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandJpaRepository;
import com.ryuqq.setof.integration.test.common.base.AdminE2ETestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * Brand Admin E2E 통합 테스트.
 *
 * <p>브랜드 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 *
 * <p>현재 v1 브랜드 엔드포인트는 rest-api(web) 모듈에만 존재하며 rest-api-admin에는 없음. v2 Admin 컨트롤러 추가 시 활성화 필요.
 */
@Disabled("v1 브랜드 엔드포인트는 rest-api-admin 모듈에 존재하지 않음 - v2 Admin 컨트롤러 추가 시 활성화")
@Tag(TestTags.BRAND)
@DisplayName("브랜드 Admin API E2E 테스트")
class BrandAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v1/brands";

    @Autowired private BrandJpaRepository brandJpaRepository;

    @BeforeEach
    void setUp() {
        brandJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /api/v1/brands - 브랜드 목록 조회")
    class SearchTest {

        @Test
        @DisplayName("전체 목록 조회 성공")
        void shouldSearchAllBrands() {
            // given
            brandJpaRepository.save(
                    BrandJpaEntityFixtures.activeEntityWithName("브랜드1", "브랜드1 표시명"));
            brandJpaRepository.save(
                    BrandJpaEntityFixtures.activeEntityWithName("브랜드2", "브랜드2 표시명"));

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
                    .body("data.number", equalTo(0))
                    .body("data.size", equalTo(20));
        }

        @Test
        @DisplayName("브랜드명으로 검색 성공")
        void shouldSearchByBrandName() {
            // given
            brandJpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("일반브랜드", "일반 표시명"));
            brandJpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("특별브랜드", "특별 표시명"));

            // when & then
            givenAdmin()
                    .queryParam("brandName", "특별브랜드")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("페이징 처리 확인")
        void shouldPaginateCorrectly() {
            // given
            for (int i = 0; i < 5; i++) {
                brandJpaRepository.save(
                        BrandJpaEntityFixtures.activeEntityWithName("브랜드" + i, "표시명" + i));
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
                    .body("data.number", equalTo(0))
                    .body("data.size", equalTo(2));
        }

        @Test
        @DisplayName("빈 결과 조회")
        void shouldReturnEmptyResult() {
            // when & then (empty result)
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

        @Test
        @DisplayName("활성 상태 필터링 조회")
        void shouldSearchByActiveStatus() {
            // given
            brandJpaRepository.save(BrandJpaEntityFixtures.activeEntityWithName("활성브랜드", "활성 표시명"));
            brandJpaRepository.save(BrandJpaEntityFixtures.newInactiveEntity());

            // when & then - 활성 브랜드만 조회
            givenAdmin()
                    .queryParam("displayed", true)
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("조회 결과에 필수 필드 포함 확인")
        void shouldContainRequiredFields() {
            // given
            brandJpaRepository.save(
                    BrandJpaEntityFixtures.activeEntityWithName("테스트브랜드", "테스트 표시명"));

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content[0].brandId", notNullValue())
                    .body("data.content[0].brandName", notNullValue())
                    .body("data.content[0].displayEnglishName", notNullValue());
        }
    }
}
