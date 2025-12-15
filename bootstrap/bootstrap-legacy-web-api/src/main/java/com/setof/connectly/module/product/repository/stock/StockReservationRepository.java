package com.setof.connectly.module.product.repository.stock;

import com.setof.connectly.module.product.entity.stock.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockReservationRepository
        extends JpaRepository<StockReservation, Long>, StockReservationJdbcRepository {}
