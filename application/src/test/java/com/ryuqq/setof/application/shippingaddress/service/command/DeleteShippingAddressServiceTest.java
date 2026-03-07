package com.ryuqq.setof.application.shippingaddress.service.command;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.shippingaddress.ShippingAddressCommandFixtures;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.internal.ShippingAddressUpdateCoordinator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteShippingAddressService 단위 테스트")
class DeleteShippingAddressServiceTest {

    @InjectMocks private DeleteShippingAddressService sut;

    @Mock private ShippingAddressUpdateCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 배송지 삭제")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 배송지를 삭제하고 coordinator.delete를 호출한다")
        void execute_ValidCommand_CallsCoordinatorDelete() {
            // given
            DeleteShippingAddressCommand command = ShippingAddressCommandFixtures.deleteCommand();

            willDoNothing().given(coordinator).delete(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().delete(command);
        }

        @Test
        @DisplayName("특정 userId와 shippingAddressId 커맨드로 coordinator.delete를 호출한다")
        void execute_SpecificIds_CallsCoordinatorDelete() {
            // given
            Long userId = 5L;
            Long shippingAddressId = 999L;
            DeleteShippingAddressCommand command =
                    ShippingAddressCommandFixtures.deleteCommand(userId, shippingAddressId);

            willDoNothing().given(coordinator).delete(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().delete(command);
        }
    }
}
