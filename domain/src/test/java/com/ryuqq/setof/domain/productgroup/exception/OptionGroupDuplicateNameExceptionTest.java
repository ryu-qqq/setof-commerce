package com.ryuqq.setof.domain.productgroup.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OptionGroupDuplicateNameException 테스트")
class OptionGroupDuplicateNameExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("중복된 그룹명으로 예외가 생성된다")
        void createWithDuplicateName() {
            // when
            OptionGroupDuplicateNameException exception =
                    new OptionGroupDuplicateNameException("색상");

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains("색상");
        }

        @Test
        @DisplayName("메시지에 중복 그룹명이 포함된다")
        void messageContainsDuplicateName() {
            // when
            OptionGroupDuplicateNameException exception =
                    new OptionGroupDuplicateNameException("사이즈");

            // then
            assertThat(exception.getMessage()).contains("사이즈");
        }
    }

    @Nested
    @DisplayName("ErrorCode 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("에러 코드는 OPTION_GROUP_DUPLICATE_NAME이다")
        void hasCorrectErrorCode() {
            // given
            OptionGroupDuplicateNameException exception =
                    new OptionGroupDuplicateNameException("색상");

            // then
            assertThat(exception.code()).isEqualTo("PG-008");
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
            OptionGroupDuplicateNameException exception =
                    new OptionGroupDuplicateNameException("색상");

            // then
            assertThat(exception).isInstanceOf(ProductGroupException.class);
        }

        @Test
        @DisplayName("args에 duplicateName을 포함한다")
        void argsContainDuplicateName() {
            // given
            OptionGroupDuplicateNameException exception =
                    new OptionGroupDuplicateNameException("색상");

            // then
            assertThat(exception.args()).containsKey("duplicateName");
            assertThat(exception.args().get("duplicateName")).isEqualTo("색상");
        }
    }
}
