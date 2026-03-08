package com.ryuqq.setof.application.navigation.service;

import com.ryuqq.setof.application.navigation.manager.NavigationMenuQueryManager;
import com.ryuqq.setof.application.navigation.port.in.NavigationMenuQueryUseCase;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * NavigationMenuQueryService - 네비게이션 메뉴 조회 Service.
 *
 * <p>UseCase를 구현하며 Manager에 위임한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class NavigationMenuQueryService implements NavigationMenuQueryUseCase {

    private final NavigationMenuQueryManager queryManager;

    public NavigationMenuQueryService(NavigationMenuQueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @Override
    public List<NavigationMenu> fetchNavigationMenus() {
        return queryManager.fetchNavigationMenus();
    }
}
