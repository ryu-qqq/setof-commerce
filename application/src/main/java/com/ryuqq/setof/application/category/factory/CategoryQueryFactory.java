package com.ryuqq.setof.application.category.factory;

import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.query.CategorySearchField;
import com.ryuqq.setof.domain.category.query.CategorySortKey;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.springframework.stereotype.Component;

/**
 * Category Query Factory.
 *
 * <p>Query DTO를 Domain Criteria로 변환합니다.
 *
 * <p>기본값 처리는 REST API Layer (ApiMapper)에서 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CategoryQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public CategoryQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    /**
     * CategorySearchParams로부터 CategorySearchCriteria 생성.
     *
     * <p>기본값 처리 없이 단순 변환만 수행합니다.
     *
     * @param params 검색 파라미터 (기본값 적용 완료된 상태)
     * @return CategorySearchCriteria
     */
    public CategorySearchCriteria createCriteria(CategorySearchParams params) {
        CategorySortKey sortKey = resolveSortKey(params.sortKey());
        SortDirection sortDirection = commonVoFactory.parseSortDirection(params.sortDirection());
        PageRequest pageRequest = commonVoFactory.createPageRequest(params.page(), params.size());

        QueryContext<CategorySortKey> queryContext =
                commonVoFactory.createQueryContext(
                        sortKey,
                        sortDirection,
                        pageRequest,
                        params.searchParams().includeDeleted());

        CategorySearchField searchField = CategorySearchField.fromString(params.searchField());

        return CategorySearchCriteria.of(
                null,
                params.displayed(),
                null,
                null,
                searchField,
                params.searchWord(),
                queryContext);
    }

    private CategorySortKey resolveSortKey(String sortKeyString) {
        if (sortKeyString == null || sortKeyString.isBlank()) {
            return CategorySortKey.defaultKey();
        }

        for (CategorySortKey key : CategorySortKey.values()) {
            if (key.fieldName().equalsIgnoreCase(sortKeyString)
                    || key.name().equalsIgnoreCase(sortKeyString)) {
                return key;
            }
        }

        return CategorySortKey.defaultKey();
    }
}
