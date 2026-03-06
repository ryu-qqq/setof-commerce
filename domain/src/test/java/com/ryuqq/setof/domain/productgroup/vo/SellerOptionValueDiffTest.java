package com.ryuqq.setof.domain.productgroup.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerOptionValueDiff Value Object 테스트")
class SellerOptionValueDiffTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("추가/삭제/유지 목록으로 생성한다")
        void createWithAllLists() {
            // given
            SellerOptionValue added = ProductGroupFixtures.newSellerOptionValue();
            SellerOptionValue removed = ProductGroupFixtures.activeSellerOptionValue();
            SellerOptionValue retained = ProductGroupFixtures.activeSellerOptionValue(101L, "파랑");

            // when
            SellerOptionValueDiff diff =
                    SellerOptionValueDiff.of(List.of(added), List.of(removed), List.of(retained));

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.retained()).hasSize(1);
        }

        @Test
        @DisplayName("빈 목록으로 생성한다")
        void createWithEmptyLists() {
            // when
            SellerOptionValueDiff diff = SellerOptionValueDiff.of(List.of(), List.of(), List.of());

            // then
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).isEmpty();
        }

        @Test
        @DisplayName("추가만 있는 경우 생성한다")
        void createWithOnlyAdded() {
            // given
            SellerOptionValue added = ProductGroupFixtures.newSellerOptionValue();

            // when
            SellerOptionValueDiff diff =
                    SellerOptionValueDiff.of(List.of(added), List.of(), List.of());

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).isEmpty();
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 여부")
    class HasNoChangesTest {

        @Test
        @DisplayName("added와 removed가 모두 비어있으면 변경 없음이다")
        void noChangesWhenAddedAndRemovedAreEmpty() {
            // given
            SellerOptionValue retained = ProductGroupFixtures.activeSellerOptionValue();
            SellerOptionValueDiff diff =
                    SellerOptionValueDiff.of(List.of(), List.of(), List.of(retained));

            // when & then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added가 있으면 변경이 있다")
        void hasChangesWhenAddedIsNotEmpty() {
            // given
            SellerOptionValue added = ProductGroupFixtures.newSellerOptionValue();
            SellerOptionValueDiff diff =
                    SellerOptionValueDiff.of(List.of(added), List.of(), List.of());

            // when & then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("removed가 있으면 변경이 있다")
        void hasChangesWhenRemovedIsNotEmpty() {
            // given
            SellerOptionValue removed = ProductGroupFixtures.activeSellerOptionValue();
            SellerOptionValueDiff diff =
                    SellerOptionValueDiff.of(List.of(), List.of(removed), List.of());

            // when & then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("모든 목록이 비어있으면 변경 없음이다")
        void noChangesWhenAllEmpty() {
            // given
            SellerOptionValueDiff diff = SellerOptionValueDiff.of(List.of(), List.of(), List.of());

            // when & then
            assertThat(diff.hasNoChanges()).isTrue();
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("added() 리스트는 불변이다")
        void addedListIsUnmodifiable() {
            SellerOptionValue added = ProductGroupFixtures.newSellerOptionValue();
            SellerOptionValueDiff diff =
                    SellerOptionValueDiff.of(List.of(added), List.of(), List.of());

            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> diff.added().add(ProductGroupFixtures.newSellerOptionValue()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("removed() 리스트는 불변이다")
        void removedListIsUnmodifiable() {
            SellerOptionValue removed = ProductGroupFixtures.activeSellerOptionValue();
            SellerOptionValueDiff diff =
                    SellerOptionValueDiff.of(List.of(), List.of(removed), List.of());

            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> diff.removed().add(ProductGroupFixtures.newSellerOptionValue()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("retained() 리스트는 불변이다")
        void retainedListIsUnmodifiable() {
            SellerOptionValue retained = ProductGroupFixtures.activeSellerOptionValue();
            SellerOptionValueDiff diff =
                    SellerOptionValueDiff.of(List.of(), List.of(), List.of(retained));

            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> diff.retained().add(ProductGroupFixtures.newSellerOptionValue()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
