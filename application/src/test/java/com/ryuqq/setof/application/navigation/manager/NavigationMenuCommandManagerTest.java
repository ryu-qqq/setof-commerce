package com.ryuqq.setof.application.navigation.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.navigation.port.out.command.NavigationMenuCommandPort;
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
@DisplayName("NavigationMenuCommandManager 단위 테스트")
class NavigationMenuCommandManagerTest {

    @InjectMocks private NavigationMenuCommandManager sut;

    @Mock private NavigationMenuCommandPort commandPort;

    @Nested
    @DisplayName("persist() - 네비게이션 메뉴 저장")
    class PersistTest {

        @Test
        @DisplayName("commandPort.persist()를 호출하고 ID를 반환한다")
        void persist_CallsCommandPort_ReturnsId() {
            // given
            Long expectedId = 1L;
            NavigationMenu navigationMenu = NavigationFixtures.newNavigationMenu();

            given(commandPort.persist(navigationMenu)).willReturn(expectedId);

            // when
            Long result = sut.persist(navigationMenu);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(navigationMenu);
        }
    }
}
