package com.ryuqq.setof.domain.productgroup.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("OptionGroupName Value Object 테스트")
class OptionGroupNameTest {

    @Nested
    @DisplayName("of() - 생성 테스트")
    class OfTest {

        @Test
        @DisplayName("유효한 이름으로 생성한다")
        void createWithValidName() {
            // when
            OptionGroupName name = OptionGroupName.of("색상");

            // then
            assertThat(name.value()).isEqualTo("색상");
        }

        @Test
        @DisplayName("앞뒤 공백이 트림된다")
        void trimWhitespace() {
            // when
            OptionGroupName name = OptionGroupName.of("  색상  ");

            // then
            assertThat(name.value()).isEqualTo("색상");
        }

        @Test
        @DisplayName("null이면 예외가 발생한다")
        void throwExceptionForNull() {
            assertThatThrownBy(() -> OptionGroupName.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("옵션 그룹명은 필수입니다");
        }

        @Test
        @DisplayName("빈 문자열이면 예외가 발생한다")
        void throwExceptionForBlank() {
            assertThatThrownBy(() -> OptionGroupName.of(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("옵션 그룹명은 필수입니다");
        }

        @Test
        @DisplayName("공백만 있는 문자열이면 예외가 발생한다")
        void throwExceptionForWhitespaceOnly() {
            assertThatThrownBy(() -> OptionGroupName.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("옵션 그룹명은 필수입니다");
        }

        @Test
        @DisplayName("100자를 초과하면 예외가 발생한다")
        void throwExceptionForTooLongName() {
            String tooLong = "가".repeat(101);
            assertThatThrownBy(() -> OptionGroupName.of(tooLong))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("100");
        }

        @Test
        @DisplayName("정확히 100자이면 생성한다")
        void createWithExactMaxLength() {
            String exactly100 = "가".repeat(100);
            OptionGroupName name = OptionGroupName.of(exactly100);
            assertThat(name.value()).hasSize(100);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값의 OptionGroupName은 동등하다")
        void sameValueAreEqual() {
            OptionGroupName name1 = OptionGroupName.of("색상");
            OptionGroupName name2 = OptionGroupName.of("색상");
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }

        @Test
        @DisplayName("다른 값의 OptionGroupName은 동등하지 않다")
        void differentValueAreNotEqual() {
            OptionGroupName name1 = OptionGroupName.of("색상");
            OptionGroupName name2 = OptionGroupName.of("사이즈");
            assertThat(name1).isNotEqualTo(name2);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("OptionGroupName은 record이므로 불변이다")
        void optionGroupNameIsImmutable() {
            OptionGroupName name = OptionGroupName.of("색상");
            assertThat(name.value()).isEqualTo("색상");
        }
    }
}
