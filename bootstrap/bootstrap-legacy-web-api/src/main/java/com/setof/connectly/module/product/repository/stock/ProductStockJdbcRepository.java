package com.setof.connectly.module.product.repository.stock;

import com.setof.connectly.module.product.dto.stock.StockDto;
import java.util.List;

public interface ProductStockJdbcRepository {
    void subtractStocks(List<StockDto> stocks);

    void addStocks(List<StockDto> stocks);
}
