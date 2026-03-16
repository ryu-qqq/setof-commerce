package com.ryuqq.setof.application.productgroup.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupCommandFactory;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupCommandCoordinator;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupReadManager;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateProductGroupBasicInfoService 단위 테스트")
class UpdateProductGroupBasicInfoServiceTest {

    @InjectMocks private UpdateProductGroupBasicInfoService sut;

    @Mock private ProductGroupReadManager readManager;
    @Mock private ProductGroupCommandFactory commandFactory;
    @Mock private ProductGroupCommandCoordinator commandCoordinator;
    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("execute() - 상품그룹 기본정보 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 상품그룹 기본정보를 수정한다")
        void execute_ValidCommand_UpdatesBasicInfo() {
            // given
            long productGroupId = 1L;
            UpdateProductGroupBasicInfoCommand command =
                    ProductGroupCommandFixtures.updateBasicInfoCommand(productGroupId);
            ProductGroup productGroup = ProductGroupFixtures.activeProductGroup(productGroupId);
            ProductGroupUpdateData updateData = Mockito.mock(ProductGroupUpdateData.class);
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(readManager.getById(ProductGroupId.of(productGroupId))).willReturn(productGroup);
            given(timeProvider.now()).willReturn(now);
            given(commandFactory.createUpdateData(command, productGroup.optionType(), now))
                    .willReturn(updateData);
            willDoNothing().given(commandCoordinator).updateBasicInfo(productGroup, updateData);

            // when
            sut.execute(command);

            // then
            then(readManager).should().getById(ProductGroupId.of(productGroupId));
            then(timeProvider).should().now();
            then(commandFactory).should().createUpdateData(command, productGroup.optionType(), now);
            then(commandCoordinator).should().updateBasicInfo(productGroup, updateData);
        }

        @Test
        @DisplayName("기존 optionType이 CommandFactory에 그대로 전달된다")
        void execute_ExistingOptionTypePassedToFactory() {
            // given
            long productGroupId = 1L;
            UpdateProductGroupBasicInfoCommand command =
                    ProductGroupCommandFixtures.updateBasicInfoCommand(productGroupId);
            ProductGroup productGroup = ProductGroupFixtures.activeProductGroup(productGroupId);
            OptionType existingOptionType = productGroup.optionType();
            ProductGroupUpdateData updateData = Mockito.mock(ProductGroupUpdateData.class);
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(readManager.getById(ProductGroupId.of(productGroupId))).willReturn(productGroup);
            given(timeProvider.now()).willReturn(now);
            given(commandFactory.createUpdateData(command, existingOptionType, now))
                    .willReturn(updateData);
            willDoNothing().given(commandCoordinator).updateBasicInfo(productGroup, updateData);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().createUpdateData(command, existingOptionType, now);
        }
    }
}
