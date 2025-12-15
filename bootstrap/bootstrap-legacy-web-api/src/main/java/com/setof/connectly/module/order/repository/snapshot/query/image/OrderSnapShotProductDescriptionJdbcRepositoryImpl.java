package com.setof.connectly.module.order.repository.snapshot.query.image;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupDetailDescription;
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
public class OrderSnapShotProductDescriptionJdbcRepositoryImpl
        implements OrderSnapShotProductDescriptionJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(
            Set<OrderSnapShotProductGroupDetailDescription>
                    orderSnapShotProductGroupDetailDescriptions) {
        String sql =
                "INSERT INTO ORDER_SNAPSHOT_PRODUCT_GROUP_DETAIL_DESCRIPTION (ORDER_ID,"
                    + " PRODUCT_GROUP_ID, PRODUCT_GROUP_IMAGE_TYPE, IMAGE_URL, DELETE_YN,"
                    + " INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) VALUES"
                    + " (:orderId, :productGroupId, :productGroupImageType, :imageUrl, :deleteYn,"
                    + " :insertOperator, :updateOperator, :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotProductGroupDetailDescription op :
                orderSnapShotProductGroupDetailDescriptions) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue(
                            "productGroupId",
                            op.getSnapShotProductGroupDetailDescription().getProductGroupId())
                    .addValue(
                            "productGroupImageType",
                            op.getSnapShotProductGroupDetailDescription()
                                    .getImageDetail()
                                    .getProductGroupImageType()
                                    .name())
                    .addValue(
                            "imageUrl",
                            op.getSnapShotProductGroupDetailDescription()
                                    .getImageDetail()
                                    .getImageUrl())
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
