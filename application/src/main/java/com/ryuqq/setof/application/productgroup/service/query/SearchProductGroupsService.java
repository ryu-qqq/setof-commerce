package com.ryuqq.setof.application.productgroup.service.query;

import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupQueryFactory;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.application.productgroup.port.in.query.SearchProductGroupsUseCase;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import org.springframework.stereotype.Service;

/**
 * SearchProductGroupsService - 키워드 기반 상품그룹 검색 Service.
 *
 * <p>GET /api/v1/search (MySQL ngram FULLTEXT 검색 + 커서 페이징)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class SearchProductGroupsService implements SearchProductGroupsUseCase {

    private final ProductGroupReadFacade readFacade;
    private final ProductGroupQueryFactory queryFactory;
    private final ProductGroupAssembler assembler;

    public SearchProductGroupsService(
            ProductGroupReadFacade readFacade,
            ProductGroupQueryFactory queryFactory,
            ProductGroupAssembler assembler) {
        this.readFacade = readFacade;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public ProductGroupSliceResult execute(ProductGroupSearchParams params) {
        ProductGroupSearchCriteria criteria = queryFactory.createCriteria(params);
        ProductGroupListBundle bundle = readFacade.getSearchBundle(criteria);
        return assembler.toSliceResult(bundle, params.size());
    }
}
