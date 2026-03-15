package com.ryuqq.setof.application.navigation.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.navigation.NavigationCommandFixtures;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.factory.NavigationMenuCommandFactory;
import com.ryuqq.setof.application.navigation.manager.NavigationMenuCommandManager;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.setof.commerce.domain.navigation.NavigationFixtures;
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
@DisplayName("RegisterNavigationMenuService 단위 테스트")
class RegisterNavigationMenuServiceTest {

    @InjectMocks private RegisterNavigationMenuService sut;

    @Mock private NavigationMenuCommandFactory commandFactory;
    @Mock private NavigationMenuCommandManager commandManager;

    @Nested
    @DisplayName("execute() - 네비게이션 메뉴 등록")
    class ExecuteTest {

        @Test
        @DisplayName("네비게이션 메뉴를 등록하고 ID를 반환한다")
        void execute_RegistersNavigationMenu_ReturnsId() {
            // given
            Long expectedId = 100L;
            RegisterNavigationMenuCommand command = NavigationCommandFixtures.registerCommand();
            NavigationMenu navigationMenu = NavigationFixtures.newNavigationMenu();

            given(commandFactory.create(command)).willReturn(navigationMenu);
            given(commandManager.persist(navigationMenu)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandFactory).should().create(command);
            then(commandManager).should().persist(navigationMenu);
        }
    }
}
