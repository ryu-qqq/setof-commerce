package com.ryuqq.setof.application.navigation.port.out;

import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;

/**
 * NavigationMenuQueryPort - 네비게이션 메뉴 조회 출력 포트.
 *
 * <p>Persistence Adapter가 구현하는 출력 포트 인터페이스. Adapter는 레거시 DB를 조회하여 NavigationMenu 도메인 객체로 변환 후 반환한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface NavigationMenuQueryPort {

    /**
     * 전시 중인 네비게이션 메뉴 목록 조회.
     *
     * @return NavigationMenu 목록
     */
    List<NavigationMenu> fetchNavigationMenus();
}
