package com.ryuqq.setof.application.contentpage.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.contentpage.ContentPageCommandFixtures;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageRegistrationBundle;
import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageUpdateBundle;
import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterDisplayComponentCommand;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPageUpdateData;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
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
@DisplayName("ContentPageCommandFactory 단위 테스트")
class ContentPageCommandFactoryTest {

    @InjectMocks private ContentPageCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    private static final Instant FIXED_NOW = Instant.parse("2025-06-01T00:00:00Z");

    @Nested
    @DisplayName("createRegistrationBundle() - 등록 번들 생성")
    class CreateRegistrationBundleTest {

        @Test
        @DisplayName("커맨드로 ContentPageRegistrationBundle을 생성한다")
        void createRegistrationBundle_ValidCommand_ReturnsBundle() {
            // given
            RegisterContentPageCommand command = ContentPageCommandFixtures.registerCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            ContentPageRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle).isNotNull();
            assertThat(bundle.contentPage()).isNotNull();
            assertThat(bundle.componentCommands()).isNotEmpty();
        }

        @Test
        @DisplayName("커맨드의 타이틀이 Bundle 내 ContentPage에 반영된다")
        void createRegistrationBundle_TitleReflected_InBundle() {
            // given
            RegisterContentPageCommand command = ContentPageCommandFixtures.registerCommand();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            ContentPageRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.contentPage().title()).isEqualTo(command.title());
        }

        @Test
        @DisplayName("컴포넌트 없는 커맨드도 Bundle 생성에 성공한다")
        void createRegistrationBundle_CommandWithoutComponents_ReturnsBundleWithEmptyComponents() {
            // given
            RegisterContentPageCommand command =
                    ContentPageCommandFixtures.registerCommandWithoutComponents();
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            ContentPageRegistrationBundle bundle = sut.createRegistrationBundle(command);

            // then
            assertThat(bundle.contentPage()).isNotNull();
            assertThat(bundle.componentCommands()).isEmpty();
            assertThat(bundle.hasComponents()).isFalse();
        }
    }

    @Nested
    @DisplayName("createUpdateBundle() - 수정 번들 생성")
    class CreateUpdateBundleTest {

        @Test
        @DisplayName("기존 ContentPage + 컴포넌트 목록 + 커맨드로 UpdateBundle을 생성한다")
        void createUpdateBundle_ValidArgs_ReturnsUpdateBundle() {
            // given
            ContentPage existingContentPage = ContentPageFixtures.activeContentPage();
            List<DisplayComponent> existingComponents =
                    List.of(ContentPageFixtures.productComponent(1L, OrderType.RECOMMEND, 10));
            UpdateContentPageCommand command = ContentPageCommandFixtures.updateCommand(1L);
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            ContentPageUpdateBundle bundle =
                    sut.createUpdateBundle(existingContentPage, existingComponents, command);

            // then
            assertThat(bundle).isNotNull();
            assertThat(bundle.contentPage()).isEqualTo(existingContentPage);
            assertThat(bundle.existingComponents()).isEqualTo(existingComponents);
            assertThat(bundle.updatedAt()).isEqualTo(FIXED_NOW);
        }

        @Test
        @DisplayName("수정 커맨드의 컴포넌트 Command 목록이 Bundle에 포함된다")
        void createUpdateBundle_ComponentCommandsIncluded_InBundle() {
            // given
            ContentPage existingContentPage = ContentPageFixtures.activeContentPage();
            List<DisplayComponent> existingComponents = List.of();
            UpdateContentPageCommand command = ContentPageCommandFixtures.updateCommand(1L);
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            ContentPageUpdateBundle bundle =
                    sut.createUpdateBundle(existingContentPage, existingComponents, command);

            // then
            assertThat(bundle.incomingComponentCommands()).isEqualTo(command.components());
        }
    }

    @Nested
    @DisplayName("createStatusChangeContext() - 상태 변경 컨텍스트 생성")
    class CreateStatusChangeContextTest {

        @Test
        @DisplayName("활성화 커맨드로 StatusChangeContext를 생성한다")
        void createStatusChangeContext_ActivateCommand_ReturnsContext() {
            // given
            ChangeContentPageStatusCommand command = ContentPageCommandFixtures.activateCommand(1L);
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<ContentPageId> context = sut.createStatusChangeContext(command);

            // then
            assertThat(context).isNotNull();
            assertThat(context.id().value()).isEqualTo(command.id());
            assertThat(context.changedAt()).isEqualTo(FIXED_NOW);
        }

        @Test
        @DisplayName("비활성화 커맨드로 StatusChangeContext를 생성한다")
        void createStatusChangeContext_DeactivateCommand_ReturnsContext() {
            // given
            ChangeContentPageStatusCommand command =
                    ContentPageCommandFixtures.deactivateCommand(2L);
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<ContentPageId> context = sut.createStatusChangeContext(command);

            // then
            assertThat(context.id().value()).isEqualTo(2L);
            assertThat(context.changedAt()).isEqualTo(FIXED_NOW);
        }
    }

    @Nested
    @DisplayName("createComponents() - 신규 컴포넌트 목록 생성")
    class CreateComponentsTest {

        @Test
        @DisplayName("커맨드 목록으로 DisplayComponent 목록을 생성한다")
        void createComponents_ValidCommands_ReturnsDisplayComponents() {
            // given
            long contentPageId = 1L;
            List<RegisterDisplayComponentCommand> commands =
                    List.of(ContentPageCommandFixtures.registerDisplayComponentCommand());
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            List<DisplayComponent> result = sut.createComponents(contentPageId, commands);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).contentPageId()).isEqualTo(contentPageId);
        }

        @Test
        @DisplayName("ViewExtensionCommand가 있으면 ViewExtension이 포함된 컴포넌트를 생성한다")
        void createComponents_WithViewExtensionCommand_ReturnsComponentWithViewExtension() {
            // given
            long contentPageId = 1L;
            List<RegisterDisplayComponentCommand> commands =
                    List.of(
                            ContentPageCommandFixtures
                                    .registerDisplayComponentCommandWithViewExtension());
            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            List<DisplayComponent> result = sut.createComponents(contentPageId, commands);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).viewExtension()).isNotNull();
        }

        @Test
        @DisplayName("빈 커맨드 목록으로 빈 컴포넌트 목록을 반환한다")
        void createComponents_EmptyCommands_ReturnsEmptyList() {
            // given
            long contentPageId = 1L;
            List<RegisterDisplayComponentCommand> emptyCommands = List.of();

            // when
            List<DisplayComponent> result = sut.createComponents(contentPageId, emptyCommands);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("createComponentsForUpdate() - 수정용 컴포넌트 목록 생성")
    class CreateComponentsForUpdateTest {

        @Test
        @DisplayName("수정용 커맨드 목록으로 DisplayComponent 목록을 생성한다")
        void createComponentsForUpdate_ValidCommands_ReturnsDisplayComponents() {
            // given
            long contentPageId = 1L;
            List<RegisterDisplayComponentCommand> commands =
                    List.of(ContentPageCommandFixtures.registerDisplayComponentCommandWithId(10L));

            // when
            List<DisplayComponent> result =
                    sut.createComponentsForUpdate(contentPageId, commands, FIXED_NOW);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).contentPageId()).isEqualTo(contentPageId);
        }
    }

    @Nested
    @DisplayName("createUpdateData() - ContentPageUpdateData 생성")
    class CreateUpdateDataTest {

        @Test
        @DisplayName("수정 커맨드와 시각으로 ContentPageUpdateData를 생성한다")
        void createUpdateData_ValidCommandAndInstant_ReturnsUpdateData() {
            // given
            UpdateContentPageCommand command = ContentPageCommandFixtures.updateCommand(1L);

            // when
            ContentPageUpdateData updateData = sut.createUpdateData(command, FIXED_NOW);

            // then
            assertThat(updateData).isNotNull();
            assertThat(updateData.title()).isEqualTo(command.title());
            assertThat(updateData.memo()).isEqualTo(command.memo());
            assertThat(updateData.imageUrl()).isEqualTo(command.imageUrl());
            assertThat(updateData.active()).isEqualTo(command.active());
            assertThat(updateData.updatedAt()).isEqualTo(FIXED_NOW);
        }
    }
}
