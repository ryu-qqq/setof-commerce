package com.ryuqq.setof.domain.productgroup.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OptionValueName Value Object 테스트")
class OptionValueNameTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("유효한 이름으로 생성한다")
        void createWithValidName() {
            // when
            OptionValueName name = OptionValueName.of("검정");

            // then
            assertThat(name.value()).isEqualTo("검정");
        }

        @Test
        @DisplayName("앞뒤 공백이 트림된다")
        void trimWhitespace() {
            // when
            OptionValueName name = OptionValueName.of("  검정  ");

            // then
            assertThat(name.value()).isEqualTo("검정");
        }

        @Test
        @DisplayName("null이면 예외가 발생한다")
        void throwExceptionForNull() {
            assertThatThrownBy(() -> OptionValueName.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("옵션 값 이름은 필수입니다");
        }

        @Test
        @DisplayName("빈 문자열이면 예외가 발생한다")
        void throwExceptionForBlank() {
            assertThatThrownBy(() -> OptionValueName.of(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("옵션 값 이름은 필수입니다");
        }

        @Test
        @DisplayName("공백만 있는 문자열이면 예외가 발생한다")
        void throwExceptionForWhitespaceOnly() {
            assertThatThrownBy(() -> OptionValueName.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("옵션 값 이름은 필수입니다");
        }

        @Test
        @DisplayName("100자를 초과하면 예외가 발생한다")
        void throwExceptionForTooLongName() {
            String tooLong = "가".repeat(101);
            assertThatThrownBy(() -> OptionValueName.of(tooLong))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("100");
        }

        @Test
        @DisplayName("정확히 100자이면 생성한다")
        void createWithExactMaxLength() {
            String exactly100 = "가".repeat(100);
            OptionValueName name = OptionValueName.of(exactly100);
            assertThat(name.value()).hasSize(100);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 OptionValueName은 동등하다")
        void sameValueAreEqual() {
            OptionValueName name1 = OptionValueName.of("검정");
            OptionValueName name2 = OptionValueName.of("검정");
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 OptionValueName은 동등하지 않다")
        void differentValueAreNotEqual() {
            OptionValueName name1 = OptionValueName.of("검정");
            OptionValueName name2 = OptionValueName.of("흰색");
            assertThat(name1).isNotEqualTo(name2);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("OptionValueName은 record이므로 불변이다")
        void optionValueNameIsImmutable() {
            OptionValueName name = OptionValueName.of("검정");
            assertThat(name.value()).isEqualTo("검정");
        }
    }
}
