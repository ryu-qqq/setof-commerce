package com.ryuqq.setof.domain.shippingaddress.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DeliveryRequest Value Object 단위 테스트")
class DeliveryRequestTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("유효한 배송 요청사항으로 생성한다")
        void createWithValidValue() {
            // when
            DeliveryRequest request = DeliveryRequest.of("문 앞에 놓아주세요");

            // then
            assertThat(request.value()).isEqualTo("문 앞에 놓아주세요");
        }

        @Test
        @DisplayName("null로 생성하면 value가 null이다")
        void createWithNullValue() {
            // when
            DeliveryRequest request = DeliveryRequest.of(null);

            // then
            assertThat(request.value()).isNull();
        }

        @Test
        @DisplayName("빈 문자열로 생성하면 null로 정규화된다")
        void blankValueNormalizesToNull() {
            // when
            DeliveryRequest request = DeliveryRequest.of("   ");

            // then
            assertThat(request.value()).isNull();
        }

        @Test
        @DisplayName("앞뒤 공백이 제거된다")
        void trimsWhitespace() {
            // when
            DeliveryRequest request = DeliveryRequest.of("  경비실에 맡겨주세요  ");

            // then
            assertThat(request.value()).isEqualTo("경비실에 맡겨주세요");
        }

        @Test
        @DisplayName("500자를 초과하는 요청사항은 예외가 발생한다")
        void tooLongValueThrowsException() {
            // given
            String tooLong = "가".repeat(501);

            // when & then
            assertThatThrownBy(() -> DeliveryRequest.of(tooLong))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("500");
        }

        @Test
        @DisplayName("정확히 500자인 요청사항은 생성된다")
        void exactlyMaxLengthIsAllowed() {
            // given
            String exactly500 = "가".repeat(500);

            // when
            DeliveryRequest request = DeliveryRequest.of(exactly500);

            // then
            assertThat(request.value()).hasSize(500);
        }
    }

    @Nested
    @DisplayName("empty() - 빈 요청사항 생성")
    class EmptyTest {

        @Test
        @DisplayName("empty()는 value가 null인 DeliveryRequest를 반환한다")
        void emptyReturnsNullValue() {
            // when
            DeliveryRequest request = DeliveryRequest.empty();

            // then
            assertThat(request.value()).isNull();
        }

        @Test
        @DisplayName("null로 생성한 것과 empty()는 동등하다")
        void nullAndEmptyAreEqual() {
            // given
            DeliveryRequest fromNull = DeliveryRequest.of(null);
            DeliveryRequest empty = DeliveryRequest.empty();

            // then
            assertThat(fromNull).isEqualTo(empty);
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 DeliveryRequest는 동등하다")
        void sameValueAreEqual() {
            // given
            DeliveryRequest req1 = DeliveryRequest.of("문 앞에");
            DeliveryRequest req2 = DeliveryRequest.of("문 앞에");

            // then
            assertThat(req1).isEqualTo(req2);
            assertThat(req1.hashCode()).isEqualTo(req2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 DeliveryRequest는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            DeliveryRequest req1 = DeliveryRequest.of("문 앞에");
            DeliveryRequest req2 = DeliveryRequest.of("경비실에");

            // then
            assertThat(req1).isNotEqualTo(req2);
        }
    }
}
