package com.setof.connectly.module.product.service.stock.fetch;

import com.setof.connectly.module.product.dto.stock.StockDto;
import java.util.List;

public interface StockFindService {

    List<StockDto> fetchStocks(List<Long> productIds);
}
