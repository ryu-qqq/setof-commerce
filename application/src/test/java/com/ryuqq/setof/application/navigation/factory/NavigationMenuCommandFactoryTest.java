package com.ryuqq.setof.application.navigation.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.navigation.NavigationCommandFixtures;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenuUpdateData;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
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
@DisplayName("NavigationMenuCommandFactory 단위 테스트")
class NavigationMenuCommandFactoryTest {

    @InjectMocks private NavigationMenuCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    @Nested
    @DisplayName("create() - RegisterNavigationMenuCommand → NavigationMenu 변환")
    class CreateTest {

        @Test
        @DisplayName("RegisterNavigationMenuCommand로부터 NavigationMenu를 생성한다")
        void create_CreatesNavigationMenu() {
            // given
            Instant now = Instant.now();
            RegisterNavigationMenuCommand command = NavigationCommandFixtures.registerCommand();

            given(timeProvider.now()).willReturn(now);

            // when
            NavigationMenu result = sut.create(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo(command.title());
            assertThat(result.linkUrl()).isEqualTo(command.linkUrl());
            assertThat(result.displayOrder()).isEqualTo(command.displayOrder());
            assertThat(result.isActive()).isEqualTo(command.active());
            assertThat(result.displayPeriod()).isNotNull();
            assertThat(result.createdAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("createUpdateContext() - UpdateNavigationMenuCommand → UpdateContext 변환")
    class CreateUpdateContextTest {

        @Test
        @DisplayName("UpdateNavigationMenuCommand로부터 UpdateContext를 생성한다")
        void createUpdateContext_CreatesUpdateContext() {
            // given
            long id = 100L;
            Instant now = Instant.now();
            UpdateNavigationMenuCommand command = NavigationCommandFixtures.updateCommand(id);

            given(timeProvider.now()).willReturn(now);

            // when
            UpdateContext<NavigationMenuId, NavigationMenuUpdateData> result =
                    sut.createUpdateContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id().value()).isEqualTo(id);
            assertThat(result.changedAt()).isEqualTo(now);
            assertThat(result.updateData()).isNotNull();
            assertThat(result.updateData().title()).isEqualTo(command.title());
            assertThat(result.updateData().linkUrl()).isEqualTo(command.linkUrl());
            assertThat(result.updateData().displayOrder()).isEqualTo(command.displayOrder());
            assertThat(result.updateData().active()).isEqualTo(command.active());
        }
    }

    @Nested
    @DisplayName("createRemoveContext() - RemoveNavigationMenuCommand → StatusChangeContext 변환")
    class CreateRemoveContextTest {

        @Test
        @DisplayName("RemoveNavigationMenuCommand로부터 StatusChangeContext를 생성한다")
        void createRemoveContext_CreatesStatusChangeContext() {
            // given
            long id = 100L;
            Instant now = Instant.now();
            RemoveNavigationMenuCommand command = NavigationCommandFixtures.removeCommand(id);

            given(timeProvider.now()).willReturn(now);

            // when
            StatusChangeContext<NavigationMenuId> result = sut.createRemoveContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id().value()).isEqualTo(id);
            assertThat(result.changedAt()).isEqualTo(now);
        }
    }
}
