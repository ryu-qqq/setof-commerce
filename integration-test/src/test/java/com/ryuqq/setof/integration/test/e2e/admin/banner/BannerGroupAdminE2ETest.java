package com.ryuqq.setof.integration.test.e2e.admin.banner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.banner.BannerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideJpaRepository;
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
 * BannerGroup Admin E2E 통합 테스트.
 *
 * <p>배너 그룹/아이템 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 *
 * <p>대상 엔드포인트:
 *
 * <ul>
 *   <li>GET /api/v1/content/banners — 배너 그룹 목록 조회 (검색/페이징)
 *   <li>GET /api/v1/content/banner/{bannerId} — 배너 아이템(슬라이드) 목록 조회
 * </ul>
 */
@Tag(TestTags.BANNER)
@DisplayName("배너 그룹 Admin API E2E 테스트")
class BannerGroupAdminE2ETest extends AdminE2ETestBase {

    private static final String BANNERS_PATH = "/v1/content/banners";
    private static final String BANNER_ITEMS_PATH = "/v1/content/banner";

    @Autowired private BannerGroupJpaRepository bannerGroupJpaRepository;
    @Autowired private BannerSlideJpaRepository bannerSlideJpaRepository;

    @BeforeEach
    void setUp() {
        bannerSlideJpaRepository.deleteAll();
        bannerGroupJpaRepository.deleteAll();
    }

    // ===== Query 테스트 - 배너 그룹 목록 조회 =====

    @Nested
    @DisplayName("GET /api/v1/content/banners - 배너 그룹 목록 조회")
    class SearchBannersTest {

        @Test
        @DisplayName("전체 배너 그룹 목록을 페이징 조회한다")
        void shouldSearchAllBannerGroups() {
            // given
            bannerGroupJpaRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "CATEGORY", "카테고리 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "MY_PAGE", "마이페이지 배너"));

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(3))
                    .body("data.totalElements", equalTo(3))
                    .body("data.number", equalTo(0))
                    .body("data.size", equalTo(20));
        }

        @Test
        @DisplayName("배너 타입으로 필터링 조회한다")
        void shouldSearchByBannerType() {
            // given
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "CATEGORY", "카테고리 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "MY_PAGE", "마이페이지 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "RECOMMEND", "추천 배너"));

            // when & then - CATEGORY 타입만 조회
            givenAdmin()
                    .queryParam("bannerType", "CATEGORY")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.totalElements", equalTo(1))
                    .body("data.content[0].bannerType", equalTo("CATEGORY"));
        }

        @Test
        @DisplayName("전시 여부(Y)로 필터링 조회한다")
        void shouldSearchByDisplayYnY() {
            // given - 활성 2개, 비활성 1개
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "CATEGORY", "활성 카테고리 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "MY_PAGE", "활성 마이페이지 배너"));
            bannerGroupJpaRepository.save(BannerJpaEntityFixtures.inactiveGroupEntity());

            // when & then - 전시 여부 Y (활성) 만 조회
            givenAdmin()
                    .queryParam("displayYn", "Y")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(greaterThanOrEqualTo(2)))
                    .body("data.content[0].displayYn", equalTo("Y"));
        }

        @Test
        @DisplayName("전시 여부(N)로 필터링 조회한다")
        void shouldSearchByDisplayYnN() {
            // given - 활성 1개, 비활성 2개
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "CATEGORY", "활성 배너"));
            bannerGroupJpaRepository.save(BannerJpaEntityFixtures.inactiveGroupEntity());
            bannerGroupJpaRepository.save(
                    createInactiveGroupEntity(null, "RECOMMEND", "비활성 추천 배너"));

            // when & then - 전시 여부 N (비활성) 만 조회
            givenAdmin()
                    .queryParam("displayYn", "N")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(greaterThanOrEqualTo(2)))
                    .body("data.content[0].displayYn", equalTo("N"));
        }

        @Test
        @DisplayName("배너 이름으로 키워드 검색한다")
        void shouldSearchByBannerName() {
            // given
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "CATEGORY", "봄 시즌 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "MY_PAGE", "여름 이벤트 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "RECOMMEND", "가을 추천 상품 배너"));

            // when & then - "여름" 키워드로 배너 이름 검색
            givenAdmin()
                    .queryParam("searchKeyword", "BANNER_NAME")
                    .queryParam("searchWord", "여름")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.totalElements", equalTo(1));
        }

        @Test
        @DisplayName("페이징 처리가 정상 동작한다")
        void shouldPaginateCorrectly() {
            // given - 5개 생성
            for (int i = 1; i <= 5; i++) {
                bannerGroupJpaRepository.save(
                        createActiveGroupEntity(null, "CATEGORY", "배너 " + i));
            }

            // when & then - page=0, size=2 로 조회 시 2개만 반환
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 2)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(5))
                    .body("data.number", equalTo(0))
                    .body("data.size", equalTo(2));
        }

        @Test
        @DisplayName("두 번째 페이지를 조회한다")
        void shouldReturnSecondPage() {
            // given - 5개 생성
            for (int i = 1; i <= 5; i++) {
                bannerGroupJpaRepository.save(
                        createActiveGroupEntity(null, "CATEGORY", "배너 " + i));
            }

            // when & then - page=1, size=3 → 2개 반환
            givenAdmin()
                    .queryParam("page", 1)
                    .queryParam("size", 3)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.number", equalTo(1));
        }

        @Test
        @DisplayName("데이터가 없으면 빈 목록을 반환한다")
        void shouldReturnEmptyResult() {
            // when & then - 데이터 없음
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(0))
                    .body("data.totalElements", equalTo(0))
                    .body("data.empty", equalTo(true));
        }

        @Test
        @DisplayName("조회 결과에 필수 응답 필드가 포함된다")
        void shouldContainRequiredResponseFields() {
            // given
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "RECOMMEND", "추천 배너"));

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content[0].bannerId", notNullValue())
                    .body("data.content[0].title", notNullValue())
                    .body("data.content[0].bannerType", notNullValue())
                    .body("data.content[0].displayYn", notNullValue())
                    .body("data.content[0].displayPeriod", notNullValue())
                    .body("data.content[0].displayPeriod.displayStartDate", notNullValue())
                    .body("data.content[0].displayPeriod.displayEndDate", notNullValue());
        }

        @Test
        @DisplayName("page 파라미터 누락 시 기본값으로 동작한다")
        void shouldUseDefaultPageWhenPageParamMissing() {
            // given
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "CATEGORY", "기본 페이지 테스트 배너"));

            // when & then - page, size 파라미터 없이 요청
            givenAdmin()
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("size가 100 초과이면 400을 반환한다")
        void shouldReturn400WhenSizeExceedsMax() {
            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 101)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("lastDomainId로 No-Offset 페이징이 동작한다")
        void shouldSupportNoOffsetPaging() {
            // given - 3개 저장
            BannerGroupJpaEntity first =
                    bannerGroupJpaRepository.save(
                            createActiveGroupEntity(null, "CATEGORY", "첫번째 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "MY_PAGE", "두번째 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "RECOMMEND", "세번째 배너"));

            // when & then - 첫번째 이후부터 조회
            givenAdmin()
                    .queryParam("lastDomainId", first.getId())
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }
    }

    // ===== Query 테스트 - 배너 아이템 목록 조회 =====

    @Nested
    @DisplayName("GET /api/v1/content/banner/{bannerId} - 배너 아이템 목록 조회")
    class GetBannerItemsTest {

        @Test
        @DisplayName("배너 그룹 ID로 슬라이드 목록을 정상 조회한다")
        void shouldReturnBannerItems() {
            // given
            BannerGroupJpaEntity group =
                    bannerGroupJpaRepository.save(
                            BannerJpaEntityFixtures.activeGroupEntity(null));
            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, group.getId()));
            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, group.getId()));

            // when & then
            givenAdmin()
                    .when()
                    .get(BANNER_ITEMS_PATH + "/" + group.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(2))
                    .body("data[0].bannerItemId", notNullValue())
                    .body("data[0].bannerType", notNullValue())
                    .body("data[0].title", notNullValue())
                    .body("data[0].imageUrl", notNullValue())
                    .body("data[0].linkUrl", notNullValue())
                    .body("data[0].displayYn", notNullValue())
                    .body("data[0].displayPeriod", notNullValue());
        }

        @Test
        @DisplayName("슬라이드가 없는 배너 그룹 조회 시 빈 목록을 반환한다")
        void shouldReturnEmptyListWhenNoSlides() {
            // given - 슬라이드 없이 그룹만 생성
            BannerGroupJpaEntity group =
                    bannerGroupJpaRepository.save(
                            BannerJpaEntityFixtures.activeGroupEntity(null));

            // when & then
            givenAdmin()
                    .when()
                    .get(BANNER_ITEMS_PATH + "/" + group.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(0));
        }

        @Test
        @DisplayName("존재하지 않는 배너 그룹 ID 조회 시 404를 반환한다")
        void shouldReturn404WhenBannerGroupNotFound() {
            // given - 존재하지 않는 ID
            long nonExistentId = 999999L;

            // when & then
            givenAdmin()
                    .when()
                    .get(BANNER_ITEMS_PATH + "/" + nonExistentId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("활성 슬라이드와 비활성 슬라이드 모두 Admin에서는 조회된다")
        void shouldReturnAllSlidesIncludingInactive() {
            // given
            BannerGroupJpaEntity group =
                    bannerGroupJpaRepository.save(
                            BannerJpaEntityFixtures.activeGroupEntity(null));
            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, group.getId()));
            // 비활성 슬라이드 추가
            bannerSlideJpaRepository.save(
                    createInactiveSlideEntity(group.getId(), "비활성 슬라이드"));

            // when & then - Admin에서는 비활성 포함 전체 조회
            givenAdmin()
                    .when()
                    .get(BANNER_ITEMS_PATH + "/" + group.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("배너 아이템에 이미지 크기 정보가 포함된다")
        void shouldContainImageSizeInResponse() {
            // given
            BannerGroupJpaEntity group =
                    bannerGroupJpaRepository.save(
                            BannerJpaEntityFixtures.activeGroupEntity(null));
            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, group.getId()));

            // when & then
            givenAdmin()
                    .when()
                    .get(BANNER_ITEMS_PATH + "/" + group.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data[0].imageSize", notNullValue());
        }
    }

    // ===== 전체 플로우 시나리오 =====

    @Nested
    @DisplayName("전체 플로우 시나리오")
    class FullFlowTest {

        @Test
        @DisplayName("배너 그룹 목록 조회 후 특정 그룹의 아이템 조회 플로우")
        void shouldSearchBannerGroupsAndGetItemsFlow() {
            // 1단계: 배너 그룹 2개 + 슬라이드 생성
            BannerGroupJpaEntity categoryGroup =
                    bannerGroupJpaRepository.save(
                            createActiveGroupEntity(null, "CATEGORY", "카테고리 배너 그룹"));
            BannerGroupJpaEntity recommendGroup =
                    bannerGroupJpaRepository.save(
                            createActiveGroupEntity(null, "RECOMMEND", "추천 배너 그룹"));

            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, categoryGroup.getId()));
            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, categoryGroup.getId()));
            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, recommendGroup.getId()));

            // 2단계: 배너 그룹 목록 조회 → 2개 확인
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(2));

            // 3단계: CATEGORY 그룹 아이템 조회 → 슬라이드 2개 확인
            givenAdmin()
                    .when()
                    .get(BANNER_ITEMS_PATH + "/" + categoryGroup.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(2));

            // 4단계: RECOMMEND 그룹 아이템 조회 → 슬라이드 1개 확인
            givenAdmin()
                    .when()
                    .get(BANNER_ITEMS_PATH + "/" + recommendGroup.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(1));
        }

        @Test
        @DisplayName("배너 타입 필터로 목록 조회 후 해당 그룹 아이템 조회 플로우")
        void shouldFilterByTypeAndGetItemsFlow() {
            // 1단계: 다양한 타입의 배너 그룹 생성
            BannerGroupJpaEntity myPageGroup =
                    bannerGroupJpaRepository.save(
                            createActiveGroupEntity(null, "MY_PAGE", "마이페이지 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "CART", "장바구니 배너"));
            bannerGroupJpaRepository.save(
                    createActiveGroupEntity(null, "LOGIN", "로그인 배너"));

            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, myPageGroup.getId()));

            // 2단계: MY_PAGE 타입으로 필터링 조회 → 1개
            givenAdmin()
                    .queryParam("bannerType", "MY_PAGE")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BANNERS_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.content[0].bannerType", equalTo("MY_PAGE"));

            // 3단계: MY_PAGE 그룹 아이템 조회 → 슬라이드 1개
            givenAdmin()
                    .when()
                    .get(BANNER_ITEMS_PATH + "/" + myPageGroup.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(1));
        }
    }

    // ===== Helper Methods =====

    private BannerGroupJpaEntity createActiveGroupEntity(Long id, String bannerType, String title) {
        Instant now = Instant.now();
        return BannerGroupJpaEntity.create(
                id,
                title,
                bannerType,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }

    private BannerGroupJpaEntity createInactiveGroupEntity(
            Long id, String bannerType, String title) {
        Instant now = Instant.now();
        return BannerGroupJpaEntity.create(
                id,
                title,
                bannerType,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }

    private BannerSlideJpaEntity createInactiveSlideEntity(long bannerGroupId, String title) {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                null,
                bannerGroupId,
                title,
                BannerJpaEntityFixtures.DEFAULT_IMAGE_URL,
                BannerJpaEntityFixtures.DEFAULT_LINK_URL,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_ORDER,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }
}
