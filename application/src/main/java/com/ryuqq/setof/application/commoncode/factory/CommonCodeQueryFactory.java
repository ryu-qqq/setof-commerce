package com.ryuqq.setof.application.commoncode.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.commoncode.dto.query.CommonCodeSearchParams;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.commoncode.query.CommonCodeSearchCriteria;
import com.ryuqq.setof.domain.commoncode.query.CommonCodeSortKey;
import com.ryuqq.setof.domain.commoncodetype.id.CommonCodeTypeId;
import org.springframework.stereotype.Component;

/**
 * CommonCodeQueryFactory - 공통 코드 Query Factory.
 *
 * <p>SearchParams → SearchCriteria 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CommonCodeQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public CommonCodeQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    /**
     * SearchParams → SearchCriteria 변환.
     *
     * @param params 검색 파라미터
     * @return CommonCodeSearchCriteria
     */
    public CommonCodeSearchCriteria createCriteria(CommonCodeSearchParams params) {
        CommonCodeSortKey sortKey = resolveSortKey(params.sortKey());
        SortDirection sortDirection = commonVoFactory.parseSortDirection(params.sortDirection());
        PageRequest pageRequest = commonVoFactory.createPageRequest(params.page(), params.size());

        QueryContext<CommonCodeSortKey> queryContext =
                commonVoFactory.createQueryContext(
                        sortKey,
                        sortDirection,
                        pageRequest,
                        params.searchParams().includeDeleted());

        return new CommonCodeSearchCriteria(
                CommonCodeTypeId.of(params.commonCodeTypeId()),
                params.active(),
                params.code(),
                queryContext);
    }

    private CommonCodeSortKey resolveSortKey(String sortKeyString) {
        if (sortKeyString == null || sortKeyString.isBlank()) {
            return CommonCodeSortKey.defaultKey();
        }

        for (CommonCodeSortKey key : CommonCodeSortKey.values()) {
            if (key.fieldName().equalsIgnoreCase(sortKeyString)
                    || key.name().equalsIgnoreCase(sortKeyString)) {
                return key;
            }
        }

        return CommonCodeSortKey.defaultKey();
    }
}
