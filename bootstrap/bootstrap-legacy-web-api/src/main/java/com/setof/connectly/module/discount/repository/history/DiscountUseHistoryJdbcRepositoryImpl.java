package com.setof.connectly.module.discount.repository.history;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.discount.entity.history.DiscountUseHistory;
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
public class DiscountUseHistoryJdbcRepositoryImpl implements DiscountUseHistoryJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<DiscountUseHistory> discountUseHistories) {
        String sql =
                "INSERT INTO discount_use_history (discount_policy_id, user_id, name, order_id,"
                    + " payment_id, product_group_id, USE_DATE, delete_yn, insert_operator,"
                    + " update_operator, insert_date, update_date) VALUES (:discountPolicyId,"
                    + " :userId, :name, :orderId, :paymentId, :productGroupId, :useDate, :deleteYn,"
                    + " :insertOperator, :updateOperator, :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (DiscountUseHistory discountUseHistory : discountUseHistories) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("discountPolicyId", discountUseHistory.getDiscountPolicyId())
                    .addValue("userId", discountUseHistory.getUserId())
                    .addValue("name", discountUseHistory.getName())
                    .addValue("orderId", discountUseHistory.getOrderId())
                    .addValue("paymentId", discountUseHistory.getPaymentId())
                    .addValue("productGroupId", discountUseHistory.getProductGroupId())
                    .addValue("useDate", discountUseHistory.getUseDate())
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
