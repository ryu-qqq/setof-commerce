package com.ryuqq.setof.application.navigation.service.command;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.factory.NavigationMenuCommandFactory;
import com.ryuqq.setof.application.navigation.manager.NavigationMenuCommandManager;
import com.ryuqq.setof.application.navigation.port.in.command.RemoveNavigationMenuUseCase;
import com.ryuqq.setof.application.navigation.validator.NavigationMenuValidator;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import org.springframework.stereotype.Service;

/**
 * RemoveNavigationMenuService - 네비게이션 메뉴 삭제 Service.
 *
 * <p>APP-TIM-001: TimeProvider 직접 사용 금지 - Factory에서 처리합니다.
 *
 * <p>APP-FAC-001: 상태 변경은 StatusChangeContext를 사용합니다.
 *
 * <p>APP-VAL-001: 검증 + Domain 조회는 Validator.findExistingOrThrow()로 처리합니다.
 *
 * <p>비즈니스 로직:
 *
 * <ul>
 *   <li>Factory를 통해 StatusChangeContext 생성
 *   <li>Validator를 통해 대상 메뉴 존재 여부 확인 및 Domain 객체 조회
 *   <li>Domain 객체의 remove() 호출로 논리 삭제 처리
 *   <li>Manager를 통해 영속화
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RemoveNavigationMenuService implements RemoveNavigationMenuUseCase {

    private final NavigationMenuCommandFactory commandFactory;
    private final NavigationMenuCommandManager commandManager;
    private final NavigationMenuValidator validator;

    public RemoveNavigationMenuService(
            NavigationMenuCommandFactory commandFactory,
            NavigationMenuCommandManager commandManager,
            NavigationMenuValidator validator) {
        this.commandFactory = commandFactory;
        this.commandManager = commandManager;
        this.validator = validator;
    }

    @Override
    public void execute(RemoveNavigationMenuCommand command) {
        StatusChangeContext<NavigationMenuId> context = commandFactory.createRemoveContext(command);

        NavigationMenu navigationMenu = validator.findExistingOrThrow(context.id());
        navigationMenu.remove(context.changedAt());

        commandManager.persist(navigationMenu);
    }
}
