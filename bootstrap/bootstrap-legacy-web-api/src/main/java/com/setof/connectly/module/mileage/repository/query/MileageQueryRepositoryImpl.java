package com.setof.connectly.module.mileage.repository.query;

import com.setof.connectly.module.mileage.dto.MileageDto;
import com.setof.connectly.module.mileage.dto.MileageRefundDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MileageQueryRepositoryImpl implements MileageQueryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void updateMileages(List<MileageDto> mileages) {
        String sql =
                "UPDATE MILEAGE "
                        + "SET USED_MILEAGE_AMOUNT = USED_MILEAGE_AMOUNT + :usedMileageAmount, "
                        + "ACTIVE_YN = :activeYn, "
                        + "UPDATE_OPERATOR = :updateOperator,"
                        + "UPDATE_DATE = :updateDate "
                        + "WHERE MILEAGE_ID = :mileageId";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(mileages.size());
        for (MileageDto mileageDto : mileages) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("usedMileageAmount", mileageDto.getCurrentUsedMileageAmount());
            parameterSource.addValue("activeYn", mileageDto.getActiveYn().name());
            parameterSource.addValue(
                    "updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue("updateDate", LocalDateTime.now());
            parameterSource.addValue("mileageId", mileageDto.getMileageId());
            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new MapSqlParameterSource[mileages.size()]));
    }

    public void updateRefundMileages(List<MileageRefundDto> refundMileages) {
        String sql =
                "UPDATE MILEAGE "
                        + "SET USED_MILEAGE_AMOUNT = USED_MILEAGE_AMOUNT - :usedMileageAmount, "
                        + "ACTIVE_YN = :activeYn, "
                        + "UPDATE_OPERATOR = :updateOperator,"
                        + "UPDATE_DATE = :updateDate "
                        + "WHERE MILEAGE_ID = :mileageId";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(refundMileages.size());
        for (MileageRefundDto mileageDto : refundMileages) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("usedMileageAmount", mileageDto.getUsedAmount());
            parameterSource.addValue("activeYn", mileageDto.getActiveYn().name());
            parameterSource.addValue(
                    "updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue("updateDate", LocalDateTime.now());
            parameterSource.addValue("mileageId", mileageDto.getMileageId());
            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new MapSqlParameterSource[refundMileages.size()]));
    }
}
