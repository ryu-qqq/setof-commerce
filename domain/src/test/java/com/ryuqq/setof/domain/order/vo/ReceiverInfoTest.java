package com.ryuqq.setof.domain.order.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ReceiverInfo Value Object 단위 테스트")
class ReceiverInfoTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("모든 필드로 수령인 정보를 생성한다")
        void createWithAllFields() {
            // when
            ReceiverInfo info =
                    ReceiverInfo.of(
                            "김수령",
                            "010-1234-5678",
                            "서울시 강남구 테헤란로 123",
                            "5층",
                            "06141",
                            "KR",
                            "문 앞에 놔주세요");

            // then
            assertThat(info.receiverName()).isEqualTo("김수령");
            assertThat(info.receiverPhone()).isEqualTo("010-1234-5678");
            assertThat(info.addressLine1()).isEqualTo("서울시 강남구 테헤란로 123");
            assertThat(info.addressLine2()).isEqualTo("5층");
            assertThat(info.zipCode()).isEqualTo("06141");
            assertThat(info.country()).isEqualTo("KR");
            assertThat(info.deliveryRequest()).isEqualTo("문 앞에 놔주세요");
        }

        @Test
        @DisplayName("선택 필드 없이 필수 필드만으로 생성할 수 있다")
        void createWithRequiredFieldsOnly() {
            // when
            ReceiverInfo info =
                    ReceiverInfo.of("김수령", null, "서울시 강남구 테헤란로 123", null, null, null, null);

            // then
            assertThat(info.receiverName()).isEqualTo("김수령");
            assertThat(info.receiverPhone()).isNull();
            assertThat(info.addressLine2()).isNull();
            assertThat(info.deliveryRequest()).isNull();
        }

        @Test
        @DisplayName("수령인명이 null이면 예외가 발생한다")
        void createWithNullReceiverName_ThrowsException() {
            assertThatThrownBy(
                            () ->
                                    ReceiverInfo.of(
                                            null,
                                            "010-1234-5678",
                                            "서울시 강남구 테헤란로 123",
                                            null,
                                            null,
                                            null,
                                            null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("수령인명은 필수입니다");
        }

        @Test
        @DisplayName("수령인명이 빈 문자열이면 예외가 발생한다")
        void createWithBlankReceiverName_ThrowsException() {
            assertThatThrownBy(
                            () ->
                                    ReceiverInfo.of(
                                            "  ",
                                            "010-1234-5678",
                                            "서울시 강남구 테헤란로 123",
                                            null,
                                            null,
                                            null,
                                            null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("수령인명은 필수입니다");
        }

        @Test
        @DisplayName("주소가 null이면 예외가 발생한다")
        void createWithNullAddressLine1_ThrowsException() {
            assertThatThrownBy(
                            () ->
                                    ReceiverInfo.of(
                                            "김수령", "010-1234-5678", null, null, null, null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주소는 필수입니다");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동일하다")
        void sameValuesAreEqual() {
            // given
            ReceiverInfo r1 =
                    ReceiverInfo.of("김수령", "010-1234-5678", "서울시 강남구", null, null, null, null);
            ReceiverInfo r2 =
                    ReceiverInfo.of("김수령", "010-1234-5678", "서울시 강남구", null, null, null, null);

            // then
            assertThat(r1).isEqualTo(r2);
            assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
        }
    }
}
