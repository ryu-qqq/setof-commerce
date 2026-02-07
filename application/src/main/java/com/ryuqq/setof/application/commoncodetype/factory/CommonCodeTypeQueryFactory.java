package com.ryuqq.setof.application.commoncodetype.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.commoncodetype.dto.query.CommonCodeTypeSearchParams;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.commoncodetype.query.CommonCodeTypeSearchCriteria;
import com.ryuqq.setof.domain.commoncodetype.query.CommonCodeTypeSearchField;
import com.ryuqq.setof.domain.commoncodetype.query.CommonCodeTypeSortKey;
import org.springframework.stereotype.Component;

/**
 * CommonCodeTypeQueryFactory - 공통 코드 타입 Query Factory
 *
 * <p>SearchParams → SearchCriteria 변환을 담당합니다.
 *
 * @author ryu-qqq
 */
@Component
public class CommonCodeTypeQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public CommonCodeTypeQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    public CommonCodeTypeSearchCriteria createCriteria(CommonCodeTypeSearchParams params) {
        CommonCodeTypeSortKey sortKey = resolveSortKey(params.sortKey());
        SortDirection sortDirection = commonVoFactory.parseSortDirection(params.sortDirection());
        PageRequest pageRequest = commonVoFactory.createPageRequest(params.page(), params.size());

        QueryContext<CommonCodeTypeSortKey> queryContext =
                commonVoFactory.createQueryContext(
                        sortKey,
                        sortDirection,
                        pageRequest,
                        params.searchParams().includeDeleted());

        CommonCodeTypeSearchField searchField =
                CommonCodeTypeSearchField.fromString(params.searchField());
        return new CommonCodeTypeSearchCriteria(
                params.active(),
                searchField,
                params.searchWord(),
                params.type(),
                queryContext);
    }

    private CommonCodeTypeSortKey resolveSortKey(String sortKeyString) {
        if (sortKeyString == null || sortKeyString.isBlank()) {
            return CommonCodeTypeSortKey.defaultKey();
        }

        for (CommonCodeTypeSortKey key : CommonCodeTypeSortKey.values()) {
            if (key.fieldName().equalsIgnoreCase(sortKeyString)
                    || key.name().equalsIgnoreCase(sortKeyString)) {
                return key;
            }
        }

        return CommonCodeTypeSortKey.defaultKey();
    }
}
