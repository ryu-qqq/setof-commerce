package com.ryuqq.setof.application.navigation.service.command;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.factory.NavigationMenuCommandFactory;
import com.ryuqq.setof.application.navigation.manager.NavigationMenuCommandManager;
import com.ryuqq.setof.application.navigation.port.in.command.UpdateNavigationMenuUseCase;
import com.ryuqq.setof.application.navigation.validator.NavigationMenuValidator;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenuUpdateData;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import org.springframework.stereotype.Service;

/**
 * UpdateNavigationMenuService - 네비게이션 메뉴 수정 Service.
 *
 * <p>APP-TIM-001: TimeProvider 직접 사용 금지 - Factory에서 처리합니다.
 *
 * <p>APP-VAL-001: 검증 + Domain 조회는 Validator.findExistingOrThrow()로 처리합니다.
 *
 * <p>비즈니스 로직:
 *
 * <ul>
 *   <li>Factory를 통해 UpdateContext 생성
 *   <li>Validator를 통해 대상 메뉴 존재 여부 확인 및 Domain 객체 조회
 *   <li>Domain 객체의 update() 호출로 상태 변경
 *   <li>Manager를 통해 영속화
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateNavigationMenuService implements UpdateNavigationMenuUseCase {

    private final NavigationMenuCommandFactory commandFactory;
    private final NavigationMenuCommandManager commandManager;
    private final NavigationMenuValidator validator;

    public UpdateNavigationMenuService(
            NavigationMenuCommandFactory commandFactory,
            NavigationMenuCommandManager commandManager,
            NavigationMenuValidator validator) {
        this.commandFactory = commandFactory;
        this.commandManager = commandManager;
        this.validator = validator;
    }

    @Override
    public void execute(UpdateNavigationMenuCommand command) {
        UpdateContext<NavigationMenuId, NavigationMenuUpdateData> context =
                commandFactory.createUpdateContext(command);

        NavigationMenu navigationMenu = validator.findExistingOrThrow(context.id());
        navigationMenu.update(context.updateData());

        commandManager.persist(navigationMenu);
    }
}
