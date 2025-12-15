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
                "INSERT INTO ORDER_SNAPSHOT_OPTION_GROUP (ORDER_ID, OPTION_GROUP_ID, OPTION_NAME,"
                    + " DELETE_YN, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE)"
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
