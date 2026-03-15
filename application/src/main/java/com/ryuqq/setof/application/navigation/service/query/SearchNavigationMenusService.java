package com.ryuqq.setof.application.navigation.service.query;

import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.application.navigation.factory.NavigationMenuQueryFactory;
import com.ryuqq.setof.application.navigation.manager.NavigationMenuReadManager;
import com.ryuqq.setof.application.navigation.port.in.query.SearchNavigationMenusUseCase;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * SearchNavigationMenusService - 네비게이션 메뉴 목록 검색 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class SearchNavigationMenusService implements SearchNavigationMenusUseCase {

    private final NavigationMenuReadManager readManager;
    private final NavigationMenuQueryFactory queryFactory;

    public SearchNavigationMenusService(
            NavigationMenuReadManager readManager, NavigationMenuQueryFactory queryFactory) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
    }

    @Override
    public List<NavigationMenu> execute(NavigationMenuSearchParams params) {
        NavigationMenuSearchCriteria criteria = queryFactory.create(params);
        return readManager.findByCriteria(criteria);
    }
}
