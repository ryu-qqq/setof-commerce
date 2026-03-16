package com.ryuqq.setof.application.contentpage.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.ContentPageCommandFixtures;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageRegistrationBundle;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;
import com.ryuqq.setof.application.contentpage.facade.ContentPageCommandFacade;
import com.ryuqq.setof.application.contentpage.factory.ContentPageCommandFactory;
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
@DisplayName("RegisterContentPageService 단위 테스트")
class RegisterContentPageServiceTest {

    @InjectMocks private RegisterContentPageService sut;

    @Mock private ContentPageCommandFactory contentPageCommandFactory;
    @Mock private ContentPageCommandFacade contentPageCommandFacade;
    @Mock private ContentPageRegistrationBundle bundle;

    @Nested
    @DisplayName("execute() - 콘텐츠 페이지 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 콘텐츠 페이지를 등록하고 ID를 반환한다")
        void execute_ValidCommand_ReturnsContentPageId() {
            // given
            RegisterContentPageCommand command = ContentPageCommandFixtures.registerCommand();
            Long expectedId = 1L;

            given(contentPageCommandFactory.createRegistrationBundle(command)).willReturn(bundle);
            given(contentPageCommandFacade.register(bundle)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(contentPageCommandFactory).should().createRegistrationBundle(command);
            then(contentPageCommandFacade).should().register(bundle);
        }

        @Test
        @DisplayName("컴포넌트 없는 커맨드로 콘텐츠 페이지를 등록하고 ID를 반환한다")
        void execute_CommandWithoutComponents_ReturnsContentPageId() {
            // given
            RegisterContentPageCommand command =
                    ContentPageCommandFixtures.registerCommandWithoutComponents();
            Long expectedId = 2L;

            given(contentPageCommandFactory.createRegistrationBundle(command)).willReturn(bundle);
            given(contentPageCommandFacade.register(bundle)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(contentPageCommandFactory).should().createRegistrationBundle(command);
            then(contentPageCommandFacade).should().register(bundle);
        }
    }
}
