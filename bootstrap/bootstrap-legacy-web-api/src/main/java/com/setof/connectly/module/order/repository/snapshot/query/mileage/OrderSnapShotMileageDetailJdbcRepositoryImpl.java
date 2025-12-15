package com.setof.connectly.module.order.repository.snapshot.query.mileage;

import com.setof.connectly.module.order.entity.snapshot.mileage.OrderSnapShotMileageDetail;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSnapShotMileageDetailJdbcRepositoryImpl
        implements OrderSnapShotMileageDetailJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<OrderSnapShotMileageDetail> orderSnapShotMileageDetails) {
        String sql =
                "INSERT INTO ORDER_SNAPSHOT_MILEAGE_DETAIL (ORDER_SNAPSHOT_MILEAGE_ID, MILEAGE_ID,"
                        + " USED_AMOUNT, MILEAGE_BALANCE, INSERT_OPERATOR, UPDATE_OPERATOR) VALUES"
                        + " (:orderSnapShotMileageDetail, :mileageId, :usedAmount, :mileageBalance,"
                        + " :insertOperator, :updateOperator)";

        List<MapSqlParameterSource> batchValues =
                new ArrayList<>(orderSnapShotMileageDetails.size());
        for (OrderSnapShotMileageDetail orderSnapShotMileageDetail : orderSnapShotMileageDetails) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();

            parameterSource.addValue(
                    "orderSnapShotMileageDetail",
                    orderSnapShotMileageDetail.getOrderSnapShotMileageId());
            parameterSource.addValue("mileageId", orderSnapShotMileageDetail.getMileageId());
            parameterSource.addValue("usedAmount", orderSnapShotMileageDetail.getUsedAmount());
            parameterSource.addValue(
                    "mileageBalance", orderSnapShotMileageDetail.getMileageBalance());
            parameterSource.addValue(
                    "insertOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue(
                    "updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));

            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql,
                batchValues.toArray(new MapSqlParameterSource[orderSnapShotMileageDetails.size()]));
    }
}
