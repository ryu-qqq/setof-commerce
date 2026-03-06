package com.ryuqq.setof.domain.productgroup.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerOptionGroupDiff Value Object 테스트")
class SellerOptionGroupDiffTest {

    private SellerOptionGroupDiff emptyDiff() {
        return SellerOptionGroupDiff.of(
                List.of(), List.of(), List.of(), List.of(), CommonVoFixtures.now());
    }

    private SellerOptionGroupDiff.RetainedGroupDiff defaultRetainedGroupDiff(
            boolean withValueChanges) {
        SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
        SellerOptionValueDiff valueDiff;
        if (withValueChanges) {
            valueDiff =
                    SellerOptionValueDiff.of(
                            List.of(ProductGroupFixtures.newSellerOptionValue()),
                            List.of(),
                            List.of(ProductGroupFixtures.activeSellerOptionValue()));
        } else {
            valueDiff =
                    SellerOptionValueDiff.of(
                            List.of(),
                            List.of(),
                            List.of(ProductGroupFixtures.activeSellerOptionValue()));
        }
        return new SellerOptionGroupDiff.RetainedGroupDiff(group, valueDiff);
    }

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("모든 필드로 diff를 생성한다")
        void createWithAllFields() {
            // given
            SellerOptionGroup added = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionGroup removed = ProductGroupFixtures.activeSellerOptionGroup();
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);
            SellerOptionValueId valueId = ProductGroupFixtures.defaultSellerOptionValueId();
            Instant now = CommonVoFixtures.now();

            // when
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(added),
                            List.of(removed),
                            List.of(retained),
                            List.of(valueId),
                            now);

            // then
            assertThat(diff.addedGroups()).hasSize(1);
            assertThat(diff.removedGroups()).hasSize(1);
            assertThat(diff.retainedGroups()).hasSize(1);
            assertThat(diff.orderedActiveValueIds()).hasSize(1);
            assertThat(diff.occurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("빈 diff를 생성한다")
        void createEmptyDiff() {
            // when
            SellerOptionGroupDiff diff = emptyDiff();

            // then
            assertThat(diff.addedGroups()).isEmpty();
            assertThat(diff.removedGroups()).isEmpty();
            assertThat(diff.retainedGroups()).isEmpty();
            assertThat(diff.orderedActiveValueIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 여부")
    class HasNoChangesTest {

        @Test
        @DisplayName("added, removed가 비어있고 retained의 값 변경도 없으면 변경 없음이다")
        void noChangesWhenAllEmpty() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(),
                            List.of(),
                            List.of(retained),
                            List.of(),
                            CommonVoFixtures.now());

            // when & then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added가 있으면 변경이 있다")
        void hasChangesWhenAddedIsNotEmpty() {
            // given
            SellerOptionGroup added = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(added),
                            List.of(),
                            List.of(),
                            List.of(),
                            CommonVoFixtures.now());

            // when & then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("removed가 있으면 변경이 있다")
        void hasChangesWhenRemovedIsNotEmpty() {
            // given
            SellerOptionGroup removed = ProductGroupFixtures.activeSellerOptionGroup();
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(),
                            List.of(removed),
                            List.of(),
                            List.of(),
                            CommonVoFixtures.now());

            // when & then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("retained 내에 값 변경이 있으면 변경이 있다")
        void hasChangesWhenRetainedHasValueChanges() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(true);
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(),
                            List.of(),
                            List.of(retained),
                            List.of(),
                            CommonVoFixtures.now());

            // when & then
            assertThat(diff.hasNoChanges()).isFalse();
        }
    }

    @Nested
    @DisplayName("allRetainedValues() - 유지된 옵션 값 반환")
    class AllRetainedValuesTest {

        @Test
        @DisplayName("retained 그룹 내 유지된 옵션 값을 반환한다")
        void returnsRetainedValues() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(),
                            List.of(),
                            List.of(retained),
                            List.of(),
                            CommonVoFixtures.now());

            // when
            List<SellerOptionValue> retainedValues = diff.allRetainedValues();

            // then
            assertThat(retainedValues).isNotEmpty();
        }

        @Test
        @DisplayName("retained 그룹이 없으면 빈 리스트를 반환한다")
        void returnsEmptyWhenNoRetained() {
            // when
            List<SellerOptionValue> retainedValues = emptyDiff().allRetainedValues();

            // then
            assertThat(retainedValues).isEmpty();
        }
    }

    @Nested
    @DisplayName("allGroups() - 전체 그룹 반환")
    class AllGroupsTest {

        @Test
        @DisplayName("removed, added, retained 그룹을 모두 합산한다")
        void returnsAllGroups() {
            // given
            SellerOptionGroup added = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionGroup removed = ProductGroupFixtures.activeSellerOptionGroup(11L, "사이즈");
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(added),
                            List.of(removed),
                            List.of(retained),
                            List.of(),
                            CommonVoFixtures.now());

            // when
            List<SellerOptionGroup> allGroups = diff.allGroups();

            // then
            assertThat(allGroups).hasSize(3);
        }
    }

    @Nested
    @DisplayName("allRemovedValues() - 삭제된 옵션 값 반환")
    class AllRemovedValuesTest {

        @Test
        @DisplayName("removed 그룹과 retained 내 removed 값을 반환한다")
        void returnsAllRemovedValues() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(),
                            List.of(),
                            List.of(retained),
                            List.of(),
                            CommonVoFixtures.now());

            // when
            List<SellerOptionValue> removedValues = diff.allRemovedValues();

            // then
            assertThat(removedValues).isEmpty();
        }
    }

    @Nested
    @DisplayName("allAddedValues() - 추가된 옵션 값 반환")
    class AllAddedValuesTest {

        @Test
        @DisplayName("added 그룹의 옵션 값을 반환한다")
        void returnsAddedValuesFromAddedGroups() {
            // given
            SellerOptionGroup added = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(added),
                            List.of(),
                            List.of(),
                            List.of(),
                            CommonVoFixtures.now());

            // when
            List<SellerOptionValue> addedValues = diff.allAddedValues();

            // then
            assertThat(addedValues).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("RetainedGroupDiff 테스트")
    class RetainedGroupDiffTest {

        @Test
        @DisplayName("hasNoValueChanges() - 값 변경이 없으면 true를 반환한다")
        void hasNoValueChangesReturnsTrue() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);

            // when & then
            assertThat(retained.hasNoValueChanges()).isTrue();
        }

        @Test
        @DisplayName("hasNoValueChanges() - 값 변경이 있으면 false를 반환한다")
        void hasNoValueChangesReturnsFalse() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(true);

            // when & then
            assertThat(retained.hasNoValueChanges()).isFalse();
        }

        @Test
        @DisplayName("retainedValues() - 유지된 값 목록을 반환한다")
        void returnsRetainedValues() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);

            // when & then
            assertThat(retained.retainedValues()).isNotEmpty();
        }

        @Test
        @DisplayName("addedValues() - 추가된 값 목록을 반환한다")
        void returnsAddedValues() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(true);

            // when & then
            assertThat(retained.addedValues()).isNotEmpty();
        }

        @Test
        @DisplayName("removedValues() - 삭제된 값 목록을 반환한다")
        void returnsRemovedValues() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);

            // when & then
            assertThat(retained.removedValues()).isEmpty();
        }

        @Test
        @DisplayName("group()은 유지된 SellerOptionGroup을 반환한다")
        void returnsGroup() {
            // given
            SellerOptionGroupDiff.RetainedGroupDiff retained = defaultRetainedGroupDiff(false);

            // when & then
            assertThat(retained.group()).isNotNull();
            assertThat(retained.group()).isInstanceOf(SellerOptionGroup.class);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("addedGroups() 리스트는 불변이다")
        void addedGroupsIsUnmodifiable() {
            SellerOptionGroup added = ProductGroupFixtures.newSellerOptionGroup();
            SellerOptionGroupDiff diff =
                    SellerOptionGroupDiff.of(
                            List.of(added),
                            List.of(),
                            List.of(),
                            List.of(),
                            CommonVoFixtures.now());

            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () ->
                                    diff.addedGroups()
                                            .add(ProductGroupFixtures.newSellerOptionGroup()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
