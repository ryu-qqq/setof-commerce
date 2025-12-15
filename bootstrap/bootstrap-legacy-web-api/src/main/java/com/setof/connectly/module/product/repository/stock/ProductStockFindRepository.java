package com.setof.connectly.module.product.repository.stock;

import com.setof.connectly.module.product.dto.stock.StockDto;
import java.util.List;
import java.util.Optional;

public interface ProductStockFindRepository {

    Optional<StockDto> fetchStock(long productId);

    List<StockDto> fetchStocks(List<Long> productIds);
}
