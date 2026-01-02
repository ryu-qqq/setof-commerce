package com.setof.connectly.module.order.repository.snapshot.query.stock;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.stock.OrderSnapShotProductStock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSnapShotProductStockJdbcRepositoryImpl
        implements OrderSnapShotProductStockJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<OrderSnapShotProductStock> orderSnapShotProductStocks) {
        String sql =
                "INSERT INTO order_snapshot_product_stock (order_id, product_stock_id, product_id,"
                    + " stock_quantity, delete_yn, insert_operator, update_operator, insert_date,"
                    + " update_date) VALUES (:orderId, :productStockId, :productId, :stockQuantity,"
                    + " :deleteYn, :insertOperator, :updateOperator, :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotProductStock op : orderSnapShotProductStocks) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue("productStockId", op.getProductStockId())
                    .addValue("productId", op.getProductId())
                    .addValue("stockQuantity", op.getStockQuantity())
                    .addValue("deleteYn", Yn.N.name())
                    .addValue(
                            "insertOperator",
                            MDC.get("user") == null ? "Anonymous" : MDC.get("user"))
                    .addValue(
                            "updateOperator",
                            MDC.get("user") == null ? "Anonymous" : MDC.get("user"))
                    .addValue("insertDate", LocalDateTime.now())
                    .addValue("updateDate", LocalDateTime.now());

            parameters.add(paramSource);
        }

        SqlParameterSource[] batch = parameters.toArray(new SqlParameterSource[0]);
        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }
}
