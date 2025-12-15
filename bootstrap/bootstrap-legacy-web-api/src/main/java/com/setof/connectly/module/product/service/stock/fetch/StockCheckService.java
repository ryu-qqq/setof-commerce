package com.setof.connectly.module.product.service.stock.fetch;

import com.setof.connectly.module.product.dto.stock.Sku;
import java.util.List;

public interface StockCheckService {
    void checkEnoughStocks(List<? extends Sku> skus);
}
