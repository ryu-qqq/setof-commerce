package com.setof.connectly.module.mileage.repository.history;

import com.setof.connectly.module.mileage.entity.MileageHistory;
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
public class MileageHistoryJdbcRepositoryImpl implements MileageHistoryJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<MileageHistory> mileageHistories) {
        String sql =
                "INSERT INTO MILEAGE_HISTORY (USER_ID, MILEAGE_ID, CHANGE_AMOUNT, REASON,"
                        + " ISSUE_TYPE, TARGET_ID, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE,"
                        + " UPDATE_DATE) VALUES (:userId, :mileageId, :changeAmount, :reason,"
                        + " :issueType, :targetId, :insertOperator, :updateOperator, :insertDate,"
                        + " :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (MileageHistory mileageHistory : mileageHistories) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("userId", mileageHistory.getUserId())
                    .addValue("mileageId", mileageHistory.getMileageId())
                    .addValue("changeAmount", mileageHistory.getChangeAmount())
                    .addValue("reason", mileageHistory.getReason().name())
                    .addValue("issueType", mileageHistory.getIssueType().name())
                    .addValue("targetId", mileageHistory.getTargetId())
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
