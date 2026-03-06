package com.ryuqq.setof.application.productgroupimage.service.command;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.productgroupimage.ProductGroupImageCommandFixtures;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
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
@DisplayName("UpdateProductGroupImagesService 단위 테스트")
class UpdateProductGroupImagesServiceTest {

    @InjectMocks private UpdateProductGroupImagesService sut;

    @Mock private ImageCommandCoordinator imageCommandCoordinator;

    @Nested
    @DisplayName("execute() - 이미지 수정")
    class ExecuteTest {

        @Test
        @DisplayName("커맨드를 Coordinator에게 위임하여 이미지를 수정한다")
        void execute_ValidCommand_DelegatesToCoordinator() {
            // given
            UpdateProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.updateCommand();

            willDoNothing().given(imageCommandCoordinator).update(command);

            // when
            sut.execute(command);

            // then
            then(imageCommandCoordinator).should().update(command);
        }

        @Test
        @DisplayName("썸네일만 있는 수정 커맨드도 Coordinator에게 위임한다")
        void execute_ThumbnailOnlyCommand_DelegatesToCoordinator() {
            // given
            UpdateProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.updateThumbnailOnlyCommand();

            willDoNothing().given(imageCommandCoordinator).update(command);

            // when
            sut.execute(command);

            // then
            then(imageCommandCoordinator).should().update(command);
        }
    }
}
