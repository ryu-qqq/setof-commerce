package com.ryuqq.setof.domain.banner.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BannerSlideDiff Value Object 테스트")
class BannerSlideDiffTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 BannerSlideDiff를 생성한다")
        void createWithOf() {
            // given
            var added = List.of(BannerFixtures.newBannerSlide());
            var removed = List.of(BannerFixtures.activeBannerSlide());
            var retained = List.of(BannerFixtures.activeBannerSlide(2L));
            Instant now = CommonVoFixtures.now();

            // when
            BannerSlideDiff diff = BannerSlideDiff.of(added, removed, retained, now);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.occurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("생성된 리스트는 불변이다")
        void listsAreImmutable() {
            // given
            var added = List.of(BannerFixtures.newBannerSlide());
            BannerSlideDiff diff =
                    BannerSlideDiff.of(added, List.of(), List.of(), CommonVoFixtures.now());

            // when & then
            assertThat(diff.added()).isUnmodifiable();
            assertThat(diff.removed()).isUnmodifiable();
            assertThat(diff.retained()).isUnmodifiable();
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 판단")
    class HasNoChangesTest {

        @Test
        @DisplayName("added와 removed가 모두 비어있으면 변경 없음이다")
        void noChangesWhenAddedAndRemovedAreEmpty() {
            // given
            var retained = List.of(BannerFixtures.activeBannerSlide());
            BannerSlideDiff diff =
                    BannerSlideDiff.of(List.of(), List.of(), retained, CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added가 있으면 변경이 있다")
        void hasChangesWhenAddedIsNotEmpty() {
            // given
            var added = List.of(BannerFixtures.newBannerSlide());
            BannerSlideDiff diff =
                    BannerSlideDiff.of(added, List.of(), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("removed가 있으면 변경이 있다")
        void hasChangesWhenRemovedIsNotEmpty() {
            // given
            var removed = List.of(BannerFixtures.activeBannerSlide());
            BannerSlideDiff diff =
                    BannerSlideDiff.of(List.of(), removed, List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("added와 removed가 모두 있으면 변경이 있다")
        void hasChangesWhenBothAddedAndRemovedExist() {
            // given
            var added = List.of(BannerFixtures.newBannerSlide());
            var removed = List.of(BannerFixtures.activeBannerSlide());
            BannerSlideDiff diff =
                    BannerSlideDiff.of(added, removed, List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("retained만 있으면 변경 없음이다 (수정은 변경으로 간주하지 않음)")
        void noChangesWhenOnlyRetained() {
            // given
            var retained =
                    List.of(
                            BannerFixtures.activeBannerSlide(1L),
                            BannerFixtures.activeBannerSlide(2L));
            BannerSlideDiff diff =
                    BannerSlideDiff.of(List.of(), List.of(), retained, CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }
    }

    @Nested
    @DisplayName("allDirtySlides() - dirty check 대상 슬라이드 반환")
    class AllDirtySlidesTest {

        @Test
        @DisplayName("retained + removed를 합쳐서 반환한다")
        void returnsRetainedPlusRemoved() {
            // given
            var retained = List.of(BannerFixtures.activeBannerSlide(1L));
            var removed =
                    List.of(
                            BannerFixtures.activeBannerSlide(2L),
                            BannerFixtures.activeBannerSlide(3L));
            var added = List.of(BannerFixtures.newBannerSlide());
            BannerSlideDiff diff =
                    BannerSlideDiff.of(added, removed, retained, CommonVoFixtures.now());

            // when
            var dirtySlides = diff.allDirtySlides();

            // then
            assertThat(dirtySlides).hasSize(3);
        }

        @Test
        @DisplayName("added는 allDirtySlides에 포함되지 않는다")
        void addedNotIncludedInDirtySlides() {
            // given
            var added = List.of(BannerFixtures.newBannerSlide(), BannerFixtures.newBannerSlide());
            BannerSlideDiff diff =
                    BannerSlideDiff.of(added, List.of(), List.of(), CommonVoFixtures.now());

            // when
            var dirtySlides = diff.allDirtySlides();

            // then
            assertThat(dirtySlides).isEmpty();
        }

        @Test
        @DisplayName("모든 목록이 비어있으면 빈 리스트를 반환한다")
        void returnsEmptyListWhenAllEmpty() {
            // given
            BannerSlideDiff diff =
                    BannerSlideDiff.of(List.of(), List.of(), List.of(), CommonVoFixtures.now());

            // when
            var dirtySlides = diff.allDirtySlides();

            // then
            assertThat(dirtySlides).isEmpty();
        }

        @Test
        @DisplayName("allDirtySlides()는 retained 순서가 먼저다")
        void retainedComesBeforeRemovedInDirtySlides() {
            // given
            var retained = List.of(BannerFixtures.activeBannerSlide(10L));
            var removed = List.of(BannerFixtures.activeBannerSlide(20L));
            BannerSlideDiff diff =
                    BannerSlideDiff.of(List.of(), removed, retained, CommonVoFixtures.now());

            // when
            var dirtySlides = diff.allDirtySlides();

            // then
            assertThat(dirtySlides.get(0).idValue()).isEqualTo(10L);
            assertThat(dirtySlides.get(1).idValue()).isEqualTo(20L);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 구성의 BannerSlideDiff는 동등하다")
        void sameContentAreEqual() {
            // given
            Instant now = CommonVoFixtures.now();
            BannerSlideDiff diff1 = BannerSlideDiff.of(List.of(), List.of(), List.of(), now);
            BannerSlideDiff diff2 = BannerSlideDiff.of(List.of(), List.of(), List.of(), now);

            // then
            assertThat(diff1).isEqualTo(diff2);
            assertThat(diff1.hashCode()).isEqualTo(diff2.hashCode());
        }
    }
}
