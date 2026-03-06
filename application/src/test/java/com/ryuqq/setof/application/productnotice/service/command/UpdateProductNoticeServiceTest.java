package com.ryuqq.setof.application.productnotice.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.application.productnotice.ProductNoticeCommandFixtures;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.internal.ProductNoticeCommandCoordinator;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.exception.ProductNoticeNotFoundException;
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
@DisplayName("UpdateProductNoticeService 단위 테스트")
class UpdateProductNoticeServiceTest {

    @InjectMocks private UpdateProductNoticeService sut;

    @Mock private ProductNoticeCommandCoordinator productNoticeCommandCoordinator;

    @Nested
    @DisplayName("execute() - 상품고시 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 상품고시를 수정한다")
        void execute_ValidCommand_InvokesCoordinatorUpdate() {
            // given
            UpdateProductNoticeCommand command = ProductNoticeCommandFixtures.updateCommand();
            willDoNothing().given(productNoticeCommandCoordinator).update(command);

            // when
            sut.execute(command);

            // then
            then(productNoticeCommandCoordinator).should().update(command);
        }

        @Test
        @DisplayName("존재하지 않는 Notice를 수정하면 ProductNoticeNotFoundException이 발생한다")
        void execute_NoticeNotFound_ThrowsNotFoundException() {
            // given
            UpdateProductNoticeCommand command = ProductNoticeCommandFixtures.updateCommand(999L);
            ProductGroupId pgId = ProductGroupId.of(999L);

            willThrow(new ProductNoticeNotFoundException(pgId))
                    .given(productNoticeCommandCoordinator)
                    .update(command);

            // when & then
            assertThatThrownBy(() -> sut.execute(command))
                    .isInstanceOf(ProductNoticeNotFoundException.class);
        }
    }
}
