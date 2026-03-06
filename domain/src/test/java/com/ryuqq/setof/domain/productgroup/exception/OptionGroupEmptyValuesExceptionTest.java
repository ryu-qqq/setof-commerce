package com.ryuqq.setof.domain.productgroup.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OptionGroupEmptyValuesException 테스트")
class OptionGroupEmptyValuesExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("옵션 그룹명으로 예외가 생성된다")
        void createWithOptionGroupName() {
            // when
            OptionGroupEmptyValuesException exception = new OptionGroupEmptyValuesException("색상");

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains("색상");
        }

        @Test
        @DisplayName("메시지에 옵션 그룹명이 포함된다")
        void messageContainsOptionGroupName() {
            // when
            OptionGroupEmptyValuesException exception = new OptionGroupEmptyValuesException("사이즈");

            // then
            assertThat(exception.getMessage()).contains("사이즈");
        }
    }

    @Nested
    @DisplayName("ErrorCode 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("에러 코드는 OPTION_GROUP_EMPTY_VALUES이다")
        void hasCorrectErrorCode() {
            // given
            OptionGroupEmptyValuesException exception = new OptionGroupEmptyValuesException("색상");

            // then
            assertThat(exception.code()).isEqualTo("PG-007");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }
    }

    @Nested
    @DisplayName("상속 관계 및 args 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductGroupException을 상속한다")
        void extendsProductGroupException() {
            // given
            OptionGroupEmptyValuesException exception = new OptionGroupEmptyValuesException("색상");

            // then
            assertThat(exception).isInstanceOf(ProductGroupException.class);
        }

        @Test
        @DisplayName("args에 optionGroupName을 포함한다")
        void argsContainOptionGroupName() {
            // given
            OptionGroupEmptyValuesException exception = new OptionGroupEmptyValuesException("색상");

            // then
            assertThat(exception.args()).containsKey("optionGroupName");
            assertThat(exception.args().get("optionGroupName")).isEqualTo("색상");
        }
    }
}
