package com.setof.connectly.module.mileage.repository.query;

import com.setof.connectly.module.mileage.entity.MileageTransaction;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MileageTransactionJdbcRepositoryImpl implements MileageTransactionJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<MileageTransaction> mileageTransactions) {
        String sql =
                "INSERT INTO mileage_transaction (user_id, mileage_id, issue_type, target_id,"
                    + " EXPECTED_MILEAGE_AMOUNT, status, insert_operator, update_operator) VALUES"
                    + " (:userId, :mileageId, :issueType, :targetId, :expectedMileageAmount,"
                    + " :status, :insertOperator, :updateOperator)";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(mileageTransactions.size());
        for (MileageTransaction mileageTransaction : mileageTransactions) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("userId", mileageTransaction.getUserId());
            parameterSource.addValue("mileageId", mileageTransaction.getMileageId());
            parameterSource.addValue("issueType", mileageTransaction.getIssueType().name());
            parameterSource.addValue("targetId", mileageTransaction.getTargetId());
            parameterSource.addValue(
                    "expectedMileageAmount", mileageTransaction.getExpectedMileageAmount());
            parameterSource.addValue("status", mileageTransaction.getStatus().name());
            parameterSource.addValue(
                    "insertOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue(
                    "updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));

            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new MapSqlParameterSource[mileageTransactions.size()]));
    }
}
