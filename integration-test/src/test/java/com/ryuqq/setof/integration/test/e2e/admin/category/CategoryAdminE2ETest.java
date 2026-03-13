package com.ryuqq.setof.integration.test.e2e.admin.category;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryJpaRepository;
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
 * Category Admin E2E 통합 테스트.
 *
 * <p>카테고리 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 *
 * <p>현재 v1 카테고리 엔드포인트는 rest-api(web) 모듈에만 존재하며 rest-api-admin에는 없음. v2 Admin 컨트롤러 추가 시 활성화 필요.
 */
@Disabled("v1 카테고리 엔드포인트는 rest-api-admin 모듈에 존재하지 않음 - v2 Admin 컨트롤러 추가 시 활성화")
@Tag(TestTags.CATEGORY)
@DisplayName("카테고리 Admin API E2E 테스트")
class CategoryAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v1/category";
    private static final String PAGE_PATH = "/v1/category/page";

    @Autowired private CategoryJpaRepository categoryJpaRepository;

    @BeforeEach
    void setUp() {
        categoryJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /api/v1/category - 전체 카테고리 트리 조회")
    class GetAllCategoriesAsTreeTest {

        @Test
        @DisplayName("전체 카테고리 트리 조회 성공")
        void shouldGetAllCategoriesAsTree() {
            // given - 루트 카테고리 생성
            CategoryJpaEntity root =
                    categoryJpaRepository.save(CategoryJpaEntityFixtures.newRootEntity());

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", notNullValue())
                    .body("data", hasSize(greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("빈 카테고리 트리 조회")
        void shouldReturnEmptyTree() {
            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(0));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/category/{categoryId} - 자식 카테고리 조회")
    class GetChildCategoriesTest {

        @Test
        @DisplayName("자식 카테고리 조회 성공")
        void shouldGetChildCategories() {
            // given - 부모 카테고리와 자식 카테고리 생성
            CategoryJpaEntity parent =
                    categoryJpaRepository.save(CategoryJpaEntityFixtures.newRootEntity());
            categoryJpaRepository.save(CategoryJpaEntityFixtures.newChildEntity(parent.getId(), 2));
            categoryJpaRepository.save(CategoryJpaEntityFixtures.newChildEntity(parent.getId(), 2));

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/" + parent.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", notNullValue());
        }

        @Test
        @DisplayName("자식이 없는 카테고리 조회")
        void shouldReturnEmptyChildList() {
            // given - 자식이 없는 카테고리
            CategoryJpaEntity category =
                    categoryJpaRepository.save(CategoryJpaEntityFixtures.newRootEntity());

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/" + category.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(0));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/category/parent/{categoryId} - 부모 카테고리 조회")
    class GetParentCategoriesTest {

        @Test
        @DisplayName("부모 카테고리 조회 성공")
        void shouldGetParentCategories() {
            // given - 부모-자식 계층 구조 생성
            CategoryJpaEntity parent =
                    categoryJpaRepository.save(CategoryJpaEntityFixtures.newRootEntity());
            CategoryJpaEntity child =
                    categoryJpaRepository.save(
                            CategoryJpaEntityFixtures.newChildEntity(parent.getId(), 2));

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/parent/" + child.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", notNullValue());
        }

        @Test
        @DisplayName("루트 카테고리의 부모 조회")
        void shouldReturnEmptyForRootCategory() {
            // given - 루트 카테고리
            CategoryJpaEntity root =
                    categoryJpaRepository.save(CategoryJpaEntityFixtures.newRootEntity());

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/parent/" + root.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", notNullValue());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/category/page - 카테고리 목록 페이징 조회")
    class SearchCategoriesTest {

        @Test
        @DisplayName("전체 목록 조회 성공")
        void shouldSearchAllCategories() {
            // given
            categoryJpaRepository.save(
                    CategoryJpaEntityFixtures.activeEntityWithName("카테고리1", "카테고리1 표시명"));
            categoryJpaRepository.save(
                    CategoryJpaEntityFixtures.activeEntityWithName("카테고리2", "카테고리2 표시명"));

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(PAGE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(2))
                    .body("data.number", equalTo(0))
                    .body("data.size", equalTo(20));
        }

        @Test
        @DisplayName("카테고리명으로 검색 성공")
        void shouldSearchByCategoryName() {
            // given
            categoryJpaRepository.save(
                    CategoryJpaEntityFixtures.activeEntityWithName("일반카테고리", "일반 표시명"));
            categoryJpaRepository.save(
                    CategoryJpaEntityFixtures.activeEntityWithName("특별카테고리", "특별 표시명"));

            // when & then
            givenAdmin()
                    .queryParam("categoryName", "특별카테고리")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(PAGE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("페이징 처리 확인")
        void shouldPaginateCorrectly() {
            // given
            for (int i = 0; i < 5; i++) {
                categoryJpaRepository.save(
                        CategoryJpaEntityFixtures.activeEntityWithName("카테고리" + i, "표시명" + i));
            }

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 2)
                    .when()
                    .get(PAGE_PATH)
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
            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(PAGE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(0))
                    .body("data.totalElements", equalTo(0));
        }

        @Test
        @DisplayName("조회 결과에 필수 필드 포함 확인")
        void shouldContainRequiredFields() {
            // given
            categoryJpaRepository.save(
                    CategoryJpaEntityFixtures.activeEntityWithName("테스트카테고리", "테스트 표시명"));

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(PAGE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content[0].categoryId", notNullValue())
                    .body("data.content[0].categoryName", notNullValue())
                    .body("data.content[0].displayName", notNullValue());
        }
    }
}
