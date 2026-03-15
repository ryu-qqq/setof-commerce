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

    @Nested
    @DisplayName("findBannerGroupById 테스트")
    class FindBannerGroupByIdTest {

        @Test
        @DisplayName("존재하는 배너 그룹 ID로 조회 성공")
        void shouldFindBannerGroupById() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            flushAndClear();

            // when
            BannerGroupJpaEntity result = queryDslRepository.findBannerGroupById(group.getId());

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(group.getId());
            assertThat(result.getTitle()).isEqualTo(BannerJpaEntityFixtures.DEFAULT_TITLE);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 null을 반환합니다")
        void shouldReturnNullWhenNotFound() {
            // when
            BannerGroupJpaEntity result = queryDslRepository.findBannerGroupById(999999L);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("findSlidesByGroupId 테스트")
    class FindSlidesByGroupIdTest {

        @Test
        @DisplayName("배너 그룹에 속한 슬라이드 목록을 displayOrder 오름차순으로 반환합니다")
        void shouldFindSlidesByGroupIdOrderedByDisplayOrder() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerSlideJpaEntity slide3 =
                    slideRepository.save(createSlideWithOrder(group.getId(), 3));
            BannerSlideJpaEntity slide1 =
                    slideRepository.save(createSlideWithOrder(group.getId(), 1));
            BannerSlideJpaEntity slide2 =
                    slideRepository.save(createSlideWithOrder(group.getId(), 2));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.findSlidesByGroupId(group.getId());

            // then
            assertThat(result).hasSize(3);
            List<Integer> orders =
                    result.stream().map(BannerSlideJpaEntity::getDisplayOrder).toList();
            assertThat(orders).isSortedAccordingTo(Integer::compareTo);
        }

        @Test
        @DisplayName("다른 그룹의 슬라이드는 조회되지 않습니다")
        void shouldNotFetchSlidesFromOtherGroup() {
            // given
            BannerGroupJpaEntity group1 =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerGroupJpaEntity group2 =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            slideRepository.save(BannerJpaEntityFixtures.newSlideEntity(group1.getId()));
            BannerSlideJpaEntity slide2 =
                    slideRepository.save(BannerJpaEntityFixtures.newSlideEntity(group2.getId()));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.findSlidesByGroupId(group1.getId());

            // then
            List<Long> slideIds = result.stream().map(BannerSlideJpaEntity::getId).toList();
            assertThat(slideIds).doesNotContain(slide2.getId());
        }

        @Test
        @DisplayName("슬라이드가 없는 그룹은 빈 목록을 반환합니다")
        void shouldReturnEmptyListWhenNoSlides() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            flushAndClear();

            // when
            List<BannerSlideJpaEntity> result =
                    queryDslRepository.findSlidesByGroupId(group.getId());

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("searchBannerGroups 테스트")
    class SearchBannerGroupsTest {

        @Test
        @DisplayName("전체 조회 시 삭제된 배너 그룹은 제외됩니다")
        void shouldExcludeDeletedGroupsFromSearch() {
            // given
            BannerGroupJpaEntity active =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerGroupJpaEntity deleted = groupRepository.save(createDeletedGroup());
            flushAndClear();

            // when
            List<BannerGroupJpaEntity> result =
                    queryDslRepository.searchBannerGroups(
                            null, null, null, null, null, null, 0, 20, false);

            // then
            List<Long> ids = result.stream().map(BannerGroupJpaEntity::getId).toList();
            assertThat(ids).contains(active.getId());
            assertThat(ids).doesNotContain(deleted.getId());
        }

        @Test
        @DisplayName("active=true 조건으로 활성 그룹만 조회합니다")
        void shouldFilterByActiveStatus() {
            // given
            BannerGroupJpaEntity activeGroup =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerGroupJpaEntity inactiveGroup = groupRepository.save(createInactiveGroup());
            flushAndClear();

            // when
            List<BannerGroupJpaEntity> result =
                    queryDslRepository.searchBannerGroups(
                            null, true, null, null, null, null, 0, 20, false);

            // then
            List<Long> ids = result.stream().map(BannerGroupJpaEntity::getId).toList();
            assertThat(ids).contains(activeGroup.getId());
            assertThat(ids).doesNotContain(inactiveGroup.getId());
        }

        @Test
        @DisplayName("배너 타입 조건으로 특정 타입의 그룹만 조회합니다")
        void shouldFilterByBannerType() {
            // given
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
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
            flushAndClear();

            // when
            List<BannerGroupJpaEntity> result =
                    queryDslRepository.searchBannerGroups(
                            "CATEGORY", null, null, null, null, null, 0, 20, false);

            // then
            List<Long> ids = result.stream().map(BannerGroupJpaEntity::getId).toList();
            assertThat(ids).containsExactly(otherTypeGroup.getId());
        }

        @Test
        @DisplayName("제목 검색어로 포함 검색합니다")
        void shouldFilterByTitleKeyword() {
            // given
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            Instant now = Instant.now();
            BannerGroupJpaEntity specificGroup =
                    groupRepository.save(
                            BannerGroupJpaEntity.create(
                                    null,
                                    "특정검색어포함제목",
                                    BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE,
                                    now.minusSeconds(3600),
                                    now.plusSeconds(86400),
                                    true,
                                    now,
                                    now,
                                    null));
            flushAndClear();

            // when
            List<BannerGroupJpaEntity> result =
                    queryDslRepository.searchBannerGroups(
                            null, null, null, null, "특정검색어", null, 0, 20, false);

            // then
            List<Long> ids = result.stream().map(BannerGroupJpaEntity::getId).toList();
            assertThat(ids).containsExactly(specificGroup.getId());
        }

        @Test
        @DisplayName("No-Offset 페이징: lastDomainId 미만의 ID를 가진 그룹만 반환합니다")
        void shouldApplyNoOffsetPaging() {
            // given
            Instant now = Instant.now();
            BannerGroupJpaEntity group1 =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerGroupJpaEntity group2 =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerGroupJpaEntity group3 =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            flushAndClear();

            // when - group3의 ID 미만인 것들만 조회
            List<BannerGroupJpaEntity> result =
                    queryDslRepository.searchBannerGroups(
                            null, null, null, null, null, group3.getId(), 0, 20, true);

            // then
            List<Long> ids = result.stream().map(BannerGroupJpaEntity::getId).toList();
            assertThat(ids).doesNotContain(group3.getId());
            assertThat(ids).contains(group1.getId(), group2.getId());
        }

        @Test
        @DisplayName("Offset 페이징으로 크기를 제한합니다")
        void shouldApplyOffsetPaging() {
            // given
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            flushAndClear();

            // when
            List<BannerGroupJpaEntity> result =
                    queryDslRepository.searchBannerGroups(
                            null, null, null, null, null, null, 0, 2, false);

            // then
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("결과가 없으면 빈 목록을 반환합니다")
        void shouldReturnEmptyListWhenNoMatch() {
            // when
            List<BannerGroupJpaEntity> result =
                    queryDslRepository.searchBannerGroups(
                            null, null, null, null, "존재하지않는제목", null, 0, 20, false);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countBannerGroups 테스트")
    class CountBannerGroupsTest {

        @Test
        @DisplayName("삭제된 그룹을 제외하고 카운트합니다")
        void shouldCountExcludingDeletedGroups() {
            // given
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            groupRepository.save(createDeletedGroup());
            flushAndClear();

            // when
            long count = queryDslRepository.countBannerGroups(null, null, null, null, null);

            // then
            assertThat(count).isEqualTo(2);
        }

        @Test
        @DisplayName("active=true 조건으로 활성 그룹만 카운트합니다")
        void shouldCountByActiveStatus() {
            // given
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            groupRepository.save(createInactiveGroup());
            flushAndClear();

            // when
            long count = queryDslRepository.countBannerGroups(null, true, null, null, null);

            // then
            assertThat(count).isEqualTo(2);
        }

        @Test
        @DisplayName("제목 키워드 조건으로 카운트합니다")
        void shouldCountByTitleKeyword() {
            // given
            groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            Instant now = Instant.now();
            groupRepository.save(
                    BannerGroupJpaEntity.create(
                            null,
                            "특이한제목",
                            BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE,
                            now.minusSeconds(3600),
                            now.plusSeconds(86400),
                            true,
                            now,
                            now,
                            null));
            flushAndClear();

            // when
            long count = queryDslRepository.countBannerGroups(null, null, null, null, "특이한제목");

            // then
            assertThat(count).isEqualTo(1);
        }

        @Test
        @DisplayName("데이터가 없으면 0을 반환합니다")
        void shouldReturnZeroWhenNoData() {
            // when
            long count = queryDslRepository.countBannerGroups(null, null, null, null, null);

            // then
            assertThat(count).isZero();
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
