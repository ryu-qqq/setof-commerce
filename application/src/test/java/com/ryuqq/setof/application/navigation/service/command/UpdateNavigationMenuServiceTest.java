package com.ryuqq.setof.application.navigation.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.navigation.NavigationCommandFixtures;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.factory.NavigationMenuCommandFactory;
import com.ryuqq.setof.application.navigation.manager.NavigationMenuCommandManager;
import com.ryuqq.setof.application.navigation.validator.NavigationMenuValidator;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenuUpdateData;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import com.setof.commerce.domain.navigation.NavigationFixtures;
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
@DisplayName("UpdateNavigationMenuService 단위 테스트")
class UpdateNavigationMenuServiceTest {

    @InjectMocks private UpdateNavigationMenuService sut;

    @Mock private NavigationMenuCommandFactory commandFactory;
    @Mock private NavigationMenuCommandManager commandManager;
    @Mock private NavigationMenuValidator validator;

    @Nested
    @DisplayName("execute() - 네비게이션 메뉴 수정")
    class ExecuteTest {

        @Test
        @DisplayName("네비게이션 메뉴를 수정하고 영속화한다")
        void execute_UpdatesNavigationMenu_AndPersists() {
            // given
            long id = 1L;
            UpdateNavigationMenuCommand command = NavigationCommandFixtures.updateCommand(id);
            NavigationMenuId menuId = NavigationMenuId.of(id);
            NavigationMenuUpdateData updateData =
                    NavigationFixtures.defaultNavigationMenuUpdateData();
            Instant now = Instant.now();
            UpdateContext<NavigationMenuId, NavigationMenuUpdateData> context =
                    new UpdateContext<>(menuId, updateData, now);

            NavigationMenu navigationMenu = NavigationFixtures.activeNavigationMenu(id);

            given(commandFactory.createUpdateContext(command)).willReturn(context);
            given(validator.findExistingOrThrow(menuId)).willReturn(navigationMenu);

            // when
            sut.execute(command);

            // then
            then(commandFactory).should().createUpdateContext(command);
            then(validator).should().findExistingOrThrow(menuId);
            then(commandManager).should().persist(navigationMenu);
        }
    }
}
