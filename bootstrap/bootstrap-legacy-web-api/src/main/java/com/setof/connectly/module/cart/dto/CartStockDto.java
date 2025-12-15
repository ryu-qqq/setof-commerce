package com.setof.connectly.module.cart.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartStockDto {
    private long cartId;
    private long productId;
    private int stockQuantity;

    @QueryProjection
    public CartStockDto(long cartId, long productId, int stockQuantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
    }

    @QueryProjection
    public CartStockDto(long productId, int stockQuantity) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
    }
}
