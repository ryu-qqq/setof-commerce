package com.ryuqq.setof.application.navigation.manager;

import com.ryuqq.setof.application.navigation.port.out.command.NavigationMenuCommandPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * NavigationMenuCommandManager - 네비게이션 메뉴 Command 매니저.
 *
 * <p>Command Port를 통해 네비게이션 메뉴의 영속화를 담당합니다.
 *
 * <p>APP-MGR-001: 트랜잭션 경계는 Manager에서 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NavigationMenuCommandManager {

    private final NavigationMenuCommandPort commandPort;

    public NavigationMenuCommandManager(NavigationMenuCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    /**
     * 네비게이션 메뉴를 저장합니다 (신규 등록 및 수정 공통).
     *
     * @param navigationMenu 저장할 NavigationMenu 도메인 객체
     * @return 저장된 네비게이션 메뉴 ID
     */
    @Transactional
    public Long persist(NavigationMenu navigationMenu) {
        return commandPort.persist(navigationMenu);
    }
}
