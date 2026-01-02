package com.setof.connectly.module.order.repository.snapshot.query.option;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionGroup;
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
public class OrderSnapShotOptionGroupJdbcRepositoryImpl
        implements OrderSnapShotOptionGroupJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<OrderSnapShotOptionGroup> orderSnapShotOptionGroups) {
        String sql =
                "INSERT INTO order_snapshot_option_group (order_id, option_group_id, option_name,"
                    + " delete_yn, insert_operator, update_operator, insert_date, update_date)"
                    + " VALUES (:orderId, :optionGroupId, :optionName, :deleteYn, :insertOperator,"
                    + " :updateOperator, :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotOptionGroup op : orderSnapShotOptionGroups) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue("optionGroupId", op.getSnapShotOptionGroup().getOptionGroupId())
                    .addValue("optionName", op.getSnapShotOptionGroup().getOptionName().getName())
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
