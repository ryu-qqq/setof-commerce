package com.ryuqq.setof.application.shippingaddress.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.shippingaddress.ShippingAddressCommandFixtures;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.internal.ShippingAddressRegistrationCoordinator;
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
@DisplayName("RegisterShippingAddressService 단위 테스트")
class RegisterShippingAddressServiceTest {

    @InjectMocks private RegisterShippingAddressService sut;

    @Mock private ShippingAddressRegistrationCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 배송지 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 배송지를 등록하고 배송지 ID를 반환한다")
        void execute_ValidCommand_ReturnsShippingAddressId() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommand();
            Long expectedId = 100L;

            given(coordinator.register(command)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(coordinator).should().register(command);
        }

        @Test
        @DisplayName("기본 배송지 커맨드로 등록 시 coordinator.register를 호출한다")
        void execute_DefaultAddressCommand_CallsCoordinatorRegister() {
            // given
            RegisterShippingAddressCommand command =
                    ShippingAddressCommandFixtures.registerCommandAsDefault();
            Long expectedId = 200L;

            given(coordinator.register(command)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(coordinator).should().register(command);
        }
    }
}
