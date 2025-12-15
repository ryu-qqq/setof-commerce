package com.setof.connectly.module.product.service.stock.fetch;

import com.setof.connectly.module.product.dto.stock.StockDto;

public interface RealTimeStockCheckService {
    void isEnoughProduct(StockDto findStock, long productId, int qty);
}
