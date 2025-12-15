package com.setof.connectly.module.product.service.stock;

import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.product.dto.stock.UpdateProductStock;
import com.setof.connectly.module.product.entity.stock.StockReservation;
import com.setof.connectly.module.product.mapper.StockMapper;
import com.setof.connectly.module.product.repository.stock.StockReservationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class StockReservationServiceImpl implements StockReservationService {

    private final ProductStockQueryService productStockQueryService;
    private final StockReservationRepository stockReservationRepository;
    private final StockMapper stockMapper;
    private final OrderFindService orderFindService;

    @Override
    public void stockReserve(Order order) {
        StockReservation reservation = stockMapper.reservation(order);
        stockReservationRepository.save(reservation);
        updateStockAndProductStatus(order.getProductId(), order.getQuantity());
    }

    @Override
    public void stocksReserve(List<Order> orders) {
        List<StockReservation> stocksReservation =
                orders.stream().map(stockMapper::reservation).collect(Collectors.toList());

        stockReservationRepository.saveAll(stocksReservation);
        List<UpdateProductStock> updateProductStocks = toUpdateProductStocks(orders);
        updateStocksAndProductStatus(updateProductStocks);
    }

    @Override
    public void cancelReserve(long orderId) {
        Order order = orderFindService.fetchOrderEntity(orderId);
        stockReservationRepository.failed(orderId);
        updateStockAndProductStatus(order.getProductId(), order.getQuantity() * -1);
    }

    @Override
    public void cancelsReserve(long paymentId) {
        List<Order> orders = orderFindService.fetchOrderEntities(paymentId);
        stockReservationRepository.failedAll(paymentId);
        List<UpdateProductStock> updateProductStocks = toUpdateProductStocksWhenCancel(orders);
        updateStocksAndProductStatus(updateProductStocks);
    }

    @Override
    public void purchasedAll(long paymentId) {
        stockReservationRepository.purchasedAll(paymentId);
    }

    private List<UpdateProductStock> toUpdateProductStocks(List<Order> orders) {
        return orders.stream()
                .map(order -> new UpdateProductStock(order.getProductId(), order.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<UpdateProductStock> toUpdateProductStocksWhenCancel(List<Order> orders) {
        return orders.stream()
                .map(
                        order ->
                                new UpdateProductStock(
                                        order.getProductId(), order.getQuantity() * -1))
                .collect(Collectors.toList());
    }

    private void updateStockAndProductStatus(long productId, int qty) {
        productStockQueryService.updateProductStock(new UpdateProductStock(productId, qty));
    }

    private void updateStocksAndProductStatus(List<UpdateProductStock> updateProductStocks) {
        productStockQueryService.updateProductStocks(updateProductStocks);
    }
}
