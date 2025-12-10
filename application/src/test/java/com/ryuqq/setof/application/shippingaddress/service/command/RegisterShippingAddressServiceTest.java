package com.ryuqq.setof.application.shippingaddress.service.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.factory.command.ShippingAddressCommandFactory;
import com.ryuqq.setof.application.shippingaddress.manager.command.ShippingAddressPersistenceManager;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingaddress.ShippingAddressFixture;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressLimitExceededException;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("RegisterShippingAddressService")
@ExtendWith(MockitoExtension.class)
class RegisterShippingAddressServiceTest {

    @Mock private ShippingAddressReadManager shippingAddressReadManager;
    @Mock private ShippingAddressPersistenceManager shippingAddressPersistenceManager;
    @Mock private ShippingAddressCommandFactory shippingAddressCommandFactory;
    @Mock private ClockHolder clockHolder;

    private ShippingAddressAssembler shippingAddressAssembler;
    private RegisterShippingAddressService registerShippingAddressService;

    @BeforeEach
    void setUp() {
        shippingAddressAssembler = new ShippingAddressAssembler();
        registerShippingAddressService =
                new RegisterShippingAddressService(
                        shippingAddressReadManager,
                        shippingAddressPersistenceManager,
                        shippingAddressCommandFactory,
                        shippingAddressAssembler,
                        clockHolder);
    }

    @Nested
    @DisplayName("execute")
    class ExecuteTest {

        @Test
        @DisplayName("첫 번째 배송지 등록 시 자동으로 기본 배송지로 설정")
        void shouldSetDefaultWhenFirstAddress() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            RegisterShippingAddressCommand command = createCommand(memberId, false);
            ShippingAddress shippingAddress = ShippingAddressFixture.createWithId(1L);
            ShippingAddressId savedId = ShippingAddressId.of(1L);

            when(shippingAddressReadManager.countByMemberId(memberId)).thenReturn(0L);
            when(shippingAddressReadManager.findDefaultByMemberId(memberId))
                    .thenReturn(Optional.empty());
            when(shippingAddressCommandFactory.create(any())).thenReturn(shippingAddress);
            when(shippingAddressPersistenceManager.persist(any())).thenReturn(savedId);
            when(shippingAddressReadManager.findById(1L)).thenReturn(shippingAddress);

            // When
            ShippingAddressResponse result = registerShippingAddressService.execute(command);

            // Then
            assertNotNull(result);
            verify(shippingAddressReadManager, times(2)).countByMemberId(memberId);
            verify(shippingAddressPersistenceManager, times(1)).persist(any());
        }

        @Test
        @DisplayName("기본 배송지로 등록 시 기존 기본 배송지 해제")
        void shouldUnsetPreviousDefaultWhenNewDefault() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            RegisterShippingAddressCommand command = createCommand(memberId, true);
            ShippingAddress existingDefault = ShippingAddressFixture.createWithId(1L);
            ShippingAddress newAddress = ShippingAddressFixture.createWithId(2L);
            ShippingAddressId savedId = ShippingAddressId.of(2L);

            when(shippingAddressReadManager.countByMemberId(memberId)).thenReturn(1L);
            when(shippingAddressReadManager.findDefaultByMemberId(memberId))
                    .thenReturn(Optional.of(existingDefault));
            when(clockHolder.getClock()).thenReturn(ShippingAddressFixture.FIXED_CLOCK);
            when(shippingAddressCommandFactory.create(any())).thenReturn(newAddress);
            when(shippingAddressPersistenceManager.persist(any())).thenReturn(savedId);
            when(shippingAddressReadManager.findById(2L)).thenReturn(newAddress);

            // When
            ShippingAddressResponse result = registerShippingAddressService.execute(command);

            // Then
            assertNotNull(result);
            verify(shippingAddressPersistenceManager, times(2)).persist(any());
        }

        @Test
        @DisplayName("최대 배송지 개수 초과 시 예외 발생")
        void shouldThrowExceptionWhenLimitExceeded() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            RegisterShippingAddressCommand command = createCommand(memberId, false);

            when(shippingAddressReadManager.countByMemberId(memberId)).thenReturn(5L);

            // When & Then
            assertThrows(
                    ShippingAddressLimitExceededException.class,
                    () -> registerShippingAddressService.execute(command));
            verify(shippingAddressPersistenceManager, never()).persist(any());
        }

        @Test
        @DisplayName("기본 배송지 아닌 경우 기존 기본 배송지 유지")
        void shouldKeepExistingDefaultWhenNotDefault() {
            // Given
            UUID memberId = ShippingAddressFixture.DEFAULT_MEMBER_ID;
            RegisterShippingAddressCommand command = createCommand(memberId, false);
            ShippingAddress newAddress = ShippingAddressFixture.createNonDefault(2L);
            ShippingAddressId savedId = ShippingAddressId.of(2L);

            when(shippingAddressReadManager.countByMemberId(memberId)).thenReturn(1L);
            when(shippingAddressCommandFactory.create(any())).thenReturn(newAddress);
            when(shippingAddressPersistenceManager.persist(any())).thenReturn(savedId);
            when(shippingAddressReadManager.findById(2L)).thenReturn(newAddress);

            // When
            ShippingAddressResponse result = registerShippingAddressService.execute(command);

            // Then
            assertNotNull(result);
            verify(shippingAddressReadManager, never()).findDefaultByMemberId(any());
        }

        private RegisterShippingAddressCommand createCommand(UUID memberId, boolean isDefault) {
            return RegisterShippingAddressCommand.of(
                    memberId,
                    "집",
                    "홍길동",
                    "01012345678",
                    "서울시 강남구 테헤란로 123",
                    "서울시 강남구 역삼동 123-45",
                    "101동 1001호",
                    "06234",
                    "문 앞에 놔주세요",
                    isDefault);
        }
    }
}
