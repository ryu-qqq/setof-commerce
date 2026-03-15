package com.ryuqq.setof.domain.discount.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.aggregate.DiscountTarget;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountTargetDiff Value Object 테스트")
class DiscountTargetDiffTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("added, removed, retained 목록으로 생성한다")
        void createWithAllLists() {
            // given
            DiscountTarget added = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTarget removed = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 200L);
            DiscountTarget retained = DiscountTarget.forNew(DiscountTargetType.CATEGORY, 300L);
            Instant now = CommonVoFixtures.now();

            // when
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(List.of(added), List.of(removed), List.of(retained), now);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.occurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("빈 목록으로 생성한다")
        void createWithEmptyLists() {
            // given
            Instant now = CommonVoFixtures.now();

            // when
            DiscountTargetDiff diff = DiscountTargetDiff.of(List.of(), List.of(), List.of(), now);

            // then
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).isEmpty();
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 확인")
    class HasNoChangesTest {

        @Test
        @DisplayName("added와 removed가 모두 비어있으면 true를 반환한다")
        void returnsTrueWhenNoAddedAndNoRemoved() {
            // given
            DiscountTarget retained = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(), List.of(), List.of(retained), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added가 있으면 false를 반환한다")
        void returnsFalseWhenHasAdded() {
            // given
            DiscountTarget added = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(added), List.of(), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("removed가 있으면 false를 반환한다")
        void returnsFalseWhenHasRemoved() {
            // given
            DiscountTarget removed = DiscountTarget.forNew(DiscountTargetType.BRAND, 200L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(), List.of(removed), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("added와 removed 모두 있으면 false를 반환한다")
        void returnsFalseWhenBothAddedAndRemoved() {
            // given
            DiscountTarget added = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTarget removed = DiscountTarget.forNew(DiscountTargetType.BRAND, 200L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(added), List.of(removed), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }
    }

    @Nested
    @DisplayName("allDirtyTargets() - UPDATE 대상 반환")
    class AllDirtyTargetsTest {

        @Test
        @DisplayName("retained + removed 를 합쳐서 반환한다")
        void returnsRetainedAndRemoved() {
            // given
            DiscountTarget added = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTarget removed = DiscountTarget.forNew(DiscountTargetType.BRAND, 200L);
            DiscountTarget retained = DiscountTarget.forNew(DiscountTargetType.CATEGORY, 300L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(added),
                            List.of(removed),
                            List.of(retained),
                            CommonVoFixtures.now());

            // when
            List<DiscountTarget> dirty = diff.allDirtyTargets();

            // then
            assertThat(dirty).hasSize(2);
            assertThat(dirty).containsExactly(retained, removed);
        }

        @Test
        @DisplayName("retained과 removed가 모두 비어있으면 빈 목록을 반환한다")
        void returnsEmptyWhenNoDirtyTargets() {
            // given
            DiscountTarget added = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(added), List.of(), List.of(), CommonVoFixtures.now());

            // when
            List<DiscountTarget> dirty = diff.allDirtyTargets();

            // then
            assertThat(dirty).isEmpty();
        }
    }

    @Nested
    @DisplayName("allChangedTargets() - 아웃박스 생성 대상 반환")
    class AllChangedTargetsTest {

        @Test
        @DisplayName("added + removed 를 합쳐서 반환한다")
        void returnsAddedAndRemoved() {
            // given
            DiscountTarget added = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTarget removed = DiscountTarget.forNew(DiscountTargetType.BRAND, 200L);
            DiscountTarget retained = DiscountTarget.forNew(DiscountTargetType.CATEGORY, 300L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(added),
                            List.of(removed),
                            List.of(retained),
                            CommonVoFixtures.now());

            // when
            List<DiscountTarget> changed = diff.allChangedTargets();

            // then
            assertThat(changed).hasSize(2);
            assertThat(changed).containsExactly(added, removed);
        }

        @Test
        @DisplayName("added와 removed가 모두 비어있으면 빈 목록을 반환한다")
        void returnsEmptyWhenNoChangedTargets() {
            // given
            DiscountTarget retained = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(), List.of(), List.of(retained), CommonVoFixtures.now());

            // when
            List<DiscountTarget> changed = diff.allChangedTargets();

            // then
            assertThat(changed).isEmpty();
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("added 목록은 외부에서 수정 불가하다")
        void addedListIsImmutable() {
            // given
            DiscountTarget added = DiscountTarget.forNew(DiscountTargetType.PRODUCT, 100L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(added), List.of(), List.of(), CommonVoFixtures.now());

            // when & then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.added()).isUnmodifiable();
        }

        @Test
        @DisplayName("removed 목록은 외부에서 수정 불가하다")
        void removedListIsImmutable() {
            // given
            DiscountTarget removed = DiscountTarget.forNew(DiscountTargetType.BRAND, 200L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(), List.of(removed), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.removed()).isUnmodifiable();
        }

        @Test
        @DisplayName("retained 목록은 외부에서 수정 불가하다")
        void retainedListIsImmutable() {
            // given
            DiscountTarget retained = DiscountTarget.forNew(DiscountTargetType.CATEGORY, 300L);
            DiscountTargetDiff diff =
                    DiscountTargetDiff.of(
                            List.of(), List.of(), List.of(retained), CommonVoFixtures.now());

            // then
            assertThat(diff.retained()).isUnmodifiable();
        }
    }

    @Nested
    @DisplayName("replaceTargets() 시나리오 테스트")
    class ReplaceTargetsScenarioTest {

        @Test
        @DisplayName("기존 대상에서 일부 교체 시 diff가 정확하게 계산된다")
        void replaceTargetsCalculatesDiffCorrectly() {
            // given - 기존 활성 타겟: PRODUCT:100, PRODUCT:200
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();
            policy.addTarget(DiscountTargetType.PRODUCT, 100L, now);
            policy.addTarget(DiscountTargetType.PRODUCT, 200L, now);

            // when - PRODUCT:200, PRODUCT:300으로 교체 (100은 제거, 300은 추가, 200은 유지)
            DiscountTargetDiff diff =
                    policy.replaceTargets(DiscountTargetType.PRODUCT, List.of(200L, 300L), now);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.added().get(0).targetId()).isEqualTo(300L);

            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.removed().get(0).targetId()).isEqualTo(100L);

            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.retained().get(0).targetId()).isEqualTo(200L);

            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("동일한 목록으로 교체 시 변경이 없다")
        void replaceWithSameTargetsHasNoChanges() {
            // given
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();
            policy.addTarget(DiscountTargetType.PRODUCT, 100L, now);
            policy.addTarget(DiscountTargetType.PRODUCT, 200L, now);

            // when
            DiscountTargetDiff diff =
                    policy.replaceTargets(DiscountTargetType.PRODUCT, List.of(100L, 200L), now);

            // then
            assertThat(diff.hasNoChanges()).isTrue();
            assertThat(diff.retained()).hasSize(2);
        }

        @Test
        @DisplayName("빈 목록으로 교체 시 기존 모두 제거된다")
        void replaceWithEmptyListRemovesAll() {
            // given
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy();
            Instant now = CommonVoFixtures.now();
            policy.addTarget(DiscountTargetType.PRODUCT, 100L, now);

            // when
            DiscountTargetDiff diff =
                    policy.replaceTargets(DiscountTargetType.PRODUCT, List.of(), now);

            // then
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.retained()).isEmpty();
            assertThat(diff.hasNoChanges()).isFalse();
        }
    }
}
