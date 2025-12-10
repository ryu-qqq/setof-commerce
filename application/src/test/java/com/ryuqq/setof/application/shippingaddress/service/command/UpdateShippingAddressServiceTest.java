package com.ryuqq.setof.application.shippingaddress.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.factory.command.ShippingAddressCommandFactory;
import com.ryuqq.setof.application.shippingaddress.manager.command.ShippingAddressPersistenceManager;
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

@DisplayName("UpdateShippingAddressService")
@ExtendWith(MockitoExtension.class)
class UpdateShippingAddressServiceTest {

    @Mock private ShippingAddressReadManager shippingAddressReadManager;
    @Mock private ShippingAddressPersistenceManager shippingAddressPersistenceManager;
    @Mock private ShippingAddressCommandFactory shippingAddressCommandFactory;

    private ShippingAddressAssembler shippingAddressAssembler;
    private UpdateShippingAddressService updateShippingAddressService;

    @BeforeEach
    void setUp() {
        shippingAddressAssembler = new ShippingAddressAssembler();
        updateShippingAddressService =
                new UpdateShippingAddressService(
                        shippingAddressReadManager,
                        shippingAddressPersistenceManager,
                        shippingAddressCommandFactory,
                        shippingAddressAssembler);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("배송지 수정 성공")
        void shouldUpdateShippingAddress() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            UpdateShippingAddressCommand command = createCommand(memberId, shippingAddressId);
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);

            // When
            ShippingAddressResponse result = updateShippingAddressService.execute(command);

            // Then
            assertNotNull(result);
            assertEquals(shippingAddressId, result.id());
            verify(shippingAddressCommandFactory, times(1)).applyUpdate(eq(shippingAddress), eq(command));
            verify(shippingAddressPersistenceManager, times(1)).persist(shippingAddress);
        }

        @Test
        @DisplayName("소유권 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenOwnershipValidationFails() {
            // Given
            UUID otherMemberId = UUID.randomUUID();
            Long shippingAddressId = 1L;
            UpdateShippingAddressCommand command = createCommand(otherMemberId, shippingAddressId);
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);

            // When & Then
            assertThrows(
                    ShippingAddressNotOwnerException.class,
                    () -> updateShippingAddressService.execute(command));
        }

        @Test
        @DisplayName("수정 후 응답에 올바른 정보 포함")
        void shouldContainCorrectInfoInResponse() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            UpdateShippingAddressCommand command = createCommand(memberId, shippingAddressId);
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);

            // When
            ShippingAddressResponse result = updateShippingAddressService.execute(command);

            // Then
            assertEquals(shippingAddress.getIdValue(), result.id());
            assertEquals(shippingAddress.getAddressNameValue(), result.addressName());
            assertEquals(shippingAddress.getReceiverNameValue(), result.receiverName());
        }

        private UpdateShippingAddressCommand createCommand(UUID memberId, Long shippingAddressId) {
            return UpdateShippingAddressCommand.of(
                    memberId,
                    shippingAddressId,
                    "회사",
                    "김철수",
                    "01098765432",
                    "서울시 서초구 서초대로 456",
                    null,
                    "A동 501호",
                    "06789",
                    "경비실에 맡겨주세요");
        }
    }
}
