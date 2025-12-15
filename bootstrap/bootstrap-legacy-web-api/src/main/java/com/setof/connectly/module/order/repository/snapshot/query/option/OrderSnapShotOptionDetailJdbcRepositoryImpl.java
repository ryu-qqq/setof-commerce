package com.setof.connectly.module.order.repository.snapshot.query.option;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionDetail;
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
public class OrderSnapShotOptionDetailJdbcRepositoryImpl
        implements OrderSnapShotOptionDetailJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<OrderSnapShotOptionDetail> orderSnapShotOptionDetails) {
        String sql =
                "INSERT INTO ORDER_SNAPSHOT_OPTION_DETAIL (ORDER_ID, OPTION_DETAIL_ID,"
                    + " OPTION_GROUP_ID, OPTION_VALUE, DELETE_YN, INSERT_OPERATOR, UPDATE_OPERATOR,"
                    + " INSERT_DATE, UPDATE_DATE) VALUES (:orderId, :optionDetailId,"
                    + " :optionGroupId, :optionValue, :deleteYn, :insertOperator, :updateOperator,"
                    + " :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotOptionDetail op : orderSnapShotOptionDetails) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue("optionDetailId", op.getSnapShotOptionDetail().getOptionDetailId())
                    .addValue("optionGroupId", op.getSnapShotOptionDetail().getOptionGroupId())
                    .addValue("optionValue", op.getSnapShotOptionDetail().getOptionValue())
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
