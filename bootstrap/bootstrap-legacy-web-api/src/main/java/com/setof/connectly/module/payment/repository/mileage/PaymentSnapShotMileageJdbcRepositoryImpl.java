package com.setof.connectly.module.payment.repository.mileage;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotMileage;
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
public class PaymentSnapShotMileageJdbcRepositoryImpl
        implements PaymentSnapShotMileageJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<PaymentSnapShotMileage> paymentSnapShotMileages) {
        String sql =
                "INSERT INTO PAYMENT_SNAPSHOT_MILEAGE (PAYMENT_ID, USER_ID, USED_MILEAGE_AMOUNT,"
                    + " MILEAGE_BALANCE, DELETE_YN, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE,"
                    + " UPDATE_DATE) VALUES (:paymentId, :userId, :usedMileageAmount,"
                    + " :mileageBalance, :deleteYn, :insertOperator, :updateOperator, :insertDate,"
                    + " :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (PaymentSnapShotMileage paymentSnapShotMileage : paymentSnapShotMileages) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("paymentId", paymentSnapShotMileage.getPaymentId())
                    .addValue("userId", paymentSnapShotMileage.getUserId())
                    .addValue("usedMileageAmount", paymentSnapShotMileage.getUsedMileageAmount())
                    .addValue("mileageBalance", paymentSnapShotMileage.getMileageBalance())
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
