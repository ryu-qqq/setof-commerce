package com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.navigation.NavigationMenuCommandApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command.RegisterNavigationMenuApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.command.UpdateNavigationMenuApiRequest;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * NavigationMenuCommandApiMapper 단위 테스트.
 *
 * <p>네비게이션 메뉴 Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("NavigationMenuCommandApiMapper 단위 테스트")
class NavigationMenuCommandApiMapperTest {

    private NavigationMenuCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NavigationMenuCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(RegisterNavigationMenuApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 RegisterNavigationMenuCommand로 변환한다")
        void toCommand_Register_Success() {
            // given
            RegisterNavigationMenuApiRequest request =
                    NavigationMenuCommandApiFixtures.registerRequest();

            // when
            RegisterNavigationMenuCommand command = mapper.toCommand(request);

            // then
            assertThat(command.title()).isEqualTo(request.title());
            assertThat(command.linkUrl()).isEqualTo(request.linkUrl());
            assertThat(command.displayOrder()).isEqualTo(request.displayOrder());
            assertThat(command.displayStartAt()).isEqualTo(request.displayStartAt());
            assertThat(command.displayEndAt()).isEqualTo(request.displayEndAt());
            assertThat(command.active()).isEqualTo(request.active());
        }

        @Test
        @DisplayName("active가 false인 경우도 올바르게 변환한다")
        void toCommand_Register_InactiveMenu() {
            // given
            RegisterNavigationMenuApiRequest request =
                    NavigationMenuCommandApiFixtures.registerRequestInactive();

            // when
            RegisterNavigationMenuCommand command = mapper.toCommand(request);

            // then
            assertThat(command.active()).isFalse();
        }

        @Test
        @DisplayName("커스텀 제목과 URL로 생성한 요청도 올바르게 변환한다")
        void toCommand_Register_CustomTitleAndUrl() {
            // given
            RegisterNavigationMenuApiRequest request =
                    NavigationMenuCommandApiFixtures.registerRequest("기획전", "/event-page");

            // when
            RegisterNavigationMenuCommand command = mapper.toCommand(request);

            // then
            assertThat(command.title()).isEqualTo("기획전");
            assertThat(command.linkUrl()).isEqualTo("/event-page");
        }
    }

    @Nested
    @DisplayName("toCommand(long, UpdateNavigationMenuApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 UpdateNavigationMenuCommand로 변환한다")
        void toCommand_Update_Success() {
            // given
            long navigationMenuId = NavigationMenuCommandApiFixtures.DEFAULT_NAVIGATION_MENU_ID;
            UpdateNavigationMenuApiRequest request =
                    NavigationMenuCommandApiFixtures.updateRequest();

            // when
            UpdateNavigationMenuCommand command = mapper.toCommand(navigationMenuId, request);

            // then
            assertThat(command.id()).isEqualTo(navigationMenuId);
            assertThat(command.title()).isEqualTo(request.title());
            assertThat(command.linkUrl()).isEqualTo(request.linkUrl());
            assertThat(command.displayOrder()).isEqualTo(request.displayOrder());
            assertThat(command.displayStartAt()).isEqualTo(request.displayStartAt());
            assertThat(command.displayEndAt()).isEqualTo(request.displayEndAt());
            assertThat(command.active()).isEqualTo(request.active());
        }

        @Test
        @DisplayName("PathVariable의 navigationMenuId를 Command에 올바르게 설정한다")
        void toCommand_Update_NavigationMenuIdFromPath() {
            // given
            long navigationMenuId = 999L;
            UpdateNavigationMenuApiRequest request =
                    NavigationMenuCommandApiFixtures.updateRequest();

            // when
            UpdateNavigationMenuCommand command = mapper.toCommand(navigationMenuId, request);

            // then
            assertThat(command.id()).isEqualTo(999L);
        }

        @Test
        @DisplayName("active가 true인 수정 요청도 올바르게 변환한다")
        void toCommand_Update_ActiveMenu() {
            // given
            long navigationMenuId = NavigationMenuCommandApiFixtures.DEFAULT_NAVIGATION_MENU_ID;
            UpdateNavigationMenuApiRequest request =
                    NavigationMenuCommandApiFixtures.updateRequestActive();

            // when
            UpdateNavigationMenuCommand command = mapper.toCommand(navigationMenuId, request);

            // then
            assertThat(command.active()).isTrue();
        }
    }

    @Nested
    @DisplayName("toCommand(long) - RemoveNavigationMenuCommand")
    class ToRemoveCommandTest {

        @Test
        @DisplayName("navigationMenuId를 RemoveNavigationMenuCommand로 변환한다")
        void toCommand_Remove_Success() {
            // given
            long navigationMenuId = NavigationMenuCommandApiFixtures.DEFAULT_NAVIGATION_MENU_ID;

            // when
            RemoveNavigationMenuCommand command = mapper.toCommand(navigationMenuId);

            // then
            assertThat(command.id()).isEqualTo(navigationMenuId);
        }

        @Test
        @DisplayName("다른 navigationMenuId도 올바르게 변환한다")
        void toCommand_Remove_DifferentId() {
            // given
            long navigationMenuId = 555L;

            // when
            RemoveNavigationMenuCommand command = mapper.toCommand(navigationMenuId);

            // then
            assertThat(command.id()).isEqualTo(555L);
        }
    }
}
