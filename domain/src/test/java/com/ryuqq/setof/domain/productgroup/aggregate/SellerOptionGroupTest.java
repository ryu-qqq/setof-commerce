package com.ryuqq.setof.domain.productgroup.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionGroupName;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerOptionGroup Entity 테스트")
class SellerOptionGroupTest {

    @Nested
    @DisplayName("forNew() - 신규 옵션 그룹 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 옵션 그룹을 생성한다")
        void createNewSellerOptionGroup() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            OptionGroupName groupName = ProductGroupFixtures.defaultOptionGroupName();
            SellerOptionValue value = ProductGroupFixtures.newSellerOptionValue();

            // when
            SellerOptionGroup group =
                    SellerOptionGroup.forNew(productGroupId, groupName, 1, List.of(value));

            // then
            assertThat(group).isNotNull();
            assertThat(group.id().isNew()).isTrue();
            assertThat(group.productGroupId()).isEqualTo(productGroupId);
            assertThat(group.optionGroupName()).isEqualTo(groupName);
            assertThat(group.sortOrder()).isEqualTo(1);
            assertThat(group.optionValueCount()).isEqualTo(1);
            assertThat(group.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성 시 활성 상태이다")
        void initialStateIsActive() {
            // when
            SellerOptionGroup group = ProductGroupFixtures.newSellerOptionGroup();

            // then
            assertThat(group.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성 시 옵션 값 목록이 복사된다")
        void optionValuesAreCopied() {
            // given
            SellerOptionValue value = ProductGroupFixtures.newSellerOptionValue();

            // when
            SellerOptionGroup group =
                    SellerOptionGroup.forNew(
                            ProductGroupId.of(1L),
                            ProductGroupFixtures.defaultOptionGroupName(),
                            1,
                            List.of(value));

            // then
            assertThat(group.optionValues()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태의 옵션 그룹을 복원한다")
        void reconstituteActiveSellerOptionGroup() {
            // when
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();

            // then
            assertThat(group.id()).isEqualTo(ProductGroupFixtures.defaultSellerOptionGroupId());
            assertThat(group.idValue())
                    .isEqualTo(ProductGroupFixtures.defaultSellerOptionGroupId().value());
            assertThat(group.isDeleted()).isFalse();
            assertThat(group.optionValueCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("삭제된 상태의 옵션 그룹을 복원한다")
        void reconstituteDeletedSellerOptionGroup() {
            // when
            SellerOptionGroup group = ProductGroupFixtures.deletedSellerOptionGroup();

            // then
            assertThat(group.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("빈 옵션 값을 가진 옵션 그룹을 복원한다")
        void reconstituteWithNoValues() {
            // when
            SellerOptionGroup group = ProductGroupFixtures.sellerOptionGroupWithNoValues();

            // then
            assertThat(group.optionValueCount()).isZero();
        }
    }

    @Nested
    @DisplayName("updateName() - 옵션 그룹명 수정")
    class UpdateNameTest {

        @Test
        @DisplayName("옵션 그룹명을 수정한다")
        void updateOptionGroupName() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            OptionGroupName newName = OptionGroupName.of("재질");

            // when
            group.updateName(newName);

            // then
            assertThat(group.optionGroupName()).isEqualTo(newName);
            assertThat(group.optionGroupNameValue()).isEqualTo("재질");
        }
    }

    @Nested
    @DisplayName("updateSortOrder() - 정렬 순서 변경")
    class UpdateSortOrderTest {

        @Test
        @DisplayName("정렬 순서를 변경한다")
        void updateSortOrder() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();

            // when
            group.updateSortOrder(3);

            // then
            assertThat(group.sortOrder()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("addOptionValue() - 옵션 값 추가")
    class AddOptionValueTest {

        @Test
        @DisplayName("옵션 값을 추가한다")
        void addOptionValue() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            int initialCount = group.optionValueCount();
            SellerOptionValue newValue =
                    SellerOptionValue.forNew(group.id(), OptionValueName.of("파랑"), 2);

            // when
            group.addOptionValue(newValue);

            // then
            assertThat(group.optionValueCount()).isEqualTo(initialCount + 1);
        }
    }

    @Nested
    @DisplayName("delete() - Soft Delete 처리")
    class DeleteTest {

        @Test
        @DisplayName("옵션 그룹과 하위 옵션 값을 모두 soft delete 처리한다")
        void deleteSellerOptionGroupAndValues() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            Instant now = CommonVoFixtures.now();

            // when
            group.delete(now);

            // then
            assertThat(group.isDeleted()).isTrue();
            group.optionValues().forEach(v -> assertThat(v.isDeleted()).isTrue());
        }
    }

    @Nested
    @DisplayName("optionValues() - 불변 리스트 반환")
    class OptionValuesTest {

        @Test
        @DisplayName("optionValues()는 불변 리스트를 반환한다")
        void returnsUnmodifiableOptionValues() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            SellerOptionValue extraValue = ProductGroupFixtures.newSellerOptionValue();

            // when & then
            assertThat(group.optionValues()).isNotNull();
            org.assertj.core.api.Assertions.assertThatThrownBy(
                            () -> group.optionValues().add(extraValue))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Accessor Methods 테스트")
    class AccessorMethodTest {

        @Test
        @DisplayName("id()는 SellerOptionGroupId를 반환한다")
        void returnsId() {
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            assertThat(group.id()).isNotNull();
            assertThat(group.id()).isInstanceOf(SellerOptionGroupId.class);
        }

        @Test
        @DisplayName("idValue()는 Long 값을 반환한다")
        void returnsIdValue() {
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            assertThat(group.idValue()).isEqualTo(10L);
        }

        @Test
        @DisplayName("productGroupId()는 ProductGroupId를 반환한다")
        void returnsProductGroupId() {
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            assertThat(group.productGroupId()).isNotNull();
            assertThat(group.productGroupIdValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("optionGroupName()은 OptionGroupName을 반환한다")
        void returnsOptionGroupName() {
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            assertThat(group.optionGroupName()).isNotNull();
            assertThat(group.optionGroupNameValue())
                    .isEqualTo(ProductGroupFixtures.DEFAULT_OPTION_GROUP_NAME);
        }

        @Test
        @DisplayName("deletionStatus()는 삭제 상태를 반환한다")
        void returnsDeletionStatus() {
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();
            assertThat(group.deletionStatus()).isNotNull();
            assertThat(group.deletionStatus().isActive()).isTrue();
        }
    }
}
