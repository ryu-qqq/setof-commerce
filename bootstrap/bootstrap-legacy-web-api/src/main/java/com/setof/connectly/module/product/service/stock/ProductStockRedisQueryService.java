package com.setof.connectly.module.product.service.stock;

import com.setof.connectly.module.product.dto.stock.StockDto;

public interface ProductStockRedisQueryService {
    void saveStockInCache(StockDto stockDto);
}
