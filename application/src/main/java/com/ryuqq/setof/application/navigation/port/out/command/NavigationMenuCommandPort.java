package com.ryuqq.setof.application.navigation.port.out.command;

import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;

/**
 * NavigationMenuCommandPort - 네비게이션 메뉴 Command 출력 포트.
 *
 * <p>Persistence Adapter가 구현하는 Command 출력 포트 인터페이스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface NavigationMenuCommandPort {

    /**
     * 네비게이션 메뉴를 저장합니다 (신규 등록 및 수정 공통).
     *
     * @param navigationMenu 저장할 NavigationMenu 도메인 객체
     * @return 저장된 네비게이션 메뉴 ID
     */
    Long persist(NavigationMenu navigationMenu);
}
