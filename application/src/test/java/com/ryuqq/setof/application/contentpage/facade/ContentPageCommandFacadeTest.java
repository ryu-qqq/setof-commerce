package com.ryuqq.setof.application.contentpage.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.contentpage.ContentPageCommandFixtures;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageRegistrationBundle;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageUpdateBundle;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.factory.ContentPageCommandFactory;
import com.ryuqq.setof.application.contentpage.manager.ContentPageCommandManager;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.DisplayComponentDiff;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.time.Instant;
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
@DisplayName("ContentPageCommandFacade 단위 테스트")
class ContentPageCommandFacadeTest {

    @InjectMocks private ContentPageCommandFacade sut;

    @Mock private ContentPageCommandFactory contentPageCommandFactory;
    @Mock private ContentPageCommandManager contentPageCommandManager;

    @Nested
    @DisplayName("register() - 콘텐츠 페이지 등록")
    class RegisterTest {

        @Test
        @DisplayName("컴포넌트가 있는 번들로 등록 시 페이지와 컴포넌트를 모두 저장하고 ID를 반환한다")
        void register_BundleWithComponents_SavesPageAndComponentsAndReturnsId() {
            // given
            ContentPage contentPage = ContentPageFixtures.newContentPage();
            ContentPageRegistrationBundle bundle =
                    new ContentPageRegistrationBundle(
                            contentPage,
                            List.of(ContentPageCommandFixtures.registerDisplayComponentCommand()));
            Long expectedId = 1L;
            List<DisplayComponent> components =
                    List.of(ContentPageFixtures.productComponent(1L, OrderType.RECOMMEND, 10));

            given(contentPageCommandManager.persist(contentPage)).willReturn(expectedId);
            given(contentPageCommandFactory.createComponents(eq(expectedId), any()))
                    .willReturn(components);
            willDoNothing().given(contentPageCommandManager).persistComponents(components);

            // when
            Long result = sut.register(bundle);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(contentPageCommandManager).should().persist(contentPage);
            then(contentPageCommandFactory).should().createComponents(eq(expectedId), any());
            then(contentPageCommandManager).should().persistComponents(components);
        }

        @Test
        @DisplayName("컴포넌트가 없는 번들로 등록 시 페이지만 저장하고 ID를 반환한다")
        void register_BundleWithoutComponents_SavesOnlyPageAndReturnsId() {
            // given
            ContentPage contentPage = ContentPageFixtures.newContentPage();
            ContentPageRegistrationBundle bundle =
                    new ContentPageRegistrationBundle(contentPage, List.of());
            Long expectedId = 1L;

            given(contentPageCommandManager.persist(contentPage)).willReturn(expectedId);

            // when
            Long result = sut.register(bundle);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(contentPageCommandManager).should().persist(contentPage);
            then(contentPageCommandFactory).shouldHaveNoInteractions();
            then(contentPageCommandManager).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("update() - 콘텐츠 페이지 수정")
    class UpdateTest {

        @Test
        @DisplayName("변경이 있는 경우 페이지 저장과 컴포넌트 diff 저장을 모두 수행한다")
        void update_BundleWithChanges_SavesPageAndComponentDiff() {
            // given
            ContentPage existingContentPage = ContentPageFixtures.activeContentPage();
            List<DisplayComponent> existingComponents =
                    List.of(ContentPageFixtures.productComponent(1L, OrderType.RECOMMEND, 10));
            UpdateContentPageCommand command = ContentPageCommandFixtures.updateCommand(1L);
            Instant now = Instant.parse("2025-06-01T00:00:00Z");

            ContentPageUpdateBundle bundle =
                    new ContentPageUpdateBundle(
                            existingContentPage, existingComponents, command.components(), now);

            List<DisplayComponent> incoming =
                    List.of(ContentPageFixtures.productComponent(2L, OrderType.NONE, 5));
            DisplayComponentDiff diff =
                    new DisplayComponentDiff(incoming, List.of(), List.of(), now);

            given(contentPageCommandFactory.createUpdateData(command, now))
                    .willReturn(ContentPageFixtures.contentPageUpdateData());
            given(contentPageCommandManager.persist(existingContentPage)).willReturn(1L);
            given(contentPageCommandFactory.createComponentsForUpdate(anyLong(), any(), eq(now)))
                    .willReturn(incoming);
            willDoNothing().given(contentPageCommandManager).persistComponentDiff(any());

            // when
            sut.update(bundle, command);

            // then
            then(contentPageCommandFactory).should().createUpdateData(command, now);
            then(contentPageCommandManager).should().persist(existingContentPage);
            then(contentPageCommandFactory)
                    .should()
                    .createComponentsForUpdate(anyLong(), any(), eq(now));
            then(contentPageCommandManager).should().persistComponentDiff(any());
        }

        @Test
        @DisplayName("incoming 컴포넌트 커맨드가 없으면 컴포넌트 diff 처리를 수행하지 않는다")
        void update_BundleWithEmptyComponentCommands_SkipsComponentDiff() {
            // given
            ContentPage existingContentPage = ContentPageFixtures.activeContentPage();
            UpdateContentPageCommand command =
                    ContentPageCommandFixtures.updateCommandWithoutComponents(1L);
            Instant now = Instant.parse("2025-06-01T00:00:00Z");

            ContentPageUpdateBundle bundle =
                    new ContentPageUpdateBundle(existingContentPage, List.of(), List.of(), now);

            given(contentPageCommandFactory.createUpdateData(command, now))
                    .willReturn(ContentPageFixtures.contentPageUpdateData());
            given(contentPageCommandManager.persist(existingContentPage)).willReturn(1L);

            // when
            sut.update(bundle, command);

            // then
            then(contentPageCommandFactory).should().createUpdateData(command, now);
            then(contentPageCommandManager).should().persist(existingContentPage);
            then(contentPageCommandFactory).shouldHaveNoMoreInteractions();
            then(contentPageCommandManager).shouldHaveNoMoreInteractions();
        }
    }
}
