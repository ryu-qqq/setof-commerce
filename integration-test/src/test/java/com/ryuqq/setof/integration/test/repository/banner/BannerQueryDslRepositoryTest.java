package com.ryuqq.setof.integration.test.repository.banner;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.banner.BannerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideQueryDslRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Banner QueryDSL Repository 통합 테스트.
 *
 * <p>BannerSlideQueryDslRepository의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>BannerGroup과 BannerSlide JOIN 조회
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>active 조건 필터링
 *   <li>displayOrder 정렬
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.BANNER)
@DisplayName("배너 QueryDSL Repository 테스트")
class BannerQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private BannerGroupJpaRepository groupRepository;
    @Autowired private BannerSlideJpaRepository slideRepository;
    @Autowired private BannerSlideQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("fetchDisplaySlides 테스트")
    class FetchDisplaySlidesTest {

        @Test
        @DisplayName("활성 그룹의 활성 슬라이드를 조회합니다")
        void shouldFetchActiveSlidesFromActiveGroup() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerSlideJpaEntity slide =
                    slideRepository.save(BannerJpaEntityFixtures.newSlideEntity(group.getId()));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.fetchDisplaySlides(
                            BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE);

            // then
            assertThat(result).isNotEmpty();
            assertThat(result).anyMatch(s -> s.getId().equals(slide.getId()));
        }

        @Test
        @DisplayName("삭제된 배너 그룹의 슬라이드는 조회되지 않습니다")
        void shouldNotFetchSlidesFromDeletedGroup() {
            // given
            BannerGroupJpaEntity deletedGroup = groupRepository.save(createDeletedGroup());
            slideRepository.save(BannerJpaEntityFixtures.newSlideEntity(deletedGroup.getId()));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.fetchDisplaySlides(
                            BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE);

            // then - 삭제된 그룹의 슬라이드는 포함되지 않음
            List<Long> groupIds =
                    result.stream().map(BannerSlideJpaEntity::getBannerGroupId).toList();
            assertThat(groupIds).doesNotContain(deletedGroup.getId());
        }

        @Test
        @DisplayName("비활성 배너 그룹의 슬라이드는 조회되지 않습니다")
        void shouldNotFetchSlidesFromInactiveGroup() {
            // given
            BannerGroupJpaEntity inactiveGroup = groupRepository.save(createInactiveGroup());
            BannerSlideJpaEntity slide =
                    slideRepository.save(
                            BannerJpaEntityFixtures.newSlideEntity(inactiveGroup.getId()));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.fetchDisplaySlides(
                            BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE);

            // then
            List<Long> slideIds = result.stream().map(BannerSlideJpaEntity::getId).toList();
            assertThat(slideIds).doesNotContain(slide.getId());
        }

        @Test
        @DisplayName("삭제된 슬라이드는 조회되지 않습니다")
        void shouldNotFetchDeletedSlide() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerSlideJpaEntity deletedSlide =
                    slideRepository.save(createDeletedSlide(group.getId()));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.fetchDisplaySlides(
                            BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE);

            // then
            List<Long> slideIds = result.stream().map(BannerSlideJpaEntity::getId).toList();
            assertThat(slideIds).doesNotContain(deletedSlide.getId());
        }

        @Test
        @DisplayName("비활성 슬라이드는 조회되지 않습니다")
        void shouldNotFetchInactiveSlide() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerSlideJpaEntity inactiveSlide =
                    slideRepository.save(createInactiveSlide(group.getId()));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.fetchDisplaySlides(
                            BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE);

            // then
            List<Long> slideIds = result.stream().map(BannerSlideJpaEntity::getId).toList();
            assertThat(slideIds).doesNotContain(inactiveSlide.getId());
        }

        @Test
        @DisplayName("다른 배너 타입의 슬라이드는 조회되지 않습니다")
        void shouldNotFetchSlidesWithDifferentBannerType() {
            // given
            Instant now = Instant.now();
            BannerGroupJpaEntity otherTypeGroup =
                    groupRepository.save(
                            BannerGroupJpaEntity.create(
                                    null,
                                    "다른타입그룹",
                                    "CATEGORY",
                                    now.minusSeconds(3600),
                                    now.plusSeconds(86400),
                                    true,
                                    now,
                                    now,
                                    null));
            BannerSlideJpaEntity slide =
                    slideRepository.save(
                            BannerJpaEntityFixtures.newSlideEntity(otherTypeGroup.getId()));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result = queryDslRepository.fetchDisplaySlides("RECOMMEND");

            // then
            List<Long> slideIds = result.stream().map(BannerSlideJpaEntity::getId).toList();
            assertThat(slideIds).doesNotContain(slide.getId());
        }

        @Test
        @DisplayName("슬라이드는 displayOrder 오름차순으로 정렬됩니다")
        void shouldReturnSlidesOrderedByDisplayOrder() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            Instant now = Instant.now();
            BannerSlideJpaEntity slide3 =
                    slideRepository.save(createSlideWithOrder(group.getId(), 3));
            BannerSlideJpaEntity slide1 =
                    slideRepository.save(createSlideWithOrder(group.getId(), 1));
            BannerSlideJpaEntity slide2 =
                    slideRepository.save(createSlideWithOrder(group.getId(), 2));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.fetchDisplaySlides(
                            BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE);

            // then
            List<Integer> orders =
                    result.stream().map(BannerSlideJpaEntity::getDisplayOrder).toList();
            assertThat(orders).isSortedAccordingTo(Integer::compareTo);
        }
    }

    // ========================================================================
    // Helper Methods
    // ========================================================================

    private BannerGroupJpaEntity createDeletedGroup() {
        Instant now = Instant.now();
        return BannerGroupJpaEntity.create(
                null,
                BannerJpaEntityFixtures.DEFAULT_TITLE,
                BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                now);
    }

    private BannerGroupJpaEntity createInactiveGroup() {
        Instant now = Instant.now();
        return BannerGroupJpaEntity.create(
                null,
                BannerJpaEntityFixtures.DEFAULT_TITLE,
                BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }

    private BannerSlideJpaEntity createDeletedSlide(long groupId) {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                null,
                groupId,
                BannerJpaEntityFixtures.DEFAULT_SLIDE_TITLE,
                BannerJpaEntityFixtures.DEFAULT_IMAGE_URL,
                BannerJpaEntityFixtures.DEFAULT_LINK_URL,
                1,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                now);
    }

    private BannerSlideJpaEntity createInactiveSlide(long groupId) {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                null,
                groupId,
                BannerJpaEntityFixtures.DEFAULT_SLIDE_TITLE,
                BannerJpaEntityFixtures.DEFAULT_IMAGE_URL,
                BannerJpaEntityFixtures.DEFAULT_LINK_URL,
                1,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                false,
                now,
                now,
                null);
    }

    private BannerSlideJpaEntity createSlideWithOrder(long groupId, int displayOrder) {
        Instant now = Instant.now();
        return BannerSlideJpaEntity.create(
                null,
                groupId,
                BannerJpaEntityFixtures.DEFAULT_SLIDE_TITLE,
                BannerJpaEntityFixtures.DEFAULT_IMAGE_URL,
                BannerJpaEntityFixtures.DEFAULT_LINK_URL,
                displayOrder,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                true,
                now,
                now,
                null);
    }
}
