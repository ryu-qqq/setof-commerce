package com.setof.connectly.module.product.dto.stock;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockDto extends UpdateProductStock {
    private String productGroupName;
    private long crawlProductSku;

    public StockDto(long productId, int productStockQuantity) {
        this.productId = productId;
        this.productStockQuantity = productStockQuantity;
    }

    @QueryProjection
    public StockDto(
            long productId, int stockQuantity, String productGroupName, long crawlProductSku) {
        super(productId, stockQuantity);
        this.productGroupName = productGroupName;
        this.crawlProductSku = crawlProductSku;
    }
}
