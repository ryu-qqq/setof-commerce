package com.ryuqq.setof.application.product.service.query;

import com.ryuqq.setof.application.product.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.product.dto.query.ProductGroupSearchQuery;
import com.ryuqq.setof.application.product.dto.response.ProductGroupResponse;
import com.ryuqq.setof.application.product.dto.response.ProductGroupSummaryResponse;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.manager.query.ProductSkuReadManager;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 상품그룹 조회 서비스
 *
 * <p>상품그룹 단건 및 목록 조회를 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ProductGroupQueryService implements GetProductGroupUseCase, GetProductGroupsUseCase {

    private final ProductGroupReadManager productGroupReadManager;
    private final ProductSkuReadManager productSkuReadManager;
    private final ProductGroupAssembler productGroupAssembler;

    public ProductGroupQueryService(
            ProductGroupReadManager productGroupReadManager,
            ProductSkuReadManager productSkuReadManager,
            ProductGroupAssembler productGroupAssembler) {
        this.productGroupReadManager = productGroupReadManager;
        this.productSkuReadManager = productSkuReadManager;
        this.productGroupAssembler = productGroupAssembler;
    }

    @Override
    public ProductGroupResponse execute(Long productGroupId) {
        ProductGroup productGroup = productGroupReadManager.findById(productGroupId);
        List<Product> products = productSkuReadManager.findByProductGroupId(productGroupId);
        return productGroupAssembler.toProductGroupResponse(productGroup, products);
    }

    @Override
    public List<ProductGroupSummaryResponse> execute(ProductGroupSearchQuery query) {
        List<ProductGroup> productGroups =
                productGroupReadManager.findByConditions(
                        query.sellerId(),
                        query.categoryId(),
                        query.brandId(),
                        query.name(),
                        query.status(),
                        query.offset(),
                        query.size());

        return productGroupAssembler.toProductGroupSummaryResponses(productGroups);
    }

    @Override
    public long count(ProductGroupSearchQuery query) {
        return productGroupReadManager.countByConditions(
                query.sellerId(),
                query.categoryId(),
                query.brandId(),
                query.name(),
                query.status());
    }
}
