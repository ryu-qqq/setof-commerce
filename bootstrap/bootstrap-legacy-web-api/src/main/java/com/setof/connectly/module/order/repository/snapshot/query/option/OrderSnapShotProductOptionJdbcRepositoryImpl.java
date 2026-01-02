package com.setof.connectly.module.order.repository.snapshot.query.option;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotProductOption;
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
public class OrderSnapShotProductOptionJdbcRepositoryImpl
        implements OrderSnapShotProductOptionJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<OrderSnapShotProductOption> orderSnapShotProductOptions) {
        String sql =
                "INSERT INTO order_snapshot_product_option (order_id, product_option_id,"
                    + " product_id, option_group_id, option_detail_id, ADDITIONAL_PRICE, delete_yn,"
                    + " insert_operator, update_operator, insert_date, update_date) VALUES"
                    + " (:orderId, :productOptionId, :productId, :optionGroupId, :optionDetailId,"
                    + " :additionalPrice, :deleteYn, :insertOperator, :updateOperator, :insertDate,"
                    + " :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotProductOption op : orderSnapShotProductOptions) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue("productOptionId", op.getSnapShotProductOption().getProductOptionId())
                    .addValue("productId", op.getSnapShotProductOption().getProductId())
                    .addValue("optionGroupId", op.getSnapShotProductOption().getOptionGroupId())
                    .addValue("optionDetailId", op.getSnapShotProductOption().getOptionDetailId())
                    .addValue("additionalPrice", op.getSnapShotProductOption().getAdditionalPrice())
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

        SqlParameterSource[] batch = parameters.toArray(new SqlParameterSource[parameters.size()]);
        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }
}
