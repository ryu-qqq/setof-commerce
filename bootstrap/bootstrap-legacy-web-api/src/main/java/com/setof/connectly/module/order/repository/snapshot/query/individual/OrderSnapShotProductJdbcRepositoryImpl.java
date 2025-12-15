package com.setof.connectly.module.order.repository.snapshot.query.individual;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProduct;
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
public class OrderSnapShotProductJdbcRepositoryImpl implements OrderSnapShotProductJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<OrderSnapShotProduct> orderSnapShotProducts) {
        String sql =
                "INSERT INTO ORDER_SNAPSHOT_PRODUCT (ORDER_ID, PRODUCT_ID, PRODUCT_GROUP_ID,"
                    + " SOLD_OUT_YN, DISPLAY_YN, DELETE_YN, INSERT_OPERATOR, UPDATE_OPERATOR,"
                    + " INSERT_DATE, UPDATE_DATE) VALUES (:orderId, :productId, :productGroupId,"
                    + " :soldOutYn, :displayYn, :deleteYn, :insertOperator, :updateOperator,"
                    + " :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotProduct op : orderSnapShotProducts) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue("productId", op.getSnapShotProduct().getProductId())
                    .addValue("productGroupId", op.getSnapShotProduct().getProductGroupId())
                    .addValue(
                            "soldOutYn",
                            op.getSnapShotProduct().getProductStatus().getSoldOutYn().name())
                    .addValue(
                            "displayYn",
                            op.getSnapShotProduct().getProductStatus().getDisplayYn().name())
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
