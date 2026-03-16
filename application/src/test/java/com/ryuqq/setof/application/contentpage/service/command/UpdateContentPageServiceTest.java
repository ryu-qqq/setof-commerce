package com.ryuqq.setof.application.contentpage.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.contentpage.ContentPageCommandFixtures;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageUpdateBundle;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.facade.ContentPageCommandFacade;
import com.ryuqq.setof.application.contentpage.factory.ContentPageCommandFactory;
import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.application.contentpage.manager.DisplayComponentReadManager;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.List;
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
@DisplayName("UpdateContentPageService 단위 테스트")
class UpdateContentPageServiceTest {

    @InjectMocks private UpdateContentPageService sut;

    @Mock private ContentPageQueryManager contentPageQueryManager;
    @Mock private DisplayComponentReadManager displayComponentReadManager;
    @Mock private ContentPageCommandFactory contentPageCommandFactory;
    @Mock private ContentPageCommandFacade contentPageCommandFacade;
    @Mock private ContentPageUpdateBundle bundle;

    @Nested
    @DisplayName("execute() - 콘텐츠 페이지 수정")
    class ExecuteTest {

        @Test
        @DisplayName("기존 페이지와 컴포넌트를 조회하고 번들 생성 후 Facade에 위임한다")
        void execute_ValidCommand_QueriesAndDelegatesUpdateToFacade() {
            // given
            long contentPageId = 1L;
            UpdateContentPageCommand command =
                    ContentPageCommandFixtures.updateCommand(contentPageId);
            ContentPage existingContentPage = ContentPageFixtures.activeContentPage(contentPageId);
            ContentPageSearchCriteria expectedCriteria =
                    new ContentPageSearchCriteria(contentPageId, true);
            List<DisplayComponent> existingComponents =
                    List.of(ContentPageFixtures.productComponent(1L, OrderType.RECOMMEND, 10));

            given(contentPageQueryManager.findByIdOrThrow(contentPageId))
                    .willReturn(existingContentPage);
            given(displayComponentReadManager.findByContentPage(expectedCriteria))
                    .willReturn(existingComponents);
            given(
                            contentPageCommandFactory.createUpdateBundle(
                                    existingContentPage, existingComponents, command))
                    .willReturn(bundle);
            willDoNothing().given(contentPageCommandFacade).update(bundle, command);

            // when
            sut.execute(command);

            // then
            then(contentPageQueryManager).should().findByIdOrThrow(contentPageId);
            then(displayComponentReadManager).should().findByContentPage(expectedCriteria);
            then(contentPageCommandFactory)
                    .should()
                    .createUpdateBundle(existingContentPage, existingComponents, command);
            then(contentPageCommandFacade).should().update(bundle, command);
        }

        @Test
        @DisplayName("기존 컴포넌트가 없는 경우에도 정상적으로 수정 처리를 위임한다")
        void execute_NoExistingComponents_DelegatesUpdateToFacade() {
            // given
            long contentPageId = 2L;
            UpdateContentPageCommand command =
                    ContentPageCommandFixtures.updateCommandWithoutComponents(contentPageId);
            ContentPage existingContentPage = ContentPageFixtures.activeContentPage(contentPageId);
            ContentPageSearchCriteria expectedCriteria =
                    new ContentPageSearchCriteria(contentPageId, true);
            List<DisplayComponent> emptyComponents = List.of();

            given(contentPageQueryManager.findByIdOrThrow(contentPageId))
                    .willReturn(existingContentPage);
            given(displayComponentReadManager.findByContentPage(expectedCriteria))
                    .willReturn(emptyComponents);
            given(
                            contentPageCommandFactory.createUpdateBundle(
                                    existingContentPage, emptyComponents, command))
                    .willReturn(bundle);
            willDoNothing().given(contentPageCommandFacade).update(bundle, command);

            // when
            sut.execute(command);

            // then
            then(contentPageQueryManager).should().findByIdOrThrow(contentPageId);
            then(displayComponentReadManager).should().findByContentPage(expectedCriteria);
            then(contentPageCommandFactory)
                    .should()
                    .createUpdateBundle(existingContentPage, emptyComponents, command);
            then(contentPageCommandFacade).should().update(bundle, command);
        }
    }
}
