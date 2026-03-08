package com.ryuqq.setof.application.productgroup.service.query;

import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.query.ProductGroupSearchParams;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupQueryFactory;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.application.productgroup.port.in.query.FetchProductGroupsUseCase;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import org.springframework.stereotype.Service;

/**
 * FetchProductGroupsService - 상품그룹 커서 페이징 목록 조회 Service.
 *
 * <p>GET /api/v1/products/group
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class FetchProductGroupsService implements FetchProductGroupsUseCase {

    private final ProductGroupReadFacade readFacade;
    private final ProductGroupQueryFactory queryFactory;
    private final ProductGroupAssembler assembler;

    public FetchProductGroupsService(
            ProductGroupReadFacade readFacade,
            ProductGroupQueryFactory queryFactory,
            ProductGroupAssembler assembler) {
        this.readFacade = readFacade;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public ProductGroupSliceResult execute(ProductGroupSearchParams params) {
        LegacyProductGroupSearchCondition condition = queryFactory.createCondition(params);
        ProductGroupListBundle bundle = readFacade.getListBundle(condition);
        return assembler.toSliceResult(bundle, params.size());
    }
}
