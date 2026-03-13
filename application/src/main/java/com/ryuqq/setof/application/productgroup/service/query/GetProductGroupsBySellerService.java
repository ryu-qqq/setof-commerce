package com.ryuqq.setof.application.productgroup.service.query;

import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.application.productgroup.port.in.query.GetProductGroupsBySellerUseCase;
import org.springframework.stereotype.Service;

/**
 * GetProductGroupsBySellerService - 셀러별 상품그룹 조회 Service.
 *
 * <p>GET /api/v1/product/group/seller/{sellerId}
 *
 * <p>Redis 캐시 없이 DB 직접 조회합니다. (score DESC 정렬)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetProductGroupsBySellerService implements GetProductGroupsBySellerUseCase {

    private final ProductGroupReadFacade readFacade;
    private final ProductGroupAssembler assembler;

    public GetProductGroupsBySellerService(
            ProductGroupReadFacade readFacade, ProductGroupAssembler assembler) {
        this.readFacade = readFacade;
        this.assembler = assembler;
    }

    @Override
    public ProductGroupSliceResult execute(Long sellerId, int pageSize) {
        ProductGroupListBundle bundle = readFacade.getListBundleBySeller(sellerId, pageSize);
        return assembler.toSliceResult(bundle, pageSize);
    }
}
