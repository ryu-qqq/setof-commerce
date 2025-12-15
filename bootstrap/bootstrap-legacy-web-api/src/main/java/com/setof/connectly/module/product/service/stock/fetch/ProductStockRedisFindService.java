package com.setof.connectly.module.product.service.stock.fetch;

import com.setof.connectly.module.product.dto.stock.StockDto;
import java.util.List;
import java.util.Optional;

public interface ProductStockRedisFindService {
    Optional<StockDto> fetchStockInRedis(long productId);

    List<StockDto> fetchStockInRedis(List<Long> productId);
}
