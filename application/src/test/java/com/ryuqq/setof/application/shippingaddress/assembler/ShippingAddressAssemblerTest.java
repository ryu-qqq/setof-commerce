package com.ryuqq.setof.application.shippingaddress.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ShippingAddressAssembler")
class ShippingAddressAssemblerTest {

    private ShippingAddressAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new ShippingAddressAssembler();
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("ShippingAddress 도메인을 ShippingAddressResponse로 변환 성공")
        void shouldConvertShippingAddressToResponse() {
            // Given
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(1L);

            // When
            ShippingAddressResponse result = assembler.toResponse(shippingAddress);

            // Then
            assertNotNull(result);
            assertEquals(shippingAddress.getIdValue(), result.id());
            assertEquals(shippingAddress.getAddressNameValue(), result.addressName());
            assertEquals(shippingAddress.getReceiverNameValue(), result.receiverName());
            assertEquals(shippingAddress.getReceiverPhoneValue(), result.receiverPhone());
            assertEquals(shippingAddress.getRoadAddressValue(), result.roadAddress());
            assertEquals(shippingAddress.getJibunAddressValue(), result.jibunAddress());
            assertEquals(shippingAddress.getDetailAddressValue(), result.detailAddress());
            assertEquals(shippingAddress.getZipCodeValue(), result.zipCode());
            assertEquals(shippingAddress.getDeliveryRequestValue(), result.deliveryRequest());
            assertEquals(shippingAddress.isDefault(), result.isDefault());
        }

        @Test
        @DisplayName("기본 배송지가 아닌 경우 isDefault가 false")
        void shouldReturnIsDefaultFalseWhenNotDefault() {
            // Given
            ShippingAddress shippingAddress = ShippingAddressFixture.createNonDefault(2L);

            // When
            ShippingAddressResponse result = assembler.toResponse(shippingAddress);

            // Then
            assertNotNull(result);
            assertEquals(false, result.isDefault());
        }

        @Test
        @DisplayName("배송 요청사항이 null인 경우 null 반환")
        void shouldReturnNullDeliveryRequestWhenNotSet() {
            // Given
            ShippingAddress shippingAddress = ShippingAddressFixture.createNonDefault(2L);

            // When
            ShippingAddressResponse result = assembler.toResponse(shippingAddress);

            // Then
            assertNull(result.deliveryRequest());
        }
    }

    @Nested
    @DisplayName("toResponses")
    class ToResponsesTest {

        @Test
        @DisplayName("ShippingAddress 목록을 ShippingAddressResponse 목록으로 변환 성공")
        void shouldConvertListToResponseList() {
            // Given
            List<ShippingAddress> shippingAddresses = ShippingAddressFixture.createList();

            // When
            List<ShippingAddressResponse> results = assembler.toResponses(shippingAddresses);

            // Then
            assertNotNull(results);
            assertEquals(shippingAddresses.size(), results.size());

            for (int i = 0; i < shippingAddresses.size(); i++) {
                assertEquals(shippingAddresses.get(i).getIdValue(), results.get(i).id());
            }
        }

        @Test
        @DisplayName("빈 목록 변환 시 빈 목록 반환")
        void shouldReturnEmptyListWhenInputIsEmpty() {
            // Given
            List<ShippingAddress> emptyList = List.of();

            // When
            List<ShippingAddressResponse> results = assembler.toResponses(emptyList);

            // Then
            assertNotNull(results);
            assertTrue(results.isEmpty());
        }
    }
}
