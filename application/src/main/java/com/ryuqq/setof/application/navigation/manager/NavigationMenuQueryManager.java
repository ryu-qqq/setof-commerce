package com.ryuqq.setof.application.navigation.manager;

import com.ryuqq.setof.application.navigation.port.out.NavigationMenuQueryPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * NavigationMenuQueryManager - 네비게이션 메뉴 조회 매니저.
 *
 * <p>Port를 통해 네비게이션 메뉴 목록을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NavigationMenuQueryManager {

    private final NavigationMenuQueryPort queryPort;

    public NavigationMenuQueryManager(NavigationMenuQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * 전시 중인 네비게이션 메뉴 목록 조회.
     *
     * @return NavigationMenu 목록
     */
    @Transactional(readOnly = true)
    public List<NavigationMenu> fetchNavigationMenus() {
        return queryPort.fetchNavigationMenus();
    }
}
