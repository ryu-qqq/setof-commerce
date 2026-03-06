package com.ryuqq.setof.application.selleroption.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.exception.OptionGroupEmptyValuesException;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupInvalidOptionStructureException;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroups;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerOptionGroupValidator 단위 테스트")
class SellerOptionGroupValidatorTest {

    private SellerOptionGroupValidator sut;

    @BeforeEach
    void setUp() {
        sut = new SellerOptionGroupValidator();
    }

    @Nested
    @DisplayName("validate() - 옵션 그룹 구조 검증")
    class ValidateTest {

        @Test
        @DisplayName("SINGLE 타입에 1개 그룹이면 검증을 통과한다")
        void validate_SingleTypeWithOneGroup_NoException() {
            // given
            SellerOptionGroups optionGroups =
                    SellerOptionGroups.reconstitute(
                            ProductGroupFixtures.defaultSellerOptionGroups());

            // when & then
            assertThatCode(() -> sut.validate(optionGroups, OptionType.SINGLE))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("COMBINATION 타입에 2개 그룹이면 검증을 통과한다")
        void validate_CombinationTypeWithTwoGroups_NoException() {
            // given
            SellerOptionGroups optionGroups =
                    SellerOptionGroups.reconstitute(
                            ProductGroupFixtures.combinationSellerOptionGroups());

            // when & then
            assertThatCode(() -> sut.validate(optionGroups, OptionType.COMBINATION))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("NONE 타입에 0개 그룹이면 검증을 통과한다")
        void validate_NoneTypeWithZeroGroups_NoException() {
            // given
            SellerOptionGroups optionGroups = SellerOptionGroups.reconstitute(List.of());

            // when & then
            assertThatCode(() -> sut.validate(optionGroups, OptionType.NONE))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("SINGLE 타입에 그룹이 없으면 예외가 발생한다")
        void validate_SingleTypeWithNoGroups_ThrowsException() {
            // given
            SellerOptionGroups optionGroups = SellerOptionGroups.reconstitute(List.of());

            // when & then
            assertThatThrownBy(() -> sut.validate(optionGroups, OptionType.SINGLE))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("COMBINATION 타입에 그룹이 1개이면 예외가 발생한다")
        void validate_CombinationTypeWithOneGroup_ThrowsException() {
            // given
            SellerOptionGroups optionGroups =
                    SellerOptionGroups.reconstitute(
                            ProductGroupFixtures.defaultSellerOptionGroups());

            // when & then
            assertThatThrownBy(() -> sut.validate(optionGroups, OptionType.COMBINATION))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("NONE 타입에 그룹이 있으면 예외가 발생한다")
        void validate_NoneTypeWithGroups_ThrowsException() {
            // given
            SellerOptionGroups optionGroups =
                    SellerOptionGroups.reconstitute(
                            ProductGroupFixtures.defaultSellerOptionGroups());

            // when & then
            assertThatThrownBy(() -> sut.validate(optionGroups, OptionType.NONE))
                    .isInstanceOf(ProductGroupInvalidOptionStructureException.class);
        }

        @Test
        @DisplayName("옵션 그룹에 옵션 값이 없으면 예외가 발생한다")
        void validate_GroupWithNoValues_ThrowsException() {
            // given
            SellerOptionGroups optionGroups =
                    SellerOptionGroups.reconstitute(
                            List.of(ProductGroupFixtures.sellerOptionGroupWithNoValues()));

            // when & then
            assertThatThrownBy(() -> sut.validate(optionGroups, OptionType.SINGLE))
                    .isInstanceOf(OptionGroupEmptyValuesException.class);
        }
    }
}
