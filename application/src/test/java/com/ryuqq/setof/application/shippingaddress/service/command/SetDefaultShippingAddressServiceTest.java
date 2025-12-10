package com.ryuqq.setof.application.shippingaddress.service.command;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.command.SetDefaultShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.manager.command.ShippingAddressPersistenceManager;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotOwnerException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("SetDefaultShippingAddressService")
@ExtendWith(MockitoExtension.class)
class SetDefaultShippingAddressServiceTest {

    @Mock private ShippingAddressReadManager shippingAddressReadManager;
    @Mock private ShippingAddressPersistenceManager shippingAddressPersistenceManager;
    @Mock private ClockHolder clockHolder;

    private ShippingAddressAssembler shippingAddressAssembler;
    private SetDefaultShippingAddressService setDefaultShippingAddressService;

    @BeforeEach
    void setUp() {
        shippingAddressAssembler = new ShippingAddressAssembler();
        setDefaultShippingAddressService =
                new SetDefaultShippingAddressService(
                        shippingAddressReadManager,
                        shippingAddressPersistenceManager,
                        shippingAddressAssembler,
                        clockHolder);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("기본 배송지 설정 성공")
        void shouldSetDefaultShippingAddress() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            SetDefaultShippingAddressCommand command =
                    SetDefaultShippingAddressCommand.of(memberId, shippingAddressId);
            ShippingAddress shippingAddress = ShippingAddressFixture.createNonDefault(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);
            when(shippingAddressReadManager.findDefaultByMemberId(memberId))
                    .thenReturn(Optional.empty());
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);

            // When
            ShippingAddressResponse result = setDefaultShippingAddressService.execute(command);

            // Then
            assertNotNull(result);
            verify(shippingAddressPersistenceManager, times(1)).persist(any());
        }

        @Test
        @DisplayName("소유권 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenOwnershipValidationFails() {
            // Given
            UUID otherMemberId = UUID.randomUUID();
            Long shippingAddressId = 1L;
            SetDefaultShippingAddressCommand command =
                    SetDefaultShippingAddressCommand.of(otherMemberId, shippingAddressId);
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);

            // When & Then
            assertThrows(
                    ShippingAddressNotOwnerException.class,
                    () -> setDefaultShippingAddressService.execute(command));
            verify(shippingAddressPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("기존 기본 배송지가 있으면 해제 후 새로 설정")
        void shouldUnsetPreviousDefaultWhenExists() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 2L;
            SetDefaultShippingAddressCommand command =
                    SetDefaultShippingAddressCommand.of(memberId, shippingAddressId);
            ShippingAddress existingDefault = ShippingAddressFixture.createWithId(1L);
            ShippingAddress newAddress = ShippingAddressFixture.createNonDefault(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(newAddress);
            when(shippingAddressReadManager.findDefaultByMemberId(memberId))
                    .thenReturn(Optional.of(existingDefault));
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);

            // When
            ShippingAddressResponse result = setDefaultShippingAddressService.execute(command);

            // Then
            assertNotNull(result);
            verify(shippingAddressPersistenceManager, times(2)).persist(any());
        }

        @Test
        @DisplayName("이미 기본 배송지인 경우 기존 해제 없이 설정")
        void shouldNotUnsetWhenAlreadyDefault() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            SetDefaultShippingAddressCommand command =
                    SetDefaultShippingAddressCommand.of(memberId, shippingAddressId);
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);
            when(shippingAddressReadManager.findDefaultByMemberId(memberId))
                    .thenReturn(Optional.of(shippingAddress));
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);

            // When
            ShippingAddressResponse result = setDefaultShippingAddressService.execute(command);

            // Then
            assertNotNull(result);
            verify(shippingAddressPersistenceManager, times(1)).persist(any());
        }

        @Test
        @DisplayName("응답에 올바른 배송지 정보 포함")
        void shouldContainCorrectInfoInResponse() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            SetDefaultShippingAddressCommand command =
                    SetDefaultShippingAddressCommand.of(memberId, shippingAddressId);
            ShippingAddress shippingAddress = ShippingAddressFixture.createNonDefault(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(shippingAddress);
            when(shippingAddressReadManager.findDefaultByMemberId(memberId))
                    .thenReturn(Optional.empty());
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);

            // When
            ShippingAddressResponse result = setDefaultShippingAddressService.execute(command);

            // Then
            assertNotNull(result);
            verify(shippingAddressReadManager, times(1)).findById(shippingAddressId);
            verify(shippingAddressReadManager, times(1)).findDefaultByMemberId(memberId);
        }
    }
}
