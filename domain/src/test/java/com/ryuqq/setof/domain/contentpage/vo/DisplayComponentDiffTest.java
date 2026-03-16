package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DisplayComponentDiff Value Object 단위 테스트")
class DisplayComponentDiffTest {

    @Nested
    @DisplayName("empty() - 빈 Diff 반환")
    class EmptyTest {

        @Test
        @DisplayName("empty()는 added/removed/retained 모두 빈 리스트를 반환한다")
        void emptyReturnsAllEmptyLists() {
            // when
            DisplayComponentDiff diff = DisplayComponentDiff.empty();

            // then
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).isEmpty();
        }

        @Test
        @DisplayName("empty()의 occurredAt은 Instant.EPOCH이다")
        void emptyOccurredAtIsEpoch() {
            // when
            DisplayComponentDiff diff = DisplayComponentDiff.empty();

            // then
            assertThat(diff.occurredAt()).isEqualTo(Instant.EPOCH);
        }

        @Test
        @DisplayName("empty()는 hasNoChanges()가 true이다")
        void emptyHasNoChanges() {
            // when
            DisplayComponentDiff diff = DisplayComponentDiff.empty();

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }
    }

    @Nested
    @DisplayName("compute() - 기존/신규 컴포넌트 비교")
    class ComputeTest {

        @Test
        @DisplayName("incoming에 idValue가 null인 컴포넌트는 added에 포함된다")
        void incomingWithNullIdIsAdded() {
            // given
            List<DisplayComponent> existing = List.of();
            DisplayComponent newComponent =
                    DisplayComponent.forNew(
                            1L,
                            "신규 컴포넌트",
                            1,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            List<DisplayComponent> incoming = List.of(newComponent);
            Instant now = CommonVoFixtures.now();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.retained()).isEmpty();
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.added().get(0).idValue()).isNull();
        }

        @Test
        @DisplayName("incoming에 기존 ID와 일치하는 컴포넌트는 retained에 포함된다")
        void incomingMatchingExistingIdIsRetained() {
            // given
            DisplayComponent existingComponent = ContentPageFixtures.textComponent(10L);
            List<DisplayComponent> existing = List.of(existingComponent);
            DisplayComponent retainedComponent = ContentPageFixtures.textComponent(10L);
            List<DisplayComponent> incoming = List.of(retainedComponent);
            Instant now = CommonVoFixtures.now();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);

            // then
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
        }

        @Test
        @DisplayName("existing에만 있고 incoming에 없는 컴포넌트는 removed에 포함되고 remove()가 호출된다")
        void existingNotInIncomingIsRemovedAndSoftDeleted() {
            // given
            DisplayComponent existingComponent = ContentPageFixtures.textComponent(20L);
            List<DisplayComponent> existing = List.of(existingComponent);
            List<DisplayComponent> incoming = List.of();
            Instant now = CommonVoFixtures.now();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);

            // then
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.added()).isEmpty();
            assertThat(diff.retained()).isEmpty();
            assertThat(diff.removed().get(0).isDeleted()).isTrue();
            assertThat(diff.removed().get(0).isActive()).isFalse();
        }

        @Test
        @DisplayName("빈 리스트끼리 compute하면 empty()와 동일하게 모두 빈 리스트이다")
        void computeWithBothEmptyReturnsAllEmptyLists() {
            // given
            List<DisplayComponent> existing = List.of();
            List<DisplayComponent> incoming = List.of();
            Instant now = CommonVoFixtures.now();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);

            // then
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).isEmpty();
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("기존이 비어있고 incoming이 있으면 전부 added에 포함된다")
        void existingEmptyAndAllIncomingIsAdded() {
            // given
            DisplayComponent newComp1 =
                    DisplayComponent.forNew(
                            1L,
                            "신규 컴포넌트 1",
                            1,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            DisplayComponent newComp2 =
                    DisplayComponent.forNew(
                            1L,
                            "신규 컴포넌트 2",
                            2,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            List<DisplayComponent> existing = List.of();
            List<DisplayComponent> incoming = List.of(newComp1, newComp2);
            Instant now = CommonVoFixtures.now();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);

            // then
            assertThat(diff.added()).hasSize(2);
            assertThat(diff.retained()).isEmpty();
            assertThat(diff.removed()).isEmpty();
        }

        @Test
        @DisplayName("기존이 있고 incoming이 비어있으면 전부 removed에 포함되고 모두 soft delete 처리된다")
        void incomingEmptyAndAllExistingIsRemoved() {
            // given
            DisplayComponent comp1 = ContentPageFixtures.textComponent(30L);
            DisplayComponent comp2 = ContentPageFixtures.productComponent(31L, OrderType.NONE, 10);
            List<DisplayComponent> existing = List.of(comp1, comp2);
            List<DisplayComponent> incoming = List.of();
            Instant now = CommonVoFixtures.now();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);

            // then
            assertThat(diff.removed()).hasSize(2);
            assertThat(diff.added()).isEmpty();
            assertThat(diff.retained()).isEmpty();
            diff.removed().forEach(c -> assertThat(c.isDeleted()).isTrue());
            diff.removed().forEach(c -> assertThat(c.isActive()).isFalse());
        }

        @Test
        @DisplayName("compute 결과의 occurredAt은 전달된 now와 일치한다")
        void computeOccurredAtMatchesProvidedNow() {
            // given
            Instant now = CommonVoFixtures.now();
            List<DisplayComponent> existing = List.of();
            List<DisplayComponent> incoming = List.of();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);

            // then
            assertThat(diff.occurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("added/retained/removed가 혼합된 경우 각각 올바르게 분류된다")
        void mixedIncomingIsClassifiedCorrectly() {
            // given
            DisplayComponent existingA = ContentPageFixtures.textComponent(100L);
            DisplayComponent existingB = ContentPageFixtures.textComponent(101L);
            List<DisplayComponent> existing = List.of(existingA, existingB);

            // retainedA: existingA ID 그대로 → retained
            DisplayComponent retainedA = ContentPageFixtures.textComponent(100L);
            // newComp: id null → added
            DisplayComponent newComp =
                    DisplayComponent.forNew(
                            1L,
                            "신규 컴포넌트",
                            3,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            // existingB는 incoming에 없으므로 → removed
            List<DisplayComponent> incoming = List.of(retainedA, newComp);
            Instant now = CommonVoFixtures.now();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);

            assertThat(diff.added().get(0).idValue()).isNull();
            assertThat(diff.retained().get(0).idValue()).isEqualTo(100L);
            assertThat(diff.removed().get(0).idValue()).isEqualTo(101L);
            assertThat(diff.removed().get(0).isDeleted()).isTrue();
        }

        @Test
        @DisplayName("compute 결과의 added/removed/retained 리스트는 불변이다")
        void computeResultListsAreImmutable() {
            // given
            DisplayComponent existing = ContentPageFixtures.textComponent(50L);
            DisplayComponent newComp =
                    DisplayComponent.forNew(
                            1L,
                            "신규",
                            1,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            List.of(existing), List.of(newComp), CommonVoFixtures.now());

            // when & then
            try {
                diff.added().add(newComp);
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }

            try {
                diff.removed().add(existing);
                throw new AssertionError("UnsupportedOperationException이 발생해야 합니다");
            } catch (UnsupportedOperationException e) {
                // 예상된 예외
            }
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 여부 확인")
    class HasNoChangesTest {

        @Test
        @DisplayName("added/removed/retained 모두 비어있으면 hasNoChanges()가 true이다")
        void hasNoChangesReturnsTrueWhenAllEmpty() {
            // given
            DisplayComponentDiff diff = DisplayComponentDiff.empty();

            // then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added가 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenAdded() {
            // given
            DisplayComponent newComp =
                    DisplayComponent.forNew(
                            1L,
                            "신규",
                            1,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            List.of(), List.of(newComp), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("removed가 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenRemoved() {
            // given
            DisplayComponent existingComp = ContentPageFixtures.textComponent(60L);
            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            List.of(existingComp), List.of(), CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("retained만 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenRetained() {
            // given
            DisplayComponent comp = ContentPageFixtures.textComponent(70L);
            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            List.of(comp),
                            List.of(ContentPageFixtures.textComponent(70L)),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("added/removed/retained 모두 있으면 hasNoChanges()가 false이다")
        void hasNoChangesReturnsFalseWhenAllPresent() {
            // given
            DisplayComponent existingA = ContentPageFixtures.textComponent(80L);
            DisplayComponent existingB = ContentPageFixtures.textComponent(81L);
            DisplayComponent retainedA = ContentPageFixtures.textComponent(80L);
            DisplayComponent newComp =
                    DisplayComponent.forNew(
                            1L,
                            "신규",
                            3,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            List.of(existingA, existingB),
                            List.of(retainedA, newComp),
                            CommonVoFixtures.now());

            // then
            assertThat(diff.hasNoChanges()).isFalse();
        }
    }

    @Nested
    @DisplayName("allDirtyComponents() - dirty 컴포넌트 합산 반환")
    class AllDirtyComponentsTest {

        @Test
        @DisplayName("retained + removed 합산 목록을 반환한다")
        void returnsRetainedPlusRemoved() {
            // given
            DisplayComponent existingA = ContentPageFixtures.textComponent(90L);
            DisplayComponent existingB = ContentPageFixtures.textComponent(91L);
            DisplayComponent retainedA = ContentPageFixtures.textComponent(90L);
            List<DisplayComponent> existing = List.of(existingA, existingB);
            List<DisplayComponent> incoming = List.of(retainedA);
            Instant now = CommonVoFixtures.now();

            // when
            DisplayComponentDiff diff = DisplayComponentDiff.compute(existing, incoming, now);
            List<DisplayComponent> dirty = diff.allDirtyComponents();

            // then
            assertThat(dirty).hasSize(2);
            assertThat(dirty).containsAll(diff.retained());
            assertThat(dirty).containsAll(diff.removed());
        }

        @Test
        @DisplayName("retained과 removed 모두 비어있으면 빈 목록을 반환한다")
        void returnsEmptyListWhenBothEmpty() {
            // given
            DisplayComponent newComp =
                    DisplayComponent.forNew(
                            1L,
                            "신규",
                            1,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            List.of(), List.of(newComp), CommonVoFixtures.now());

            // when
            List<DisplayComponent> dirty = diff.allDirtyComponents();

            // then
            assertThat(dirty).isEmpty();
        }

        @Test
        @DisplayName("allDirtyComponents()는 added 컴포넌트를 포함하지 않는다")
        void doesNotIncludeAddedComponents() {
            // given
            DisplayComponent existing = ContentPageFixtures.textComponent(92L);
            DisplayComponent retained = ContentPageFixtures.textComponent(92L);
            DisplayComponent newComp =
                    DisplayComponent.forNew(
                            1L,
                            "신규",
                            2,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            CommonVoFixtures.now());
            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            List.of(existing), List.of(retained, newComp), CommonVoFixtures.now());

            // when
            List<DisplayComponent> dirty = diff.allDirtyComponents();

            // then
            assertThat(dirty).hasSize(1);
            assertThat(diff.added()).hasSize(1);
            assertThat(dirty).doesNotContainAnyElementsOf(diff.added());
        }

        @Test
        @DisplayName("allDirtyComponents()는 retained이 먼저, removed가 나중에 오는 순서로 반환한다")
        void returnsRetainedBeforeRemoved() {
            // given
            DisplayComponent existingA = ContentPageFixtures.textComponent(93L);
            DisplayComponent existingB = ContentPageFixtures.textComponent(94L);
            DisplayComponent retainedA = ContentPageFixtures.textComponent(93L);
            DisplayComponentDiff diff =
                    DisplayComponentDiff.compute(
                            List.of(existingA, existingB),
                            List.of(retainedA),
                            CommonVoFixtures.now());

            // when
            List<DisplayComponent> dirty = diff.allDirtyComponents();

            // then
            assertThat(dirty).hasSize(2);
            // retained이 앞에, removed가 뒤에
            assertThat(dirty.get(0).idValue()).isEqualTo(93L);
            assertThat(dirty.get(1).idValue()).isEqualTo(94L);
            assertThat(dirty.get(1).isDeleted()).isTrue();
        }
    }
}
