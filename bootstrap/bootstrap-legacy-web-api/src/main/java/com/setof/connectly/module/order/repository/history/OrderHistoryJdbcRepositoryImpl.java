package com.setof.connectly.module.order.repository.history;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.order.OrderHistory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderHistoryJdbcRepositoryImpl implements OrderHistoryJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<OrderHistory> orderHistories) {
        String sql =
                "INSERT INTO ORDERS_HISTORY (ORDER_ID, CHANGE_REASON, CHANGE_DETAIL_REASON,"
                        + " ORDER_STATUS, DELETE_YN, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE,"
                        + " UPDATE_DATE) VALUES (:orderId, :changeReason, :changeDetailReason,"
                        + " :orderStatus, :deleteYn, :insertOperator, :updateOperator, :insertDate,"
                        + " :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderHistory orderHistory : orderHistories) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", orderHistory.getOrderId())
                    .addValue("changeReason", orderHistory.getChangeReason())
                    .addValue("changeDetailReason", orderHistory.getChangeDetailReason())
                    .addValue("orderStatus", orderHistory.getOrderStatus().getName())
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
