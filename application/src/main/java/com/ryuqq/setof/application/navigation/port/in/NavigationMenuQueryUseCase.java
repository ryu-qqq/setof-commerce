package com.ryuqq.setof.application.navigation.port.in;

import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;

/**
 * NavigationMenuQueryUseCase - 네비게이션 메뉴 조회 UseCase.
 *
 * <p>전시 중인 네비게이션 메뉴 목록 조회를 위한 입력 포트 인터페이스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface NavigationMenuQueryUseCase {

    /**
     * 전시 중인 네비게이션 메뉴 목록 조회.
     *
     * @return NavigationMenu 목록
     */
    List<NavigationMenu> fetchNavigationMenus();
}
