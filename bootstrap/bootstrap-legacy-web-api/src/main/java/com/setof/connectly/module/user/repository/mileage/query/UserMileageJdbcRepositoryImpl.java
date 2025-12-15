package com.setof.connectly.module.user.repository.mileage.query;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserMileageJdbcRepositoryImpl implements UserMileageJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void updateUsingMileage(double currentUsingMileageAmount, long userId) {
        String sql =
                "UPDATE USER_MILEAGE SET CURRENT_MILEAGE = :currentMileage, UPDATE_DATE = "
                    + " :updateDate, UPDATE_OPERATOR = :updateOperator WHERE USER_ID = :userId ";

        Map<String, Object> params = new HashMap<>();
        params.put("currentMileage", currentUsingMileageAmount);
        params.put("userId", userId);
        params.put("updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void updateRefundMileage(double totalRefundMileage, long userId) {
        String sql =
                "UPDATE USER_MILEAGE SET CURRENT_MILEAGE = CURRENT_MILEAGE + :totalRefundMileage,"
                    + " UPDATE_DATE =  :updateDate, UPDATE_OPERATOR = :updateOperator WHERE USER_ID"
                    + " = :userId ";

        Map<String, Object> params = new HashMap<>();
        params.put("totalRefundMileage", totalRefundMileage);
        params.put("userId", userId);
        params.put("updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());
        namedParameterJdbcTemplate.update(sql, params);
    }
}
