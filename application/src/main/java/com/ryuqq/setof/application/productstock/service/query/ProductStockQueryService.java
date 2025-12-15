package com.ryuqq.setof.application.productstock.service.query;

import com.ryuqq.setof.application.productstock.assembler.ProductStockAssembler;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import com.ryuqq.setof.application.productstock.manager.query.ProductStockReadManager;
import com.ryuqq.setof.application.productstock.port.in.query.GetProductStockUseCase;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 재고 조회 서비스
 *
 * <p>상품의 재고 정보를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ProductStockQueryService implements GetProductStockUseCase {

    private final ProductStockReadManager productStockReadManager;
    private final ProductStockAssembler productStockAssembler;

    public ProductStockQueryService(
            ProductStockReadManager productStockReadManager,
            ProductStockAssembler productStockAssembler) {
        this.productStockReadManager = productStockReadManager;
        this.productStockAssembler = productStockAssembler;
    }

    @Override
    public ProductStockResponse execute(Long productId) {
        ProductStock productStock = productStockReadManager.findByProductId(productId);
        return productStockAssembler.toResponse(productStock);
    }

    @Override
    public List<ProductStockResponse> execute(List<Long> productIds) {
        List<ProductStock> productStocks = productStockReadManager.findByProductIds(productIds);
        return productStockAssembler.toResponses(productStocks);
    }
}
