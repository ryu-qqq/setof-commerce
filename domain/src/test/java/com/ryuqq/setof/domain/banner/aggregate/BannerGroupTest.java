package com.ryuqq.setof.domain.banner.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerSlideDiff;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BannerGroup Aggregate 테스트")
class BannerGroupTest {

    @Nested
    @DisplayName("forNew() - 신규 배너 그룹 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 배너 그룹을 생성한다")
        void createNewBannerGroup() {
            // when
            var bannerGroup = BannerFixtures.newBannerGroup();

            // then
            assertThat(bannerGroup.id().isNew()).isTrue();
            assertThat(bannerGroup.title()).isEqualTo(BannerFixtures.DEFAULT_TITLE);
            assertThat(bannerGroup.bannerType()).isEqualTo(BannerFixtures.DEFAULT_BANNER_TYPE);
            assertThat(bannerGroup.isActive()).isTrue();
            assertThat(bannerGroup.isDeleted()).isFalse();
            assertThat(bannerGroup.slides()).isNotEmpty();
        }

        @Test
        @DisplayName("커스텀 값으로 배너 그룹을 생성한다")
        void createWithCustomValues() {
            // when
            var bannerGroup = BannerFixtures.newBannerGroup("커스텀 배너", BannerType.CATEGORY);

            // then
            assertThat(bannerGroup.title()).isEqualTo("커스텀 배너");
            assertThat(bannerGroup.bannerType()).isEqualTo(BannerType.CATEGORY);
        }

        @Test
        @DisplayName("신규 생성 시 DeletionStatus가 active 상태이다")
        void newBannerGroupHasActiveDeletionStatus() {
            // when
            var bannerGroup = BannerFixtures.newBannerGroup();

            // then
            assertThat(bannerGroup.deletionStatus().isDeleted()).isFalse();
            assertThat(bannerGroup.deletionStatus().isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 배너 그룹을 복원한다")
        void reconstituteActiveBannerGroup() {
            // when
            var bannerGroup = BannerFixtures.activeBannerGroup();

            // then
            assertThat(bannerGroup.id().isNew()).isFalse();
            assertThat(bannerGroup.idValue()).isEqualTo(1L);
            assertThat(bannerGroup.isActive()).isTrue();
            assertThat(bannerGroup.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("비활성 상태의 배너 그룹을 복원한다")
        void reconstituteInactiveBannerGroup() {
            // when
            var bannerGroup = BannerFixtures.inactiveBannerGroup();

            // then
            assertThat(bannerGroup.idValue()).isEqualTo(2L);
            assertThat(bannerGroup.isActive()).isFalse();
            assertThat(bannerGroup.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 배너 그룹을 복원한다")
        void reconstituteDeletedBannerGroup() {
            // when
            var bannerGroup = BannerFixtures.deletedBannerGroup();

            // then
            assertThat(bannerGroup.isDeleted()).isTrue();
            assertThat(bannerGroup.deletionStatus().deletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("update() - 배너 그룹 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("배너 그룹 정보를 수정한다")
        void updateBannerGroup() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();
            var updateData = BannerFixtures.defaultBannerGroupUpdateData();

            // when
            bannerGroup.update(updateData);

            // then
            assertThat(bannerGroup.title()).isEqualTo("수정된 배너 그룹");
            assertThat(bannerGroup.bannerType()).isEqualTo(BannerType.CATEGORY);
            assertThat(bannerGroup.isActive()).isFalse();
        }

        @Test
        @DisplayName("수정 시 슬라이드 목록이 교체된다")
        void updateReplacesSlides() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();
            var slideEntries =
                    List.of(
                            new BannerGroupUpdateData.SlideEntry(
                                    null,
                                    "새 슬라이드 1",
                                    "img1.jpg",
                                    "link1",
                                    1,
                                    BannerFixtures.defaultDisplayPeriod(),
                                    true),
                            new BannerGroupUpdateData.SlideEntry(
                                    null,
                                    "새 슬라이드 2",
                                    "img2.jpg",
                                    "link2",
                                    2,
                                    BannerFixtures.defaultDisplayPeriod(),
                                    true));
            var updateData =
                    new BannerGroupUpdateData(
                            "수정된 배너 그룹",
                            BannerType.CATEGORY,
                            BannerFixtures.defaultDisplayPeriod(),
                            true,
                            slideEntries,
                            CommonVoFixtures.now());

            // when
            bannerGroup.update(updateData);

            // then
            assertThat(bannerGroup.slides()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("update() Diff 패턴 - 슬라이드 변경 비교")
    class UpdateDiffTest {

        @Test
        @DisplayName("slideId가 null인 엔트리만 있으면 모두 added에 존재한다")
        void onlyNewSlideEntriesGoToAdded() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();
            var updateData =
                    new BannerGroupUpdateData(
                            BannerFixtures.DEFAULT_TITLE,
                            BannerFixtures.DEFAULT_BANNER_TYPE,
                            BannerFixtures.defaultDisplayPeriod(),
                            true,
                            BannerFixtures.newOnlySlideEntries(),
                            CommonVoFixtures.now());

            // when
            BannerSlideDiff diff = bannerGroup.update(updateData);

            // then
            assertThat(diff.added()).hasSize(2);
            assertThat(diff.retained()).isEmpty();
            assertThat(diff.removed()).hasSize(1);
            diff.added().forEach(slide -> assertThat(slide.id().isNew()).isTrue());
        }

        @Test
        @DisplayName("기존 슬라이드 ID를 포함한 엔트리는 retained에 존재하고 속성이 수정된다")
        void existingSlideIdEntryGoesToRetainedWithUpdatedAttributes() {
            // given
            Long existingSlideId = 10L;
            var bannerGroup = BannerFixtures.activeBannerGroupWithSlides(existingSlideId, 20L);
            var updateData =
                    new BannerGroupUpdateData(
                            BannerFixtures.DEFAULT_TITLE,
                            BannerFixtures.DEFAULT_BANNER_TYPE,
                            BannerFixtures.defaultDisplayPeriod(),
                            true,
                            BannerFixtures.updateExistingSlideEntries(existingSlideId),
                            CommonVoFixtures.now());

            // when
            BannerSlideDiff diff = bannerGroup.update(updateData);

            // then
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.added()).isEmpty();

            BannerSlide retainedSlide = diff.retained().get(0);
            assertThat(retainedSlide.idValue()).isEqualTo(existingSlideId);
            assertThat(retainedSlide.title()).isEqualTo("수정된 슬라이드");
            assertThat(retainedSlide.imageUrl()).isEqualTo("https://example.com/updated-image.png");
            assertThat(retainedSlide.displayOrder()).isEqualTo(5);
            assertThat(retainedSlide.isActive()).isFalse();
        }

        @Test
        @DisplayName("요청에 없는 기존 슬라이드 ID는 removed에 존재하고 soft delete 상태이다")
        void existingSlideNotInRequestGoesToRemovedWithSoftDelete() {
            // given
            Long keepSlideId = 10L;
            Long deleteSlideId = 20L;
            var bannerGroup =
                    BannerFixtures.activeBannerGroupWithSlides(keepSlideId, deleteSlideId);
            var updateData =
                    new BannerGroupUpdateData(
                            BannerFixtures.DEFAULT_TITLE,
                            BannerFixtures.DEFAULT_BANNER_TYPE,
                            BannerFixtures.defaultDisplayPeriod(),
                            true,
                            BannerFixtures.updateExistingSlideEntries(keepSlideId),
                            CommonVoFixtures.now());

            // when
            BannerSlideDiff diff = bannerGroup.update(updateData);

            // then
            assertThat(diff.removed()).hasSize(1);
            BannerSlide removedSlide = diff.removed().get(0);
            assertThat(removedSlide.idValue()).isEqualTo(deleteSlideId);
            assertThat(removedSlide.isDeleted()).isTrue();
            assertThat(removedSlide.isActive()).isFalse();
            assertThat(removedSlide.deletionStatus().deletedAt()).isNotNull();
        }

        @Test
        @DisplayName("신규 추가 + 기존 수정 + 기존 삭제가 동시에 처리된다")
        void mixedScenarioHandlesAllThreeOperationsSimultaneously() {
            // given
            Long retainSlideId = 10L;
            Long deleteSlideId = 20L;
            var bannerGroup =
                    BannerFixtures.activeBannerGroupWithSlides(retainSlideId, deleteSlideId);
            var updateData =
                    new BannerGroupUpdateData(
                            "혼합 수정 배너",
                            BannerType.CATEGORY,
                            BannerFixtures.defaultDisplayPeriod(),
                            false,
                            BannerFixtures.mixedSlideEntries(retainSlideId),
                            CommonVoFixtures.now());

            // when
            BannerSlideDiff diff = bannerGroup.update(updateData);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);

            assertThat(diff.added().get(0).id().isNew()).isTrue();
            assertThat(diff.retained().get(0).idValue()).isEqualTo(retainSlideId);
            assertThat(diff.retained().get(0).title()).isEqualTo("수정된 기존 슬라이드");
            assertThat(diff.removed().get(0).idValue()).isEqualTo(deleteSlideId);
            assertThat(diff.removed().get(0).isDeleted()).isTrue();

            assertThat(bannerGroup.title()).isEqualTo("혼합 수정 배너");
            assertThat(bannerGroup.isActive()).isFalse();
        }

        @Test
        @DisplayName("빈 엔트리 목록으로 수정하면 모든 기존 슬라이드가 removed 처리된다")
        void emptyEntriesRemoveAllExistingSlides() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroupWithSlides(10L, 20L);
            var updateData =
                    new BannerGroupUpdateData(
                            BannerFixtures.DEFAULT_TITLE,
                            BannerFixtures.DEFAULT_BANNER_TYPE,
                            BannerFixtures.defaultDisplayPeriod(),
                            true,
                            BannerFixtures.emptySlideEntries(),
                            CommonVoFixtures.now());

            // when
            BannerSlideDiff diff = bannerGroup.update(updateData);

            // then
            assertThat(diff.removed()).hasSize(2);
            assertThat(diff.added()).isEmpty();
            assertThat(diff.retained()).isEmpty();
            diff.removed().forEach(slide -> assertThat(slide.isDeleted()).isTrue());
            assertThat(bannerGroup.slides()).isEmpty();
        }

        @Test
        @DisplayName("update() 후 bannerGroup.slides()는 retained + added만 포함한다")
        void afterUpdateSlidesContainsOnlyRetainedAndAdded() {
            // given
            Long retainSlideId = 10L;
            Long deleteSlideId = 20L;
            var bannerGroup =
                    BannerFixtures.activeBannerGroupWithSlides(retainSlideId, deleteSlideId);
            var updateData =
                    new BannerGroupUpdateData(
                            BannerFixtures.DEFAULT_TITLE,
                            BannerFixtures.DEFAULT_BANNER_TYPE,
                            BannerFixtures.defaultDisplayPeriod(),
                            true,
                            BannerFixtures.mixedSlideEntries(retainSlideId),
                            CommonVoFixtures.now());

            // when
            BannerSlideDiff diff = bannerGroup.update(updateData);

            // then
            int expectedSize = diff.retained().size() + diff.added().size();
            assertThat(bannerGroup.slides()).hasSize(expectedSize);
        }
    }

    @Nested
    @DisplayName("changeDisplayStatus() - 노출 상태 변경")
    class ChangeDisplayStatusTest {

        @Test
        @DisplayName("배너 그룹을 비활성화한다")
        void deactivateBannerGroup() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();
            Instant now = CommonVoFixtures.now();

            // when
            bannerGroup.changeDisplayStatus(false, now);

            // then
            assertThat(bannerGroup.isActive()).isFalse();
            assertThat(bannerGroup.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("비활성 배너 그룹을 활성화한다")
        void activateBannerGroup() {
            // given
            var bannerGroup = BannerFixtures.inactiveBannerGroup();
            Instant now = CommonVoFixtures.now();

            // when
            bannerGroup.changeDisplayStatus(true, now);

            // then
            assertThat(bannerGroup.isActive()).isTrue();
            assertThat(bannerGroup.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("remove() - 배너 그룹 삭제 (Soft Delete)")
    class RemoveTest {

        @Test
        @DisplayName("배너 그룹을 소프트 삭제한다")
        void removeBannerGroup() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();
            Instant now = CommonVoFixtures.now();

            // when
            bannerGroup.remove(now);

            // then
            assertThat(bannerGroup.isDeleted()).isTrue();
            assertThat(bannerGroup.isActive()).isFalse();
            assertThat(bannerGroup.updatedAt()).isEqualTo(now);
            assertThat(bannerGroup.deletionStatus().deletedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("addSlide() / replaceSlides() - 슬라이드 관리")
    class SlideManagementTest {

        @Test
        @DisplayName("슬라이드를 추가한다")
        void addSlide() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();
            int originalSize = bannerGroup.slides().size();
            BannerSlide newSlide = BannerFixtures.newBannerSlide("추가 슬라이드", 2);

            // when
            bannerGroup.addSlide(newSlide);

            // then
            assertThat(bannerGroup.slides()).hasSize(originalSize + 1);
        }

        @Test
        @DisplayName("슬라이드 목록을 전체 교체한다")
        void replaceSlides() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();
            List<BannerSlide> newSlides =
                    List.of(
                            BannerFixtures.newBannerSlide("슬라이드 A", 1),
                            BannerFixtures.newBannerSlide("슬라이드 B", 2),
                            BannerFixtures.newBannerSlide("슬라이드 C", 3));

            // when
            bannerGroup.replaceSlides(newSlides);

            // then
            assertThat(bannerGroup.slides()).hasSize(3);
        }

        @Test
        @DisplayName("빈 목록으로 슬라이드를 교체하면 슬라이드가 없어진다")
        void replaceSlidesWithEmpty() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();

            // when
            bannerGroup.replaceSlides(List.of());

            // then
            assertThat(bannerGroup.slides()).isEmpty();
        }

        @Test
        @DisplayName("slides()는 수정 불가능한 리스트를 반환한다")
        void slidesReturnsUnmodifiableList() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();

            // when & then
            assertThat(bannerGroup.slides()).isNotNull();
            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> bannerGroup.slides().add(BannerFixtures.newBannerSlide()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("isDisplayable() - 노출 가능 여부 판단")
    class IsDisplayableTest {

        @Test
        @DisplayName("활성 상태이고 기간 내이면 노출 가능하다")
        void displayableWhenActiveAndWithinPeriod() {
            // given
            var bannerGroup = BannerFixtures.activeBannerGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThat(bannerGroup.isDisplayable(now)).isTrue();
        }

        @Test
        @DisplayName("비활성 상태이면 노출 불가하다")
        void notDisplayableWhenInactive() {
            // given
            var bannerGroup = BannerFixtures.inactiveBannerGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThat(bannerGroup.isDisplayable(now)).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태이면 노출 불가하다")
        void notDisplayableWhenDeleted() {
            // given
            var bannerGroup = BannerFixtures.deletedBannerGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThat(bannerGroup.isDisplayable(now)).isFalse();
        }

        @Test
        @DisplayName("노출 기간이 만료되면 노출 불가하다")
        void notDisplayableWhenPeriodExpired() {
            // given
            var bannerGroup = BannerFixtures.expiredBannerGroup();
            Instant now = CommonVoFixtures.now();

            // when & then
            assertThat(bannerGroup.isDisplayable(now)).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id()와 idValue()가 올바른 값을 반환한다")
        void returnsIdValues() {
            var bannerGroup = BannerFixtures.activeBannerGroup();

            assertThat(bannerGroup.id()).isNotNull();
            assertThat(bannerGroup.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("displayPeriod()가 올바른 값을 반환한다")
        void returnsDisplayPeriod() {
            var bannerGroup = BannerFixtures.activeBannerGroup();

            assertThat(bannerGroup.displayPeriod()).isNotNull();
            assertThat(bannerGroup.displayPeriod().startDate()).isNotNull();
            assertThat(bannerGroup.displayPeriod().endDate()).isNotNull();
        }

        @Test
        @DisplayName("deletionStatus()가 DeletionStatus를 반환한다")
        void returnsDeletionStatus() {
            var bannerGroup = BannerFixtures.activeBannerGroup();

            assertThat(bannerGroup.deletionStatus()).isNotNull();
            assertThat(bannerGroup.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("createdAt()과 updatedAt()이 올바른 값을 반환한다")
        void returnsTimeValues() {
            var bannerGroup = BannerFixtures.activeBannerGroup();

            assertThat(bannerGroup.createdAt()).isNotNull();
            assertThat(bannerGroup.updatedAt()).isNotNull();
        }
    }
}
