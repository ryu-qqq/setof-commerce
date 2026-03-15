package com.ryuqq.setof.application.navigation.manager;

import com.ryuqq.setof.application.navigation.port.out.NavigationMenuQueryPort;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.exception.NavigationErrorCode;
import com.ryuqq.setof.domain.navigation.exception.NavigationException;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * NavigationMenuReadManager - 네비게이션 메뉴 조회 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NavigationMenuReadManager {

    private final NavigationMenuQueryPort queryPort;

    public NavigationMenuReadManager(NavigationMenuQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public NavigationMenu getById(long navigationMenuId) {
        return queryPort
                .findById(navigationMenuId)
                .orElseThrow(
                        () ->
                                new NavigationException(
                                        NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND,
                                        "네비게이션 메뉴를 찾을 수 없습니다. id=" + navigationMenuId));
    }

    @Transactional(readOnly = true)
    public List<NavigationMenu> findByCriteria(NavigationMenuSearchCriteria criteria) {
        return queryPort.findByCriteria(criteria);
    }
}
