package com.setof.connectly.module.product.service.stock.fetch;

import com.setof.connectly.module.product.dto.stock.StockDto;
import com.setof.connectly.module.product.repository.stock.ProductStockFindRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class StockFindServiceImpl implements StockFindService {

    private final ProductStockFindRepository productStockFindRepository;

    @Override
    public List<StockDto> fetchStocks(List<Long> productIds) {
        return productStockFindRepository.fetchStocks(productIds);
    }
}
