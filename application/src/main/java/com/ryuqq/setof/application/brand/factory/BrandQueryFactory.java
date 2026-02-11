package com.ryuqq.setof.application.brand.factory;

import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSearchField;
import com.ryuqq.setof.domain.brand.query.BrandSortKey;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.springframework.stereotype.Component;

/**
 * Brand Query Factory.
 *
 * <p>Query DTO를 Domain Criteria로 변환합니다.
 *
 * <p>기본값 처리는 REST API Layer (ApiMapper)에서 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public BrandQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    /**
     * BrandSearchParams로부터 BrandSearchCriteria 생성.
     *
     * <p>기본값 처리 없이 단순 변환만 수행합니다.
     *
     * @param params 검색 파라미터 (기본값 적용 완료된 상태)
     * @return BrandSearchCriteria
     */
    public BrandSearchCriteria createCriteria(BrandSearchParams params) {
        BrandSortKey sortKey = resolveSortKey(params.sortKey());
        SortDirection sortDirection = commonVoFactory.parseSortDirection(params.sortDirection());
        PageRequest pageRequest = commonVoFactory.createPageRequest(params.page(), params.size());

        QueryContext<BrandSortKey> queryContext =
                commonVoFactory.createQueryContext(
                        sortKey,
                        sortDirection,
                        pageRequest,
                        params.searchParams().includeDeleted());

        return BrandSearchCriteria.of(
                null,
                BrandSearchField.fromString(params.searchField()),
                params.searchWord(),
                queryContext);
    }

    /**
     * BrandDisplaySearchParams로부터 BrandSearchCriteria 생성.
     *
     * <p>Display API용 간단한 검색 조건입니다.
     *
     * @param params 검색 파라미터
     * @return BrandSearchCriteria
     */
    public BrandSearchCriteria createDisplayCriteria(BrandDisplaySearchParams params) {
        Boolean displayed = params.displayed() != null ? params.displayed() : true;
        QueryContext<BrandSortKey> queryContext = QueryContext.defaultOf(BrandSortKey.defaultKey());
        return BrandSearchCriteria.of(
                displayed,
                BrandSearchField.fromString(params.searchField()),
                params.searchWord(),
                queryContext);
    }

    private BrandSortKey resolveSortKey(String sortKeyString) {
        if (sortKeyString == null || sortKeyString.isBlank()) {
            return BrandSortKey.defaultKey();
        }

        for (BrandSortKey key : BrandSortKey.values()) {
            if (key.fieldName().equalsIgnoreCase(sortKeyString)
                    || key.name().equalsIgnoreCase(sortKeyString)) {
                return key;
            }
        }

        return BrandSortKey.defaultKey();
    }
}
