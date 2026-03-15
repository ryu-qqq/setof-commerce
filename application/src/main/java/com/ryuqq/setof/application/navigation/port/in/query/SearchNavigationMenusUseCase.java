package com.ryuqq.setof.application.navigation.port.in.query;

import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.util.List;

/**
 * SearchNavigationMenusUseCase - 네비게이션 메뉴 목록 검색 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface SearchNavigationMenusUseCase {

    /**
     * 검색 조건으로 네비게이션 메뉴 목록을 조회합니다.
     *
     * @param params 검색 파라미터
     * @return 네비게이션 메뉴 목록
     */
    List<NavigationMenu> execute(NavigationMenuSearchParams params);
}
