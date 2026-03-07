package com.ryuqq.setof.application.shippingaddress.service.command;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.shippingaddress.ShippingAddressCommandFixtures;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
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
@DisplayName("UpdateShippingAddressService 단위 테스트")
class UpdateShippingAddressServiceTest {

    @InjectMocks private UpdateShippingAddressService sut;

    @Mock private ShippingAddressUpdateCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 배송지 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 배송지를 수정하고 coordinator.update를 호출한다")
        void execute_ValidCommand_CallsCoordinatorUpdate() {
            // given
            UpdateShippingAddressCommand command = ShippingAddressCommandFixtures.updateCommand();

            willDoNothing().given(coordinator).update(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().update(command);
        }

        @Test
        @DisplayName("기본 배송지로 수정 커맨드도 coordinator.update를 호출한다")
        void execute_DefaultAddressCommand_CallsCoordinatorUpdate() {
            // given
            UpdateShippingAddressCommand command =
                    ShippingAddressCommandFixtures.updateCommandAsDefault();

            willDoNothing().given(coordinator).update(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().update(command);
        }
    }
}
