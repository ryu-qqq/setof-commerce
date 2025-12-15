package com.ryuqq.setof.application.productstock.assembler;

import com.ryuqq.setof.application.productstock.dto.command.InitializeStockCommand;
import com.ryuqq.setof.application.productstock.dto.response.ProductStockResponse;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import com.ryuqq.setof.domain.productstock.vo.StockQuantity;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductStock Assembler
 *
 * <p>Command DTO와 Domain 객체, Response DTO 간 변환을 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductStockAssembler {

    /**
     * InitializeStockCommand를 ProductStock 도메인으로 변환
     *
     * @param command 초기화 커맨드
     * @param now 현재 시각
     * @return ProductStock 도메인 객체
     */
    public ProductStock toDomain(InitializeStockCommand command, Instant now) {
        ProductId productId = ProductId.of(command.productId());
        StockQuantity quantity = StockQuantity.of(command.initialQuantity());
        return ProductStock.create(productId, quantity, now);
    }

    /**
     * ProductStock 도메인을 ProductStockResponse로 변환
     *
     * @param productStock ProductStock 도메인 객체
     * @return ProductStockResponse
     */
    public ProductStockResponse toResponse(ProductStock productStock) {
        return ProductStockResponse.of(
                productStock.getIdValue(),
                productStock.getProductIdValue(),
                productStock.getQuantityValue(),
                productStock.getUpdatedAt());
    }

    /**
     * ProductStock 도메인 목록을 ProductStockResponse 목록으로 변환
     *
     * @param productStocks ProductStock 도메인 목록
     * @return ProductStockResponse 목록
     */
    public List<ProductStockResponse> toResponses(List<ProductStock> productStocks) {
        return productStocks.stream().map(this::toResponse).toList();
    }
}
