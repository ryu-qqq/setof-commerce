package com.ryuqq.setof.domain.productgroup.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.exception.OptionGroupDuplicateNameException;
import com.ryuqq.setof.domain.productgroup.exception.OptionGroupEmptyValuesException;
import com.ryuqq.setof.domain.productgroup.exception.OptionValueDuplicateNameException;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupInvalidOptionStructureException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerOptionGroups Value Object 테스트")
class SellerOptionGroupsTest {

    @Nested
    @DisplayName("of() - 신규 생성 또는 수정 시 사용")
    class OfTest {

        @Test
        @DisplayName("빈 목록으로 생성한다")
        void createWithEmptyList() {
            // when
            SellerOptionGroups groups = SellerOptionGroups.of(List.of());

            // then
            assertThat(groups.groups()).isEmpty();
        }

        @Test
        @DisplayName("단일 그룹으로 생성한다")
        void createWithSingleGroup() {
            // given
            SellerOptionGroup group = ProductGroupFixtures.activeSellerOptionGroup();

            // when
            SellerOptionGroups groups = SellerOptionGroups.of(List.of(group));

            // then
            assertThat(groups.groups()).hasSize(1);
        }

        @Test
        @DisplayName("groups() 리스트는 불변이다")
        void groupsListIsUnmodifiable() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when & then
            assertThatThrownBy(
                            () -> groups.groups().add(ProductGroupFixtures.newSellerOptionGroup()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원 시 사용")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 검증 없이 복원한다")
        void reconstituteWithoutValidation() {
            // given
            List<SellerOptionGroup> groupList =
                    ProductGroupFixtures.combinationSellerOptionGroups();

            // when
            SellerOptionGroups groups = SellerOptionGroups.reconstitute(groupList);

            // then
            assertThat(groups.groups()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("validateStructure() - 옵션 구조 검증")
    class ValidateStructureTest {

        @Test
        @DisplayName("NONE 타입 + 빈 그룹 목록은 유효하다")
        void noneTypeWithEmptyGroupsIsValid() {
            // given
            SellerOptionGroups groups = SellerOptionGroups.of(List.of());

            // when & then
            org.assertj.core.api.Assertions.assertThatCode(
                            () -> groups.validateStructure(OptionType.NONE))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("NONE 타입에 그룹이 있으면 예외가 발생한다")
        void noneTypeWithGroupsThrowsException() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when & then
            assertThatThrownBy(() -> groups.validateStructure(OptionType.NONE))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("SINGLE 타입 + 1개 그룹은 유효하다")
        void singleTypeWithOneGroupIsValid() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when & then
            org.assertj.core.api.Assertions.assertThatCode(
                            () -> groups.validateStructure(OptionType.SINGLE))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("SINGLE 타입에 그룹이 없으면 예외가 발생한다")
        void singleTypeWithNoGroupThrowsException() {
            // given
            SellerOptionGroups groups = SellerOptionGroups.of(List.of());

            // when & then
            assertThatThrownBy(() -> groups.validateStructure(OptionType.SINGLE))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("SINGLE 타입에 그룹이 2개이면 예외가 발생한다")
        void singleTypeWithTwoGroupsThrowsException() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(ProductGroupFixtures.combinationSellerOptionGroups());

            // when & then
            assertThatThrownBy(() -> groups.validateStructure(OptionType.SINGLE))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("COMBINATION 타입 + 2개 그룹은 유효하다")
        void combinationTypeWithTwoGroupsIsValid() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(ProductGroupFixtures.combinationSellerOptionGroups());

            // when & then
            org.assertj.core.api.Assertions.assertThatCode(
                            () -> groups.validateStructure(OptionType.COMBINATION))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("COMBINATION 타입에 그룹이 1개이면 예외가 발생한다")
        void combinationTypeWithOneGroupThrowsException() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when & then
            assertThatThrownBy(() -> groups.validateStructure(OptionType.COMBINATION))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("그룹명이 중복되면 예외가 발생한다")
        void duplicateGroupNameThrowsException() {
            // given: 동일한 그룹명으로 두 그룹 생성
            SellerOptionGroup group1 = ProductGroupFixtures.activeSellerOptionGroup(10L, "색상");
            SellerOptionGroup group2 = ProductGroupFixtures.activeSellerOptionGroup(11L, "색상");
            SellerOptionGroups groups = SellerOptionGroups.of(List.of(group1, group2));

            // when & then
            assertThatThrownBy(() -> groups.validateStructure(OptionType.COMBINATION))
                    .isInstanceOf(OptionGroupDuplicateNameException.class);
        }

        @Test
        @DisplayName("옵션 그룹에 값이 없으면 예외가 발생한다")
        void emptyGroupValuesThrowsException() {
            // given
            SellerOptionGroup emptyGroup = ProductGroupFixtures.sellerOptionGroupWithNoValues();
            SellerOptionGroups groups = SellerOptionGroups.of(List.of(emptyGroup));

            // when & then
            assertThatThrownBy(() -> groups.validateStructure(OptionType.SINGLE))
                    .isInstanceOf(OptionGroupEmptyValuesException.class);
        }

        @Test
        @DisplayName("같은 그룹 내 옵션 값 이름이 중복되면 예외가 발생한다")
        void duplicateValueNamesInSameGroupThrowsException() {
            // given: 같은 이름의 옵션 값 두 개를 가진 그룹
            SellerOptionValue value1 = ProductGroupFixtures.activeSellerOptionValue(100L, "검정");
            SellerOptionValue value2 = ProductGroupFixtures.activeSellerOptionValue(101L, "검정");
            SellerOptionGroup group =
                    SellerOptionGroup.reconstitute(
                            ProductGroupFixtures.defaultSellerOptionGroupId(),
                            ProductGroupId.of(1L),
                            ProductGroupFixtures.defaultOptionGroupName(),
                            1,
                            List.of(value1, value2),
                            com.ryuqq.setof.domain.common.vo.DeletionStatus.active());
            SellerOptionGroups groups = SellerOptionGroups.of(List.of(group));

            // when & then
            assertThatThrownBy(() -> groups.validateStructure(OptionType.SINGLE))
                    .isInstanceOf(OptionValueDuplicateNameException.class);
        }
    }

    @Nested
    @DisplayName("totalOptionValueCount() - 전체 옵션 값 수")
    class TotalOptionValueCountTest {

        @Test
        @DisplayName("그룹이 없으면 0을 반환한다")
        void returnsZeroForEmptyGroups() {
            // given
            SellerOptionGroups groups = SellerOptionGroups.of(List.of());

            // when & then
            assertThat(groups.totalOptionValueCount()).isZero();
        }

        @Test
        @DisplayName("단일 그룹의 옵션 값 수를 반환한다")
        void returnsTotalCountForSingleGroup() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when & then
            assertThat(groups.totalOptionValueCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("복수 그룹의 옵션 값 수 합산을 반환한다")
        void returnsTotalCountForMultipleGroups() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(ProductGroupFixtures.combinationSellerOptionGroups());

            // when & then
            assertThat(groups.totalOptionValueCount()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("identityDiff() - Identity Diff 생성")
    class IdentityDiffTest {

        @Test
        @DisplayName("변경 없는 diff를 생성한다")
        void createsIdentityDiff() {
            // given
            SellerOptionGroups groups =
                    SellerOptionGroups.of(List.of(ProductGroupFixtures.activeSellerOptionGroup()));

            // when
            SellerOptionGroupDiff diff = groups.identityDiff(CommonVoFixtures.now());

            // then
            assertThat(diff.addedGroups()).isEmpty();
            assertThat(diff.removedGroups()).isEmpty();
            assertThat(diff.retainedGroups()).hasSize(1);
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("빈 그룹 목록에 대한 identity diff는 모두 비어있다")
        void identityDiffForEmptyGroupsIsEmpty() {
            // given
            SellerOptionGroups groups = SellerOptionGroups.of(List.of());

            // when
            SellerOptionGroupDiff diff = groups.identityDiff(CommonVoFixtures.now());

            // then
            assertThat(diff.addedGroups()).isEmpty();
            assertThat(diff.removedGroups()).isEmpty();
            assertThat(diff.retainedGroups()).isEmpty();
            assertThat(diff.hasNoChanges()).isTrue();
        }
    }
}
