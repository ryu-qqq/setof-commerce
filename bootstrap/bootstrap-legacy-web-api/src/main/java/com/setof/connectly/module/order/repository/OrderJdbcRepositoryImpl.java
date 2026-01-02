package com.setof.connectly.module.order.repository;

import com.setof.connectly.module.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepositoryImpl implements OrderJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void updateOrderStatus(long paymentId, OrderStatus orderStatus) {

        String sql =
                "UPDATE orders SET order_status = :orderStatus, update_operator = :updateOperator,"
                        + " update_date = :updateDate WHERE payment_id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", paymentId);
        params.put("orderStatus", orderStatus.name());
        params.put("updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());
        namedParameterJdbcTemplate.update(sql, params);
    }
}
