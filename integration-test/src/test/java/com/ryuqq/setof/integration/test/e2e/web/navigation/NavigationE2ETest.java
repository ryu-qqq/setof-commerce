package com.ryuqq.setof.integration.test.e2e.web.navigation;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.navigation.NavigationMenuJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuJpaRepository;
import com.ryuqq.setof.integration.test.common.base.E2ETestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * Navigation(GNB) Web API E2E 통합 테스트.
 *
 * <p>GET /api/v1/content/gnbs 전체 흐름을 테스트합니다.
 */
@Tag(TestTags.NAVIGATION)
@DisplayName("네비게이션 Web API E2E 테스트")
class NavigationE2ETest extends E2ETestBase {

    private static final String BASE_PATH = "/v1/content/gnbs";

    @Autowired private NavigationMenuJpaRepository navigationMenuJpaRepository;

    @BeforeEach
    void setUp() {
        navigationMenuJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /v1/content/gnbs - GNB 메뉴 조회")
    class GetGnbsTest {

        @Test
        @DisplayName("활성 네비게이션 메뉴를 정상 조회한다")
        void shouldReturnNavigationMenus() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("홈", 1));
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("카테고리", 2));
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("마이페이지", 3));

            // when & then
            givenJson()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(3))
                    .body("data[0].title", notNullValue())
                    .body("data[0].linkUrl", notNullValue());
        }

        @Test
        @DisplayName("메뉴가 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyListWhenNoMenus() {
            // given - 데이터 없음

            // when & then
            givenJson()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(0));
        }

        @Test
        @DisplayName("비활성 메뉴는 조회되지 않는다")
        void shouldNotReturnInactiveMenus() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("활성메뉴", 1));
            Instant now = Instant.now();
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntity.create(
                            null,
                            "비활성메뉴",
                            "https://example.com/inactive",
                            2,
                            NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                            NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                            false,
                            now,
                            now,
                            null));

            // when & then
            givenJson()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(1))
                    .body("data[0].title", equalTo("활성메뉴"));
        }

        @Test
        @DisplayName("삭제된 메뉴는 조회되지 않는다")
        void shouldNotReturnDeletedMenus() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("정상메뉴", 1));
            Instant now = Instant.now();
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntity.create(
                            null,
                            "삭제된메뉴",
                            "https://example.com/deleted",
                            2,
                            NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                            NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                            false,
                            now,
                            now,
                            now));

            // when & then
            givenJson()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(1));
        }
    }
}
