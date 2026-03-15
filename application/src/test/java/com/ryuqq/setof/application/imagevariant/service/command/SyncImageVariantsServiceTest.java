package com.ryuqq.setof.application.imagevariant.service.command;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.imagevariant.ImageVariantCommandFixtures;
import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;
import com.ryuqq.setof.application.imagevariant.internal.ImageVariantCommandCoordinator;
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
@DisplayName("SyncImageVariantsService 단위 테스트")
class SyncImageVariantsServiceTest {

    @InjectMocks private SyncImageVariantsService sut;

    @Mock private ImageVariantCommandCoordinator coordinator;

    @Nested
    @DisplayName("execute() - 이미지 Variant 동기화")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 이미지 Variant를 동기화한다")
        void execute_ValidCommand_DelegatesToCoordinator() {
            // given
            SyncImageVariantsCommand command = ImageVariantCommandFixtures.syncCommand();
            willDoNothing().given(coordinator).sync(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().sync(command);
        }

        @Test
        @DisplayName("빈 Variant 목록이 포함된 커맨드도 Coordinator에 위임한다")
        void execute_CommandWithEmptyVariants_DelegatesToCoordinator() {
            // given
            SyncImageVariantsCommand command =
                    ImageVariantCommandFixtures.syncCommandWithEmptyVariants();
            willDoNothing().given(coordinator).sync(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().sync(command);
        }

        @Test
        @DisplayName("DESCRIPTION_IMAGE 소스 타입 커맨드도 Coordinator에 위임한다")
        void execute_DescriptionImageCommand_DelegatesToCoordinator() {
            // given
            SyncImageVariantsCommand command =
                    ImageVariantCommandFixtures.syncCommandWithDescriptionImage();
            willDoNothing().given(coordinator).sync(command);

            // when
            sut.execute(command);

            // then
            then(coordinator).should().sync(command);
        }
    }
}
