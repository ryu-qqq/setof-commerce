package com.ryuqq.setof.domain.shippingaddress.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.shippingaddress.exception.InvalidDeliveryAddressException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * DeliveryAddress Value Object 테스트
 *
 * <p>배송 주소 정보를 테스트합니다.
 */
@DisplayName("DeliveryAddress Value Object")
class DeliveryAddressTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("모든 주소 정보로 DeliveryAddress 생성")
        void shouldCreateDeliveryAddressWithAllValues() {
            // Given
            String roadAddress = "서울시 강남구 테헤란로 123";
            String jibunAddress = "역삼동 123-45";
            String detailAddress = "101동 1001호";
            String zipCode = "06234";

            // When
            DeliveryAddress deliveryAddress =
                    DeliveryAddress.of(roadAddress, jibunAddress, detailAddress, zipCode);

            // Then
            assertNotNull(deliveryAddress);
            assertEquals(roadAddress, deliveryAddress.roadAddress());
            assertEquals(jibunAddress, deliveryAddress.jibunAddress());
            assertEquals(detailAddress, deliveryAddress.detailAddress());
            assertEquals(zipCode, deliveryAddress.zipCode());
        }

        @Test
        @DisplayName("도로명 주소만으로 DeliveryAddress 생성")
        void shouldCreateDeliveryAddressWithRoadAddressOnly() {
            // Given
            String roadAddress = "서울시 강남구 테헤란로 123";
            String zipCode = "06234";

            // When
            DeliveryAddress deliveryAddress = DeliveryAddress.of(roadAddress, null, null, zipCode);

            // Then
            assertNotNull(deliveryAddress);
            assertEquals(roadAddress, deliveryAddress.roadAddress());
            assertNull(deliveryAddress.jibunAddress());
            assertNull(deliveryAddress.detailAddress());
        }

        @Test
        @DisplayName("지번 주소만으로 DeliveryAddress 생성")
        void shouldCreateDeliveryAddressWithJibunAddressOnly() {
            // Given
            String jibunAddress = "역삼동 123-45";
            String zipCode = "06234";

            // When
            DeliveryAddress deliveryAddress = DeliveryAddress.of(null, jibunAddress, null, zipCode);

            // Then
            assertNotNull(deliveryAddress);
            assertNull(deliveryAddress.roadAddress());
            assertEquals(jibunAddress, deliveryAddress.jibunAddress());
        }
    }

    @Nested
    @DisplayName("검증 실패 테스트")
    class ValidationFailureTest {

        @Test
        @DisplayName("우편번호가 null이면 예외 발생")
        void shouldThrowExceptionWhenZipCodeIsNull() {
            // When & Then
            assertThrows(
                    InvalidDeliveryAddressException.class,
                    () -> DeliveryAddress.of("서울시 강남구", null, null, null));
        }

        @Test
        @DisplayName("우편번호가 빈 문자열이면 예외 발생")
        void shouldThrowExceptionWhenZipCodeIsEmpty() {
            // When & Then
            assertThrows(
                    InvalidDeliveryAddressException.class,
                    () -> DeliveryAddress.of("서울시 강남구", null, null, ""));
        }

        @Test
        @DisplayName("도로명 주소와 지번 주소가 모두 없으면 예외 발생")
        void shouldThrowExceptionWhenBothAddressesAreNull() {
            // When & Then
            assertThrows(
                    InvalidDeliveryAddressException.class,
                    () -> DeliveryAddress.of(null, null, "상세주소", "06234"));
        }

        @Test
        @DisplayName("도로명 주소와 지번 주소가 모두 빈 문자열이면 예외 발생")
        void shouldThrowExceptionWhenBothAddressesAreEmpty() {
            // When & Then
            assertThrows(
                    InvalidDeliveryAddressException.class,
                    () -> DeliveryAddress.of("", "", "상세주소", "06234"));
        }
    }

    @Nested
    @DisplayName("유틸리티 메서드 테스트")
    class UtilityMethodTest {

        @Test
        @DisplayName("fullAddress()는 도로명 주소 우선으로 전체 주소를 반환한다")
        void shouldReturnFullAddressWithRoadAddress() {
            // Given
            DeliveryAddress deliveryAddress =
                    DeliveryAddress.of("서울시 강남구 테헤란로 123", "역삼동 123-45", "101동 1001호", "06234");

            // When
            String fullAddress = deliveryAddress.fullAddress();

            // Then
            assertTrue(fullAddress.contains("서울시 강남구 테헤란로 123"));
            assertTrue(fullAddress.contains("101동 1001호"));
        }

        @Test
        @DisplayName("fullAddress()는 도로명 주소가 없으면 지번 주소를 반환한다")
        void shouldReturnFullAddressWithJibunAddress() {
            // Given
            DeliveryAddress deliveryAddress =
                    DeliveryAddress.of(null, "역삼동 123-45", "101동 1001호", "06234");

            // When
            String fullAddress = deliveryAddress.fullAddress();

            // Then
            assertTrue(fullAddress.contains("역삼동 123-45"));
            assertTrue(fullAddress.contains("101동 1001호"));
        }

        @Test
        @DisplayName("fullAddress()는 상세 주소가 없어도 동작한다")
        void shouldReturnFullAddressWithoutDetailAddress() {
            // Given
            DeliveryAddress deliveryAddress =
                    DeliveryAddress.of("서울시 강남구 테헤란로 123", null, null, "06234");

            // When
            String fullAddress = deliveryAddress.fullAddress();

            // Then
            assertNotNull(fullAddress);
            assertTrue(fullAddress.contains("서울시 강남구 테헤란로 123"));
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 DeliveryAddress는 동등하다")
        void shouldBeEqualWhenSameValue() {
            // Given
            DeliveryAddress addr1 =
                    DeliveryAddress.of("서울시 강남구 테헤란로 123", "역삼동 123-45", "101동 1001호", "06234");
            DeliveryAddress addr2 =
                    DeliveryAddress.of("서울시 강남구 테헤란로 123", "역삼동 123-45", "101동 1001호", "06234");

            // When & Then
            assertEquals(addr1, addr2);
            assertEquals(addr1.hashCode(), addr2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 DeliveryAddress는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentValue() {
            // Given
            DeliveryAddress addr1 =
                    DeliveryAddress.of("서울시 강남구 테헤란로 123", null, "101동 1001호", "06234");
            DeliveryAddress addr2 =
                    DeliveryAddress.of("서울시 서초구 반포대로 456", null, "202동 2002호", "06500");

            // When & Then
            assertNotEquals(addr1, addr2);
        }
    }
}
