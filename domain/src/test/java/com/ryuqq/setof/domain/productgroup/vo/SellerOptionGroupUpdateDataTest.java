package com.ryuqq.setof.domain.productgroup.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerOptionGroupUpdateData Value Object 테스트")
class SellerOptionGroupUpdateDataTest {

    private SellerOptionGroupUpdateData.ValueEntry newValueEntry(String name) {
        return new SellerOptionGroupUpdateData.ValueEntry(null, name, 1);
    }

    private SellerOptionGroupUpdateData.ValueEntry existingValueEntry(Long id, String name) {
        return new SellerOptionGroupUpdateData.ValueEntry(id, name, 1);
    }

    private SellerOptionGroupUpdateData.GroupEntry newGroupEntry(String name) {
        return new SellerOptionGroupUpdateData.GroupEntry(
                null, name, 1, List.of(newValueEntry("값")));
    }

    private SellerOptionGroupUpdateData.GroupEntry existingGroupEntry(Long id, String name) {
        return new SellerOptionGroupUpdateData.GroupEntry(id, name, 1, List.of(newValueEntry("값")));
    }

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("신규 그룹 엔트리로 생성한다 (sellerOptionGroupId가 null인 경우)")
        void createWithNewGroupEntry() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            SellerOptionGroupUpdateData.GroupEntry entry = newGroupEntry("색상");
            Instant updatedAt = CommonVoFixtures.now();

            // when
            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(productGroupId, List.of(entry), updatedAt);

            // then
            assertThat(updateData.productGroupId()).isEqualTo(productGroupId);
            assertThat(updateData.groupEntries()).hasSize(1);
            assertThat(updateData.groupEntries().get(0).sellerOptionGroupId()).isNull();
            assertThat(updateData.groupEntries().get(0).optionGroupName()).isEqualTo("색상");
            assertThat(updateData.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("기존 그룹 엔트리로 생성한다 (sellerOptionGroupId가 non-null인 경우)")
        void createWithExistingGroupEntry() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            SellerOptionGroupUpdateData.GroupEntry entry = existingGroupEntry(10L, "색상");
            Instant updatedAt = CommonVoFixtures.now();

            // when
            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(productGroupId, List.of(entry), updatedAt);

            // then
            assertThat(updateData.groupEntries().get(0).sellerOptionGroupId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("복수의 그룹 엔트리로 생성한다")
        void createWithMultipleGroupEntries() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<SellerOptionGroupUpdateData.GroupEntry> entries =
                    List.of(existingGroupEntry(10L, "색상"), existingGroupEntry(11L, "사이즈"));
            Instant updatedAt = CommonVoFixtures.now();

            // when
            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(productGroupId, entries, updatedAt);

            // then
            assertThat(updateData.groupEntries()).hasSize(2);
        }

        @Test
        @DisplayName("그룹 엔트리 목록은 불변 복사본이다")
        void groupEntriesAreCopied() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            SellerOptionGroupUpdateData.GroupEntry entry = newGroupEntry("색상");
            Instant updatedAt = CommonVoFixtures.now();

            // when
            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(productGroupId, List.of(entry), updatedAt);

            // then
            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> updateData.groupEntries().add(newGroupEntry("사이즈")))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("GroupEntry 테스트")
    class GroupEntryTest {

        @Test
        @DisplayName("신규 그룹 엔트리는 sellerOptionGroupId가 null이다")
        void newGroupEntryHasNullId() {
            SellerOptionGroupUpdateData.GroupEntry entry = newGroupEntry("색상");
            assertThat(entry.sellerOptionGroupId()).isNull();
        }

        @Test
        @DisplayName("기존 그룹 엔트리는 sellerOptionGroupId가 non-null이다")
        void existingGroupEntryHasNonNullId() {
            SellerOptionGroupUpdateData.GroupEntry entry = existingGroupEntry(10L, "색상");
            assertThat(entry.sellerOptionGroupId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("GroupEntry의 values 목록은 불변이다")
        void valuesAreUnmodifiable() {
            SellerOptionGroupUpdateData.GroupEntry entry = newGroupEntry("색상");
            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> entry.values().add(newValueEntry("추가")))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("GroupEntry 필드를 정상적으로 반환한다")
        void returnsGroupEntryFields() {
            // given
            List<SellerOptionGroupUpdateData.ValueEntry> values =
                    List.of(newValueEntry("검정"), newValueEntry("흰색"));
            SellerOptionGroupUpdateData.GroupEntry entry =
                    new SellerOptionGroupUpdateData.GroupEntry(10L, "색상", 2, values);

            // then
            assertThat(entry.sellerOptionGroupId()).isEqualTo(10L);
            assertThat(entry.optionGroupName()).isEqualTo("색상");
            assertThat(entry.sortOrder()).isEqualTo(2);
            assertThat(entry.values()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("ValueEntry 테스트")
    class ValueEntryTest {

        @Test
        @DisplayName("신규 값 엔트리는 sellerOptionValueId가 null이다")
        void newValueEntryHasNullId() {
            SellerOptionGroupUpdateData.ValueEntry entry = newValueEntry("검정");
            assertThat(entry.sellerOptionValueId()).isNull();
        }

        @Test
        @DisplayName("기존 값 엔트리는 sellerOptionValueId가 non-null이다")
        void existingValueEntryHasNonNullId() {
            SellerOptionGroupUpdateData.ValueEntry entry = existingValueEntry(100L, "검정");
            assertThat(entry.sellerOptionValueId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("ValueEntry 필드를 정상적으로 반환한다")
        void returnsValueEntryFields() {
            // given
            SellerOptionGroupUpdateData.ValueEntry entry =
                    new SellerOptionGroupUpdateData.ValueEntry(100L, "검정", 3);

            // then
            assertThat(entry.sellerOptionValueId()).isEqualTo(100L);
            assertThat(entry.optionValueName()).isEqualTo("검정");
            assertThat(entry.sortOrder()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Accessor Methods 테스트")
    class AccessorMethodTest {

        @Test
        @DisplayName("productGroupId()는 ProductGroupId를 반환한다")
        void returnsProductGroupId() {
            ProductGroupId productGroupId = ProductGroupId.of(99L);
            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(
                            productGroupId, List.of(), CommonVoFixtures.now());
            assertThat(updateData.productGroupId()).isEqualTo(productGroupId);
        }

        @Test
        @DisplayName("updatedAt()은 수정 시각을 반환한다")
        void returnsUpdatedAt() {
            Instant now = CommonVoFixtures.now();
            SellerOptionGroupUpdateData updateData =
                    SellerOptionGroupUpdateData.of(ProductGroupId.of(1L), List.of(), now);
            assertThat(updateData.updatedAt()).isEqualTo(now);
        }
    }
}
