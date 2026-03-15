package com.ryuqq.setof.integration.test.e2e.web.banner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.ryuqq.setof.adapter.out.persistence.banner.BannerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideJpaRepository;
import com.ryuqq.setof.integration.test.common.base.E2ETestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * Banner Web API E2E 통합 테스트.
 *
 * <p>GET /api/v1/content/banner 전체 흐름을 테스트합니다.
 */
@Tag(TestTags.BANNER)
@DisplayName("배너 Web API E2E 테스트")
class BannerE2ETest extends E2ETestBase {

    private static final String BASE_PATH = "/v1/content/banner";

    @Autowired private BannerGroupJpaRepository bannerGroupJpaRepository;
    @Autowired private BannerSlideJpaRepository bannerSlideJpaRepository;

    @BeforeEach
    void setUp() {
        bannerSlideJpaRepository.deleteAll();
        bannerGroupJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /v1/content/banner - 배너 슬라이드 조회")
    class GetBannersTest {

        @Test
        @DisplayName("활성 배너 그룹의 슬라이드를 정상 조회한다")
        void shouldReturnBannerSlides() {
            // given
            BannerGroupJpaEntity group =
                    bannerGroupJpaRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, group.getId()));
            bannerSlideJpaRepository.save(
                    BannerJpaEntityFixtures.activeSlideEntity(null, group.getId()));

            // when & then
            givenJson()
                    .queryParam("bannerType", "RECOMMEND")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", hasSize(greaterThanOrEqualTo(1)))
                    .body("data[0].title", notNullValue())
                    .body("data[0].imageUrl", notNullValue());
        }

        @Test
        @DisplayName("슬라이드가 없는 bannerType 요청 시 논리적 404를 반환한다")
        void shouldReturn404WhenNoBannerSlides() {
            // given - 데이터 없음

            // when & then - V1 레거시 호환: HTTP 200 + result.status_code=404
            givenJson()
                    .queryParam("bannerType", "CATEGORY")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", nullValue())
                    .body("response.status", equalTo(404));
        }

        @Test
        @DisplayName("bannerType 파라미터 누락 시 400을 반환한다")
        void shouldReturn400WhenBannerTypeMissing() {
            givenJson().when().get(BASE_PATH).then().statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("잘못된 bannerType 값 전달 시 400을 반환한다")
        void shouldReturn400WhenInvalidBannerType() {
            givenJson()
                    .queryParam("bannerType", "INVALID_TYPE")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("비활성 슬라이드는 조회되지 않는다")
        void shouldNotReturnInactiveSlides() {
            // given - 비활성 슬라이드만 존재
            BannerGroupJpaEntity group =
                    bannerGroupJpaRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerSlideJpaEntity inactiveSlide = BannerJpaEntityFixtures.inactiveSlideEntity();
            // inactive slide의 group id를 맞춰야 하므로 새로 생성
            bannerSlideJpaRepository.save(
                    BannerSlideJpaEntity.create(
                            null,
                            group.getId(),
                            "비활성 슬라이드",
                            "https://example.com/img.png",
                            "https://example.com/link",
                            1,
                            BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                            BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                            false,
                            java.time.Instant.now(),
                            java.time.Instant.now(),
                            null));

            // when & then - V1 레거시 호환: HTTP 200 + result.status_code=404
            givenJson()
                    .queryParam("bannerType", "RECOMMEND")
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", nullValue())
                    .body("response.status", equalTo(404));
        }
    }
}
