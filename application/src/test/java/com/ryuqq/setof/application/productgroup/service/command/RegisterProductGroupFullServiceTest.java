package com.ryuqq.setof.application.productgroup.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupBundleFactory;
import com.ryuqq.setof.application.productgroup.internal.FullProductGroupRegistrationCoordinator;
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
@DisplayName("RegisterProductGroupFullService 단위 테스트")
class RegisterProductGroupFullServiceTest {

    @InjectMocks private RegisterProductGroupFullService sut;

    @Mock private ProductGroupBundleFactory bundleFactory;
    @Mock private FullProductGroupRegistrationCoordinator registrationCoordinator;

    @Nested
    @DisplayName("execute() - 상품그룹 전체 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 상품그룹을 등록하고 ID를 반환한다")
        void execute_ValidCommand_ReturnsProductGroupId() {
            // given
            RegisterProductGroupCommand command = ProductGroupCommandFixtures.registerCommand();
            ProductGroupRegistrationBundle bundle =
                    ProductGroupCommandFixtures.registrationBundle();
            Long expectedId = 1L;

            given(bundleFactory.createRegistrationBundle(command)).willReturn(bundle);
            given(registrationCoordinator.register(bundle)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(bundleFactory).should().createRegistrationBundle(command);
            then(registrationCoordinator).should().register(bundle);
        }
    }
}
