package com.ryuqq.setof.application.navigation.factory;

import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSearchCriteria;
import com.ryuqq.setof.domain.navigation.query.NavigationMenuSortKey;
import org.springframework.stereotype.Component;

/**
 * NavigationMenuQueryFactory - 네비게이션 메뉴 검색 조건 변환 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NavigationMenuQueryFactory {

    /**
     * SearchParams → SearchCriteria 변환.
     *
     * @param params 외부 검색 파라미터
     * @return 도메인 검색 조건
     */
    public NavigationMenuSearchCriteria create(NavigationMenuSearchParams params) {
        QueryContext<NavigationMenuSortKey> queryContext =
                QueryContext.of(
                        NavigationMenuSortKey.defaultKey(),
                        SortDirection.ASC,
                        PageRequest.of(0, PageRequest.UNPAGED_SIZE));

        return NavigationMenuSearchCriteria.of(
                params.displayPeriodStart(), params.displayPeriodEnd(), queryContext);
    }
}
