package com.ryuqq.setof.application.navigation.service.command;

import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.factory.NavigationMenuCommandFactory;
import com.ryuqq.setof.application.navigation.manager.NavigationMenuCommandManager;
import com.ryuqq.setof.application.navigation.port.in.command.RegisterNavigationMenuUseCase;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import org.springframework.stereotype.Service;

/**
 * RegisterNavigationMenuService - 네비게이션 메뉴 등록 Service.
 *
 * <p>APP-TIM-001: TimeProvider 직접 사용 금지 - Factory에서 처리합니다.
 *
 * <p>비즈니스 로직:
 *
 * <ul>
 *   <li>Factory를 통해 RegisterNavigationMenuCommand를 NavigationMenu 도메인 객체로 변환
 *   <li>Manager를 통해 영속화하고 생성된 ID를 반환
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterNavigationMenuService implements RegisterNavigationMenuUseCase {

    private final NavigationMenuCommandFactory commandFactory;
    private final NavigationMenuCommandManager commandManager;

    public RegisterNavigationMenuService(
            NavigationMenuCommandFactory commandFactory,
            NavigationMenuCommandManager commandManager) {
        this.commandFactory = commandFactory;
        this.commandManager = commandManager;
    }

    @Override
    public Long execute(RegisterNavigationMenuCommand command) {
        NavigationMenu navigationMenu = commandFactory.create(command);
        return commandManager.persist(navigationMenu);
    }
}
