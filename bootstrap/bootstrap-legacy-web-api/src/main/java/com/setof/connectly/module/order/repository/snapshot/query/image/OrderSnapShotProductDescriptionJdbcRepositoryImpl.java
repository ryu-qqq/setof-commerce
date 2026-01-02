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
                "INSERT INTO order_snapshot_product_group_detail_description (order_id,"
                    + " product_group_id, product_group_IMAGE_TYPE, image_url, delete_yn,"
                    + " insert_operator, update_operator, insert_date, update_date) VALUES"
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
