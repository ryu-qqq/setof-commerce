package com.ryuqq.setof.integration.test.e2e.admin.gnb;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.navigation.NavigationMenuJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.navigation.repository.NavigationMenuJpaRepository;
import com.ryuqq.setof.integration.test.common.base.AdminE2ETestBase;
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
 * GNB Admin E2E 통합 테스트.
 *
 * <p>GNB 조회 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 *
 * <p>대상 엔드포인트:
 *
 * <ul>
 *   <li>GET /api/v1/content/gnbs — GNB 목록 조회 (날짜 필터, 키워드 검색)
 * </ul>
 */
@Tag(TestTags.NAVIGATION)
@DisplayName("GNB Admin API E2E 테스트")
class GnbAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v1/content/gnbs";

    @Autowired private NavigationMenuJpaRepository navigationMenuJpaRepository;

    @BeforeEach
    void setUp() {
        navigationMenuJpaRepository.deleteAll();
    }

    // ===== Query 테스트 - GNB 목록 조회 =====

    @Nested
    @DisplayName("GET /api/v1/content/gnbs - GNB 목록 조회")
    class GetGnbsTest {

        @Test
        @DisplayName("전체 GNB 목록을 정상 조회한다")
        void shouldReturnAllGnbs() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("홈", 1));
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("카테고리", 2));
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("마이페이지", 3));

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(3));
        }

        @Test
        @DisplayName("GNB가 없으면 빈 목록을 반환한다")
        void shouldReturnEmptyListWhenNoGnbs() {
            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(0));
        }

        @Test
        @DisplayName("조회 결과에 필수 응답 필드가 포함된다")
        void shouldContainRequiredResponseFields() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("신상품", 1));

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data[0].gnbId", notNullValue())
                    .body("data[0].gnbDetails", notNullValue())
                    .body("data[0].gnbDetails.title", notNullValue())
                    .body("data[0].gnbDetails.linkUrl", notNullValue())
                    .body("data[0].gnbDetails.displayOrder", notNullValue())
                    .body("data[0].gnbDetails.displayYn", notNullValue())
                    .body("data[0].gnbDetails.displayPeriod", notNullValue())
                    .body("data[0].gnbDetails.displayPeriod.displayStartDate", notNullValue())
                    .body("data[0].gnbDetails.displayPeriod.displayEndDate", notNullValue());
        }

        @Test
        @DisplayName("활성 GNB와 비활성 GNB가 모두 존재할 때 결과를 반환한다")
        void shouldReturnGnbsIncludingInactive() {
            // given - 활성 2개, 비활성 1개
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("홈", 1));
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("카테고리", 2));
            navigationMenuJpaRepository.save(NavigationMenuJpaEntityFixtures.inactiveEntity());

            // when & then - Admin에서는 비활성 포함 조회 가능
            givenAdmin().when().get(BASE_PATH).then().statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("전시 여부가 Y인 GNB의 displayYn 필드 값을 확인한다")
        void shouldReturnCorrectDisplayYnForActiveGnb() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("활성 메뉴", 1));

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data[0].gnbDetails.displayYn", equalTo("Y"));
        }

        @Test
        @DisplayName("전시 기간 필터로 조회한다 (startDate, endDate 파라미터)")
        void shouldFilterByDateRange() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("날짜 필터 테스트 메뉴", 1));

            // when & then - 전시 기간 파라미터 포함하여 요청
            givenAdmin()
                    .queryParam("startDate", "2025-01-01 00:00:00")
                    .queryParam("endDate", "2025-12-31 23:59:59")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("startDate만 전달해도 정상 응답한다")
        void shouldReturnOkWhenOnlyStartDateProvided() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("메뉴", 1));

            // when & then
            givenAdmin()
                    .queryParam("startDate", "2025-01-01 00:00:00")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("endDate만 전달해도 정상 응답한다")
        void shouldReturnOkWhenOnlyEndDateProvided() {
            // given
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("메뉴", 1));

            // when & then
            givenAdmin()
                    .queryParam("endDate", "2025-12-31 23:59:59")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("잘못된 날짜 형식으로 요청 시 400을 반환한다")
        void shouldReturn400WhenInvalidDateFormat() {
            // when & then - 잘못된 날짜 형식
            givenAdmin()
                    .queryParam("startDate", "2025/01/01")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    // ===== 전체 플로우 시나리오 =====

    @Nested
    @DisplayName("전체 플로우 시나리오")
    class FullFlowTest {

        @Test
        @DisplayName("전체 GNB 목록 조회 후 날짜 필터 조회 플로우")
        void shouldSearchAllGnbsThenFilterByDateFlow() {
            // 1단계: GNB 3개 생성
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("홈", 1));
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("신상품", 2));
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("이벤트", 3));

            // 2단계: 전체 조회 → 3개 확인
            givenAdmin()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(3));

            // 3단계: 날짜 필터 조회 (fixtures 기본 표시 기간 내에 포함되는 날짜)
            givenAdmin()
                    .queryParam("startDate", "2025-01-01 00:00:00")
                    .queryParam("endDate", "2099-12-31 23:59:59")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("비활성 포함된 GNB 목록에서 필드 검증 플로우")
        void shouldVerifyFieldsWithMixedStatusGnbsFlow() {
            // 1단계: 활성 + 비활성 GNB 생성
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntityFixtures.activeEntityWithTitle("활성 메뉴", 1));
            Instant now = Instant.now();
            navigationMenuJpaRepository.save(
                    NavigationMenuJpaEntity.create(
                            null,
                            "비활성 메뉴",
                            "https://example.com/inactive",
                            2,
                            NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                            NavigationMenuJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                            false,
                            now,
                            now,
                            null));

            // 2단계: 전체 조회 시 응답 구조 검증
            givenAdmin()
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data[0].gnbId", notNullValue())
                    .body("data[0].gnbDetails.title", notNullValue())
                    .body("data[0].gnbDetails.displayPeriod.displayStartDate", notNullValue());
        }
    }
}
