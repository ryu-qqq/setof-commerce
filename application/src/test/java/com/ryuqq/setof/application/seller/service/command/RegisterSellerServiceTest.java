package com.ryuqq.setof.application.seller.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerCommandFixtures;
import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.factory.SellerCommandFactory;
import com.ryuqq.setof.application.seller.internal.SellerRegistrationCoordinator;
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
@DisplayName("RegisterSellerService 단위 테스트")
class RegisterSellerServiceTest {

    @InjectMocks private RegisterSellerService sut;

    @Mock private SellerCommandFactory sellerCommandFactory;
    @Mock private SellerRegistrationCoordinator sellerRegistrationCoordinator;
    @Mock private SellerRegistrationBundle bundle;

    @Nested
    @DisplayName("execute() - 셀러 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 셀러를 등록하고 셀러 ID를 반환한다")
        void execute_ValidCommand_ReturnsSellerId() {
            // given
            RegisterSellerCommand command = SellerCommandFixtures.registerCommand();
            Long expectedSellerId = 1L;

            given(sellerCommandFactory.createRegistrationBundle(command)).willReturn(bundle);
            given(sellerRegistrationCoordinator.register(bundle)).willReturn(expectedSellerId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedSellerId);
            then(sellerCommandFactory).should().createRegistrationBundle(command);
            then(sellerRegistrationCoordinator).should().register(bundle);
        }
    }
}
