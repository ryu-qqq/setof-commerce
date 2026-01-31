package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
@DisplayName("Address Value Object 테스트")
class AddressTest {

    @Nested
    @DisplayName("of() - 주소 생성")
    class CreationTest {

        @Test
        @DisplayName("전체 주소 정보로 주소를 생성한다")
        void createWithFullAddress() {
            // when
            var address = Address.of("06141", "서울시 강남구 테헤란로 123", "테스트빌딩 5층");

            // then
            assertThat(address.zipcode()).isEqualTo("06141");
            assertThat(address.line1()).isEqualTo("서울시 강남구 테헤란로 123");
            assertThat(address.line2()).isEqualTo("테스트빌딩 5층");
        }

        @Test
        @DisplayName("상세 주소 없이 주소를 생성한다")
        void createWithoutLine2() {
            // when
            var address = Address.of("06141", "서울시 강남구 테헤란로 123");

            // then
            assertThat(address.zipcode()).isEqualTo("06141");
            assertThat(address.line1()).isEqualTo("서울시 강남구 테헤란로 123");
            assertThat(address.line2()).isNull();
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            // when
            var address = Address.of("  06141  ", "  서울시 강남구  ", "  5층  ");

            // then
            assertThat(address.zipcode()).isEqualTo("06141");
            assertThat(address.line1()).isEqualTo("서울시 강남구");
            assertThat(address.line2()).isEqualTo("5층");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("우편번호가 null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmptyZipcode(String value) {
            assertThatThrownBy(() -> Address.of(value, "서울시 강남구", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("우편번호");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t"})
        @DisplayName("기본 주소가 null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmptyLine1(String value) {
            assertThatThrownBy(() -> Address.of("06141", value, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("기본 주소");
        }
    }

    @Nested
    @DisplayName("fullAddress() - 전체 주소 문자열")
    class FullAddressTest {

        @Test
        @DisplayName("상세 주소가 있으면 전체 주소를 반환한다")
        void returnsFullAddressWithLine2() {
            // given
            var address = Address.of("06141", "서울시 강남구 테헤란로 123", "테스트빌딩 5층");

            // when
            String fullAddress = address.fullAddress();

            // then
            assertThat(fullAddress).isEqualTo("서울시 강남구 테헤란로 123 테스트빌딩 5층");
        }

        @Test
        @DisplayName("상세 주소가 없으면 기본 주소만 반환한다")
        void returnsLine1OnlyWhenNoLine2() {
            // given
            var address = Address.of("06141", "서울시 강남구 테헤란로 123");

            // when
            String fullAddress = address.fullAddress();

            // then
            assertThat(fullAddress).isEqualTo("서울시 강남구 테헤란로 123");
        }

        @Test
        @DisplayName("상세 주소가 빈 문자열이면 기본 주소만 반환한다")
        void returnsLine1OnlyWhenEmptyLine2() {
            // given
            var address = Address.of("06141", "서울시 강남구 테헤란로 123", "  ");

            // when
            String fullAddress = address.fullAddress();

            // then
            assertThat(fullAddress).isEqualTo("서울시 강남구 테헤란로 123");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 주소는 동등하다")
        void equalAddressesAreEqual() {
            var address1 = Address.of("06141", "서울시 강남구", "5층");
            var address2 = Address.of("06141", "서울시 강남구", "5층");

            assertThat(address1).isEqualTo(address2);
            assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
        }

        @Test
        @DisplayName("우편번호가 다르면 동등하지 않다")
        void differentZipcodeNotEqual() {
            var address1 = Address.of("06141", "서울시 강남구", "5층");
            var address2 = Address.of("06142", "서울시 강남구", "5층");

            assertThat(address1).isNotEqualTo(address2);
        }
    }
}
