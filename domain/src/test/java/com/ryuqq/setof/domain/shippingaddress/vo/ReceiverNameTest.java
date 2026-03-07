package com.ryuqq.setof.domain.shippingaddress.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReceiverName Value Object 단위 테스트")
class ReceiverNameTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 이름으로 ReceiverName을 생성한다")
        void createWithValidValue() {
            // when
            ReceiverName receiverName = ReceiverName.of("홍길동");

            // then
            assertThat(receiverName.value()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("앞뒤 공백이 제거된다")
        void trimsLeadingAndTrailingWhitespace() {
            // when
            ReceiverName receiverName = ReceiverName.of("  홍길동  ");

            // then
            assertThat(receiverName.value()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("null을 전달하면 예외가 발생한다")
        void nullThrowsException() {
            assertThatThrownBy(() -> ReceiverName.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("수령인 이름은 필수입니다");
        }

        @Test
        @DisplayName("빈 문자열을 전달하면 예외가 발생한다")
        void blankThrowsException() {
            assertThatThrownBy(() -> ReceiverName.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("수령인 이름은 필수입니다");
        }

        @Test
        @DisplayName("50자를 초과하는 이름은 예외가 발생한다")
        void tooLongValueThrowsException() {
            // given
            String tooLong = "가".repeat(51);

            // when & then
            assertThatThrownBy(() -> ReceiverName.of(tooLong))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("50");
        }

        @Test
        @DisplayName("정확히 50자인 이름은 생성된다")
        void exactlyMaxLengthIsAllowed() {
            // given
            String exactly50 = "가".repeat(50);

            // when
            ReceiverName receiverName = ReceiverName.of(exactly50);

            // then
            assertThat(receiverName.value()).hasSize(50);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 ReceiverName은 동등하다")
        void sameValueAreEqual() {
            // given
            ReceiverName name1 = ReceiverName.of("홍길동");
            ReceiverName name2 = ReceiverName.of("홍길동");

            // then
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 ReceiverName은 동등하지 않다")
        void differentValueNotEquals() {
            // given
            ReceiverName name1 = ReceiverName.of("홍길동");
            ReceiverName name2 = ReceiverName.of("김철수");

            // then
            assertThat(name1).isNotEqualTo(name2);
        }
    }
}
