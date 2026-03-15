package com.ryuqq.setof.integration.test.e2e.web.contentpage;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.ryuqq.setof.adapter.out.persistence.contentpage.ContentPageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageJpaRepository;
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
 * ContentPage Web API E2E 통합 테스트.
 *
 * <p>GET /api/v1/content/* 전체 흐름을 테스트합니다.
 */
@Tag(TestTags.CONTENT_PAGE)
@DisplayName("콘텐츠 페이지 Web API E2E 테스트")
class ContentPageE2ETest extends E2ETestBase {

    private static final String ON_DISPLAY_PATH = "/v1/content/on-display";
    private static final String META_PATH = "/v1/content/meta";
    private static final String DETAIL_PATH = "/v1/content";

    @Autowired private ContentPageJpaRepository contentPageJpaRepository;

    @BeforeEach
    void setUp() {
        contentPageJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("GET /v1/content/on-display - 전시 중인 콘텐츠 ID 조회")
    class OnDisplayTest {

        @Test
        @DisplayName("전시 중인 콘텐츠 ID 목록을 반환한다")
        void shouldReturnOnDisplayContentIds() {
            // given
            contentPageJpaRepository.save(ContentPageJpaEntityFixtures.activeEntity(null));
            contentPageJpaRepository.save(ContentPageJpaEntityFixtures.activeEntity(null));

            // when & then
            givenJson()
                    .when()
                    .get(ON_DISPLAY_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.contentIds", hasSize(greaterThanOrEqualTo(1)));
        }

        @Test
        @DisplayName("전시 콘텐츠가 없으면 빈 Set을 반환한다")
        void shouldReturnEmptySetWhenNoContent() {
            givenJson()
                    .when()
                    .get(ON_DISPLAY_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.contentIds", empty());
        }

        @Test
        @DisplayName("비활성 콘텐츠는 전시 목록에 포함되지 않는다")
        void shouldNotIncludeInactiveContent() {
            // given
            contentPageJpaRepository.save(ContentPageJpaEntityFixtures.activeEntity(null));
            Instant now = Instant.now();
            contentPageJpaRepository.save(
                    ContentPageJpaEntity.create(
                            null,
                            "비활성콘텐츠",
                            "memo",
                            "https://example.com/img.png",
                            ContentPageJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                            ContentPageJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                            false,
                            now,
                            now,
                            null));

            // when & then
            givenJson()
                    .when()
                    .get(ON_DISPLAY_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.contentIds", hasSize(1));
        }
    }

    @Nested
    @DisplayName("GET /v1/content/meta/{contentId} - 콘텐츠 메타 조회")
    class MetaTest {

        @Test
        @DisplayName("콘텐츠 메타 정보를 정상 조회한다")
        void shouldReturnContentMeta() {
            // given
            ContentPageJpaEntity saved =
                    contentPageJpaRepository.save(ContentPageJpaEntityFixtures.activeEntity(null));

            // when & then
            givenJson()
                    .when()
                    .get(META_PATH + "/" + saved.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.contentId", equalTo(saved.getId().intValue()))
                    .body("data.title", notNullValue())
                    .body("data.componentDetails", hasSize(0));
        }

        @Test
        @DisplayName("존재하지 않는 contentId로 조회 시 논리적 404를 반환한다")
        void shouldReturn404WhenContentNotFound() {
            // V1 레거시 호환: HTTP 200 + result.status_code=404
            givenJson()
                    .when()
                    .get(META_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", nullValue())
                    .body("response.status", equalTo(404));
        }
    }

    @Nested
    @DisplayName("GET /v1/content/{contentId} - 콘텐츠 상세 조회")
    class DetailTest {

        @Test
        @DisplayName("콘텐츠 상세를 정상 조회한다")
        void shouldReturnContentDetail() {
            // given
            ContentPageJpaEntity saved =
                    contentPageJpaRepository.save(ContentPageJpaEntityFixtures.activeEntity(null));

            // when & then
            givenJson()
                    .when()
                    .get(DETAIL_PATH + "/" + saved.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.contentId", equalTo(saved.getId().intValue()))
                    .body("data.title", notNullValue())
                    .body("data.displayPeriod", notNullValue());
        }

        @Test
        @DisplayName("bypass=Y로 전시 기간 무관하게 조회한다")
        void shouldReturnContentDetailWithBypass() {
            // given
            ContentPageJpaEntity saved =
                    contentPageJpaRepository.save(ContentPageJpaEntityFixtures.activeEntity(null));

            // when & then
            givenJson()
                    .queryParam("bypass", "Y")
                    .when()
                    .get(DETAIL_PATH + "/" + saved.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.contentId", equalTo(saved.getId().intValue()));
        }

        @Test
        @DisplayName("존재하지 않는 contentId로 상세 조회 시 논리적 404를 반환한다")
        void shouldReturn404WhenContentNotFound() {
            // V1 레거시 호환: HTTP 200 + result.status_code=404
            givenJson()
                    .when()
                    .get(DETAIL_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", nullValue())
                    .body("response.status", equalTo(404));
        }

        @Test
        @DisplayName("삭제된 콘텐츠는 조회되지 않는다")
        void shouldNotReturnDeletedContent() {
            // given
            Instant now = Instant.now();
            ContentPageJpaEntity deleted =
                    contentPageJpaRepository.save(
                            ContentPageJpaEntity.create(
                                    null,
                                    "삭제된콘텐츠",
                                    "memo",
                                    "https://example.com/img.png",
                                    ContentPageJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                                    ContentPageJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                                    false,
                                    now,
                                    now,
                                    now));

            // when & then - V1 레거시 호환: HTTP 200 + response.status=404
            givenJson()
                    .when()
                    .get(DETAIL_PATH + "/" + deleted.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data", nullValue())
                    .body("response.status", equalTo(404));
        }
    }
}
