package com.setof.connectly.module.product.repository.stock;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.product.entity.stock.StockReservation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StockReservationJdbcRepositoryImpl implements StockReservationJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<StockReservation> stockReservationList) {
        String sql =
                "INSERT INTO STOCK_RESERVATION (PRODUCT_ID, USER_ID, PAYMENT_ID, ORDER_ID,"
                    + " STOCK_QUANTITY, RESERVED_AT, RESERVATION_STATUS, DELETE_YN,"
                    + " INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) VALUES"
                    + " (:productId, :userId, :paymentId, :orderId, :stockQuantity, :reservedAt,"
                    + " :reservationStatus, :deleteYn, :insertOperator, :updateOperator,"
                    + " :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (StockReservation stockReservation : stockReservationList) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("productId", stockReservation.getProductId())
                    .addValue("userId", stockReservation.getUserId())
                    .addValue("paymentId", stockReservation.getPaymentId())
                    .addValue("orderId", stockReservation.getOrderId())
                    .addValue("stockQuantity", stockReservation.getStockQuantity())
                    .addValue("reservedAt", stockReservation.getReservedAt())
                    .addValue("reservationStatus", stockReservation.getReservationStatus().name())
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

    @Override
    public void purchasedAll(long paymentId) {
        String sql =
                "UPDATE STOCK_RESERVATION SET RESERVATION_STATUS = 'PURCHASED', UPDATE_DATE = :date"
                        + "  WHERE PAYMENT_ID = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", paymentId);
        params.put("date", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void failedAll(long paymentId) {
        String sql =
                "UPDATE STOCK_RESERVATION SET RESERVATION_STATUS = 'FAILED', UPDATE_DATE = :date "
                        + " WHERE PAYMENT_ID = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", paymentId);
        params.put("date", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void failed(long orderId) {
        String sql =
                "UPDATE STOCK_RESERVATION SET RESERVATION_STATUS = 'FAILED', UPDATE_DATE = :date "
                        + " WHERE ORDER_ID = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", orderId);
        params.put("date", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }
}
