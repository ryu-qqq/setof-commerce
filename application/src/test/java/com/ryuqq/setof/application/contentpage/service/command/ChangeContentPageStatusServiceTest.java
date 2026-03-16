package com.ryuqq.setof.application.contentpage.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.contentpage.ContentPageCommandFixtures;
import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;
import com.ryuqq.setof.application.contentpage.factory.ContentPageCommandFactory;
import com.ryuqq.setof.application.contentpage.manager.ContentPageCommandManager;
import com.ryuqq.setof.application.contentpage.validator.ContentPageValidator;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.time.Instant;
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
@DisplayName("ChangeContentPageStatusService 단위 테스트")
class ChangeContentPageStatusServiceTest {

    @InjectMocks private ChangeContentPageStatusService sut;

    @Mock private ContentPageCommandFactory contentPageCommandFactory;
    @Mock private ContentPageValidator contentPageValidator;
    @Mock private ContentPageCommandManager contentPageCommandManager;

    @Nested
    @DisplayName("execute() - 콘텐츠 페이지 노출 상태 변경")
    class ExecuteTest {

        @Test
        @DisplayName("활성화 커맨드로 콘텐츠 페이지를 활성화한다")
        void execute_ActivateCommand_ChangesStatusToActive() {
            // given
            long contentPageId = 1L;
            ChangeContentPageStatusCommand command =
                    ContentPageCommandFixtures.activateCommand(contentPageId);
            ContentPageId id = ContentPageId.of(contentPageId);
            Instant now = Instant.parse("2025-06-01T00:00:00Z");
            StatusChangeContext<ContentPageId> context = new StatusChangeContext<>(id, now);
            ContentPage contentPage = ContentPageFixtures.inactiveContentPage();

            given(contentPageCommandFactory.createStatusChangeContext(command)).willReturn(context);
            given(contentPageValidator.findExistingOrThrow(id)).willReturn(contentPage);
            given(contentPageCommandManager.persist(contentPage)).willReturn(contentPageId);

            // when
            sut.execute(command);

            // then
            then(contentPageCommandFactory).should().createStatusChangeContext(command);
            then(contentPageValidator).should().findExistingOrThrow(id);
            then(contentPageCommandManager).should().persist(contentPage);
        }

        @Test
        @DisplayName("비활성화 커맨드로 콘텐츠 페이지를 비활성화한다")
        void execute_DeactivateCommand_ChangesStatusToInactive() {
            // given
            long contentPageId = 1L;
            ChangeContentPageStatusCommand command =
                    ContentPageCommandFixtures.deactivateCommand(contentPageId);
            ContentPageId id = ContentPageId.of(contentPageId);
            Instant now = Instant.parse("2025-06-01T00:00:00Z");
            StatusChangeContext<ContentPageId> context = new StatusChangeContext<>(id, now);
            ContentPage contentPage = ContentPageFixtures.activeContentPage(contentPageId);

            given(contentPageCommandFactory.createStatusChangeContext(command)).willReturn(context);
            given(contentPageValidator.findExistingOrThrow(id)).willReturn(contentPage);
            given(contentPageCommandManager.persist(contentPage)).willReturn(contentPageId);

            // when
            sut.execute(command);

            // then
            then(contentPageCommandFactory).should().createStatusChangeContext(command);
            then(contentPageValidator).should().findExistingOrThrow(id);
            then(contentPageCommandManager).should().persist(contentPage);
        }
    }
}
