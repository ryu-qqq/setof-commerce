package com.ryuqq.setof.application.productnotice.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productnotice.ProductNoticeCommandFixtures;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.internal.ProductNoticeCommandCoordinator;
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
@DisplayName("RegisterProductNoticeService 단위 테스트")
class RegisterProductNoticeServiceTest {

    @InjectMocks private RegisterProductNoticeService sut;

    @Mock private ProductNoticeCommandCoordinator productNoticeCommandCoordinator;

    @Nested
    @DisplayName("execute() - 상품고시 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 상품고시를 등록하고 ID를 반환한다")
        void execute_ValidCommand_ReturnsNoticeId() {
            // given
            RegisterProductNoticeCommand command = ProductNoticeCommandFixtures.registerCommand();
            Long expectedId = 1L;

            given(productNoticeCommandCoordinator.register(command)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(productNoticeCommandCoordinator).should().register(command);
        }

        @Test
        @DisplayName("다른 ProductGroupId를 가진 커맨드로도 상품고시를 등록할 수 있다")
        void execute_DifferentProductGroupId_ReturnsNoticeId() {
            // given
            RegisterProductNoticeCommand command =
                    ProductNoticeCommandFixtures.registerCommand(200L);
            Long expectedId = 5L;

            given(productNoticeCommandCoordinator.register(command)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(productNoticeCommandCoordinator).should().register(command);
        }
    }
}
