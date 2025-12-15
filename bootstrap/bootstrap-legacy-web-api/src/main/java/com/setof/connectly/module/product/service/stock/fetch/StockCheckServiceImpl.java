package com.setof.connectly.module.product.service.stock.fetch;

import com.setof.connectly.module.exception.payment.ExceedStockException;
import com.setof.connectly.module.exception.product.ProductNotFoundException;
import com.setof.connectly.module.product.dto.stock.Sku;
import com.setof.connectly.module.product.dto.stock.StockDto;
import com.setof.connectly.module.product.dto.stock.UpdateProductStock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class StockCheckServiceImpl implements StockCheckService {

    private final StockFindService stockFindService;
    private final RealTimeStockCheckService realTimeStockCheckService;

    @Override
    public void checkEnoughStocks(List<? extends Sku> skus) {
        Map<Long, StockDto> stockMap =
                skus.stream()
                        .map(s -> new StockDto(s.getProductId(), s.getProductStockQuantity()))
                        .collect(
                                Collectors.toMap(
                                        UpdateProductStock::getProductId, Function.identity()));

        List<StockDto> findStocks =
                stockFindService.fetchStocks(new ArrayList<>(stockMap.keySet()));

        findStocks.forEach(
                findStock -> {
                    StockDto mapStockDto = stockMap.get(findStock.getProductId());
                    if (mapStockDto == null) throw new ProductNotFoundException();
                    stockCheckProcess(mapStockDto, findStock);
                });
    }

    private void stockCheckProcess(StockDto stockDto, StockDto findStockDto) {
        int requestQuantity = stockDto.getProductStockQuantity();
        if (findStockDto.getProductStockQuantity() < requestQuantity)
            throw new ExceedStockException(
                    findStockDto.getProductGroupName(), findStockDto.getProductStockQuantity());
        // if(findStockDto.getCrawlProductSku() > 0)
        // realTimeStockCheckService.isEnoughProduct(findStockDto, stockDto.getProductId(),
        // stockDto.getProductStockQuantity());
    }
}
