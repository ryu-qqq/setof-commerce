package com.ryuqq.setof.domain.productgroup.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupInvalidOptionStructureException 테스트")
class ProductGroupInvalidOptionStructureExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("NONE 타입에 그룹이 있는 경우 예외가 생성된다")
        void createForNoneTypeWithGroups() {
            // when
            ProductGroupInvalidOptionStructureException exception =
                    new ProductGroupInvalidOptionStructureException(OptionType.NONE, 0, 1);

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains("NONE");
            assertThat(exception.getMessage()).contains("0");
            assertThat(exception.getMessage()).contains("1");
        }

        @Test
        @DisplayName("SINGLE 타입에 그룹이 없는 경우 예외가 생성된다")
        void createForSingleTypeWithNoGroups() {
            // when
            ProductGroupInvalidOptionStructureException exception =
                    new ProductGroupInvalidOptionStructureException(OptionType.SINGLE, 1, 0);

            // then
            assertThat(exception.getMessage()).contains("SINGLE");
            assertThat(exception.getMessage()).contains("1");
            assertThat(exception.getMessage()).contains("0");
        }

        @Test
        @DisplayName("COMBINATION 타입에 그룹이 1개인 경우 예외가 생성된다")
        void createForCombinationTypeWithOneGroup() {
            // when
            ProductGroupInvalidOptionStructureException exception =
                    new ProductGroupInvalidOptionStructureException(OptionType.COMBINATION, 2, 1);

            // then
            assertThat(exception.getMessage()).contains("COMBINATION");
            assertThat(exception.getMessage()).contains("2");
            assertThat(exception.getMessage()).contains("1");
        }
    }

    @Nested
    @DisplayName("ErrorCode 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("에러 코드는 INVALID_OPTION_STRUCTURE이다")
        void hasCorrectErrorCode() {
            // given
            ProductGroupInvalidOptionStructureException exception =
                    new ProductGroupInvalidOptionStructureException(OptionType.NONE, 0, 1);

            // then
            assertThat(exception.code()).isEqualTo("PG-005");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductGroupException을 상속한다")
        void extendsProductGroupException() {
            // given
            ProductGroupInvalidOptionStructureException exception =
                    new ProductGroupInvalidOptionStructureException(OptionType.NONE, 0, 1);

            // then
            assertThat(exception).isInstanceOf(ProductGroupException.class);
        }
    }
}
