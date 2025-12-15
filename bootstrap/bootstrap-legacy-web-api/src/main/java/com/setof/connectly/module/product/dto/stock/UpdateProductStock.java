package com.setof.connectly.module.product.dto.stock;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UpdateProductStock implements Sku {

    protected long productId;
    protected int productStockQuantity;
}
