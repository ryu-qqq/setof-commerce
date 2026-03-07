package com.ryuqq.setof.application.seller.service.command;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.seller.SellerCommandFixtures;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.internal.SellerUpdateCoordinator;
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
@DisplayName("UpdateSellerService 단위 테스트")
class UpdateSellerServiceTest {

    @InjectMocks private UpdateSellerService sut;

    @Mock private SellerUpdateCoordinator sellerUpdateCoordinator;

    @Nested
    @DisplayName("execute() - 셀러 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 셀러를 수정한다")
        void execute_ValidCommand_DelegatesUpdateToCoordinator() {
            // given
            UpdateSellerCommand command = SellerCommandFixtures.updateCommand(1L);

            willDoNothing().given(sellerUpdateCoordinator).update(command);

            // when
            sut.execute(command);

            // then
            then(sellerUpdateCoordinator).should().update(command);
        }

        @Test
        @DisplayName("선택 필드가 없는 커맨드로 셀러 기본 정보만 수정한다")
        void execute_CommandWithoutOptionalFields_DelegatesUpdateToCoordinator() {
            // given
            UpdateSellerCommand command = SellerCommandFixtures.updateCommandWithoutOptional(1L);

            willDoNothing().given(sellerUpdateCoordinator).update(command);

            // when
            sut.execute(command);

            // then
            then(sellerUpdateCoordinator).should().update(command);
        }
    }
}
