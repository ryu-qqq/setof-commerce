package com.setof.connectly.module.product.repository.stock;

import com.setof.connectly.module.product.entity.stock.StockReservation;
import java.util.List;

public interface StockReservationJdbcRepository {
    void saveAll(List<StockReservation> stockReservationList);

    void purchasedAll(long paymentId);

    void failedAll(long paymentId);

    void failed(long orderId);
}
