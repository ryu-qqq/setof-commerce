package com.ryuqq.setof.application.shippingaddress.service.command;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
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

@DisplayName("DeleteShippingAddressService")
@ExtendWith(MockitoExtension.class)
class DeleteShippingAddressServiceTest {

    @Mock private ShippingAddressReadManager shippingAddressReadManager;
    @Mock private ShippingAddressPersistenceManager shippingAddressPersistenceManager;
    @Mock private ClockHolder clockHolder;

    private DeleteShippingAddressService deleteShippingAddressService;

    @BeforeEach
    void setUp() {
        deleteShippingAddressService =
                new DeleteShippingAddressService(
                        shippingAddressReadManager, shippingAddressPersistenceManager, clockHolder);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("배송지 삭제 성공")
        void shouldDeleteShippingAddress() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            DeleteShippingAddressCommand command =
                    DeleteShippingAddressCommand.of(memberId, shippingAddressId);
            ShippingAddress shippingAddress =
                    ShippingAddressFixture.createNonDefault(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId))
                    .thenReturn(shippingAddress);
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);

            // When
            deleteShippingAddressService.execute(command);

            // Then
            verify(shippingAddressPersistenceManager, times(1)).persist(shippingAddress);
        }

        @Test
        @DisplayName("소유권 검증 실패 시 예외 발생")
        void shouldThrowExceptionWhenOwnershipValidationFails() {
            // Given
            UUID otherMemberId = UUID.randomUUID();
            Long shippingAddressId = 1L;
            DeleteShippingAddressCommand command =
                    DeleteShippingAddressCommand.of(otherMemberId, shippingAddressId);
            ShippingAddress shippingAddress =
                    ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId))
                    .thenReturn(shippingAddress);

            // When & Then
            assertThrows(
                    ShippingAddressNotOwnerException.class,
                    () -> deleteShippingAddressService.execute(command));
            verify(shippingAddressPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("기본 배송지 삭제 시 다음 배송지로 자동 승격")
        void shouldPromoteNextDefaultWhenDeletingDefault() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            DeleteShippingAddressCommand command =
                    DeleteShippingAddressCommand.of(memberId, shippingAddressId);
            ShippingAddress defaultAddress = ShippingAddressFixture.createWithId(shippingAddressId);
            ShippingAddress nextAddress = ShippingAddressFixture.createNonDefault(2L);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(defaultAddress);
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);
            when(shippingAddressReadManager.findLatestExcluding(memberId, shippingAddressId))
                    .thenReturn(Optional.of(nextAddress));

            // When
            deleteShippingAddressService.execute(command);

            // Then
            verify(shippingAddressPersistenceManager, times(2)).persist(any());
        }

        @Test
        @DisplayName("기본 배송지 삭제 후 다른 배송지가 없으면 승격 없음")
        void shouldNotPromoteWhenNoOtherAddress() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            DeleteShippingAddressCommand command =
                    DeleteShippingAddressCommand.of(memberId, shippingAddressId);
            ShippingAddress defaultAddress = ShippingAddressFixture.createWithId(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId)).thenReturn(defaultAddress);
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);
            when(shippingAddressReadManager.findLatestExcluding(memberId, shippingAddressId))
                    .thenReturn(Optional.empty());

            // When
            deleteShippingAddressService.execute(command);

            // Then
            verify(shippingAddressPersistenceManager, times(1)).persist(any());
        }

        @Test
        @DisplayName("삭제 후 영속화 호출 확인")
        void shouldCallPersistAfterDelete() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            Long shippingAddressId = 1L;
            DeleteShippingAddressCommand command =
                    DeleteShippingAddressCommand.of(memberId, shippingAddressId);
            ShippingAddress shippingAddress =
                    ShippingAddressFixture.createNonDefault(shippingAddressId);

            when(shippingAddressReadManager.findById(shippingAddressId))
                    .thenReturn(shippingAddress);
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);

            // When
            deleteShippingAddressService.execute(command);

            // Then
            verify(shippingAddressReadManager, times(1)).findById(shippingAddressId);
            verify(clockHolder, times(1)).getClock();
            verify(shippingAddressPersistenceManager, times(1)).persist(shippingAddress);
        }
    }
}
