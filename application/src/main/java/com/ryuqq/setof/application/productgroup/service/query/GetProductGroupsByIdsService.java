package com.ryuqq.setof.application.productgroup.service.query;

import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.application.productgroup.port.in.query.GetProductGroupsByIdsUseCase;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetProductGroupsByIdsService - ID 목록 기반 상품그룹 조회 Service.
 *
 * <p>GET /api/v1/products/group/recent (찜 목록 등 ID 목록 기반 조회)
 *
 * <p>요청 ID 순서로 재정렬된 결과를 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetProductGroupsByIdsService implements GetProductGroupsByIdsUseCase {

    private final ProductGroupReadFacade readFacade;
    private final ProductGroupAssembler assembler;

    public GetProductGroupsByIdsService(
            ProductGroupReadFacade readFacade, ProductGroupAssembler assembler) {
        this.readFacade = readFacade;
        this.assembler = assembler;
    }

    @Override
    public ProductGroupSliceResult execute(List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return ProductGroupSliceResult.empty(0);
        }

        ProductGroupListBundle bundle = readFacade.getListBundleByIds(productGroupIds);
        return assembler.toSliceResult(bundle, productGroupIds.size());
    }
}
