package com.ryuqq.setof.application.shippingaddress.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotOwnerException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetShippingAddressService")
@ExtendWith(MockitoExtension.class)
class GetShippingAddressServiceTest {

    @Mock private ShippingAddressReadManager shippingAddressReadManager;

    private ShippingAddressAssembler shippingAddressAssembler;
    private GetShippingAddressService getShippingAddressService;

    @BeforeEach
    void setUp() {
        shippingAddressAssembler = new ShippingAddressAssembler();
        getShippingAddressService =
                new GetShippingAddressService(shippingAddressReadManager, shippingAddressAssembler);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("배송지 단건 조회 성공")
        void shouldReturnShippingAddress() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);

            // When
            ShippingAddressResponse result =
                    getShippingAddressService.execute(memberId, shippingAddressId);

            // Then
            assertNotNull(result);
            assertEquals(shippingAddressId, result.id());
            verify(shippingAddressReadManager, times(1)).findById(shippingAddressId);
        }

        @Test
        @DisplayName("소유권 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenOwnershipValidationFails() {
            // Given
            UUID otherMemberId = UUID.randomUUID();
            Long shippingAddressId = 1L;
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);

            // When & Then
            assertThrows(
                    ShippingAddressNotOwnerException.class,
                    () -> getShippingAddressService.execute(otherMemberId, shippingAddressId));
        }

        @Test
        @DisplayName("응답에 올바른 배송지 정보 포함")
        void shouldContainCorrectShippingAddressInfo() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);

            // When
            ShippingAddressResponse result =
                    getShippingAddressService.execute(memberId, shippingAddressId);

            // Then
            assertEquals(shippingAddress.getIdValue(), result.id());
            assertEquals(shippingAddress.getAddressNameValue(), result.addressName());
            assertEquals(shippingAddress.getReceiverNameValue(), result.receiverName());
            assertEquals(shippingAddress.getReceiverPhoneValue(), result.receiverPhone());
            assertEquals(shippingAddress.isDefault(), result.isDefault());
        }
    }
}
