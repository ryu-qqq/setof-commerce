package com.ryuqq.setof.domain.productgroup.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OptionValueDuplicateNameException 테스트")
class OptionValueDuplicateNameExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("그룹명과 중복 값 이름으로 예외가 생성된다")
        void createWithGroupNameAndDuplicateValueName() {
            // when
            OptionValueDuplicateNameException exception =
                    new OptionValueDuplicateNameException("색상", "검정");

            // then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains("색상");
            assertThat(exception.getMessage()).contains("검정");
        }

        @Test
        @DisplayName("메시지에 그룹명과 값 이름이 모두 포함된다")
        void messageContainsBothNames() {
            // when
            OptionValueDuplicateNameException exception =
                    new OptionValueDuplicateNameException("사이즈", "XL");

            // then
            assertThat(exception.getMessage()).contains("사이즈");
            assertThat(exception.getMessage()).contains("XL");
        }
    }

    @Nested
    @DisplayName("ErrorCode 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("에러 코드는 OPTION_VALUE_DUPLICATE_NAME이다")
        void hasCorrectErrorCode() {
            // given
            OptionValueDuplicateNameException exception =
                    new OptionValueDuplicateNameException("색상", "검정");

            // then
            assertThat(exception.code()).isEqualTo("PG-009");
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
            OptionValueDuplicateNameException exception =
                    new OptionValueDuplicateNameException("색상", "검정");

            // then
            assertThat(exception).isInstanceOf(ProductGroupException.class);
        }

        @Test
        @DisplayName("args에 optionGroupName과 duplicateValueName을 포함한다")
        void argsContainBothNames() {
            // given
            OptionValueDuplicateNameException exception =
                    new OptionValueDuplicateNameException("색상", "검정");

            // then
            assertThat(exception.args()).containsKey("optionGroupName");
            assertThat(exception.args()).containsKey("duplicateValueName");
            assertThat(exception.args().get("optionGroupName")).isEqualTo("색상");
            assertThat(exception.args().get("duplicateValueName")).isEqualTo("검정");
        }
    }
}
