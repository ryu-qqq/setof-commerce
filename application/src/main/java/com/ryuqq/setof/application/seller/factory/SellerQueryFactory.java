package com.ryuqq.setof.application.seller.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerSearchField;
import com.ryuqq.setof.domain.seller.query.SellerSortKey;
import org.springframework.stereotype.Component;

@Component
public class SellerQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public SellerQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    public SellerSearchCriteria createCriteria(SellerSearchParams params) {
        SellerSortKey sortKey = resolveSortKey(params.sortKey());
        SortDirection sortDirection = commonVoFactory.parseSortDirection(params.sortDirection());
        PageRequest pageRequest = commonVoFactory.createPageRequest(params.page(), params.size());

        QueryContext<SellerSortKey> queryContext =
                commonVoFactory.createQueryContext(
                        sortKey,
                        sortDirection,
                        pageRequest,
                        params.searchParams().includeDeleted());

        SellerSearchField searchField = SellerSearchField.fromString(params.searchField());
        return SellerSearchCriteria.of(
                params.active(), searchField, params.searchWord(), queryContext);
    }

    private SellerSortKey resolveSortKey(String sortKeyString) {
        if (sortKeyString == null || sortKeyString.isBlank()) {
            return SellerSortKey.defaultKey();
        }
        for (SellerSortKey key : SellerSortKey.values()) {
            if (key.fieldName().equalsIgnoreCase(sortKeyString)
                    || key.name().equalsIgnoreCase(sortKeyString)) {
                return key;
            }
        }
        return SellerSortKey.defaultKey();
    }
}
