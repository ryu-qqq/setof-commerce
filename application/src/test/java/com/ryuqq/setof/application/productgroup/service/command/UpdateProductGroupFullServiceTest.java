package com.ryuqq.setof.application.productgroup.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupBundleFactory;
import com.ryuqq.setof.application.productgroup.internal.FullProductGroupUpdateCoordinator;
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
@DisplayName("UpdateProductGroupFullService 단위 테스트")
class UpdateProductGroupFullServiceTest {

    @InjectMocks private UpdateProductGroupFullService sut;

    @Mock private ProductGroupBundleFactory bundleFactory;
    @Mock private FullProductGroupUpdateCoordinator updateCoordinator;

    @Nested
    @DisplayName("execute() - 상품그룹 전체 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 상품그룹을 수정한다")
        void execute_ValidCommand_UpdatesProductGroup() {
            // given
            long productGroupId = 1L;
            UpdateProductGroupFullCommand command =
                    ProductGroupCommandFixtures.updateFullCommand(productGroupId);
            ProductGroupUpdateBundle bundle =
                    ProductGroupCommandFixtures.updateBundle(productGroupId);

            given(bundleFactory.createUpdateBundle(command)).willReturn(bundle);

            // when
            sut.execute(command);

            // then
            then(bundleFactory).should().createUpdateBundle(command);
            then(updateCoordinator).should().update(bundle);
        }
    }
}
