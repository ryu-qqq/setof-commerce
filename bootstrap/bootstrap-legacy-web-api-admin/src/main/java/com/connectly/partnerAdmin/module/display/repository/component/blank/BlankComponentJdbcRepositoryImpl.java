package com.connectly.partnerAdmin.module.display.repository.component.blank;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.blank.BlankComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.BlankComponent;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TextComponent;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BlankComponentJdbcRepositoryImpl implements BlankComponentJdbcRepository{

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;



    @Override
    public void saveAll(List<BlankComponent> blankComponents) {
        String sql = "INSERT INTO BLANK_COMPONENT (COMPONENT_ID, HEIGHT, LINE_YN, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (BlankComponent blankComponent : blankComponents) {
            Object[] values = new Object[] {
                    blankComponent.getComponent().getId(),
                    blankComponent.getHeight(),
                    blankComponent.getLineYn().name(),
                    MDC.get("user") ==null ? "Anonymous" : MDC.get("user"),
                    MDC.get("user") ==null ? "Anonymous" : MDC.get("user"),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            };
            batchArgs.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }


    @Override
    public void updateAll(List<BlankComponentDetail> blankComponentDetails) {
        String sql = "UPDATE BLANK_COMPONENT " +
                "SET HEIGHT = :height, " +
                "LINE_YN = :lineYn," +
                "UPDATE_OPERATOR = :updateOperator," +
                "UPDATE_DATE = :updateDate " +
                "WHERE BLANK_COMPONENT_ID = :blankComponentId";

        List<Map<String, Object>> batchValues = new ArrayList<>(blankComponentDetails.size());
        for (BlankComponentDetail blankComponentDetail : blankComponentDetails) {
            batchValues.add(
                    new MapSqlParameterSource("height", blankComponentDetail.getHeight())
                            .addValue("lineYn", blankComponentDetail.getLineYn().name())
                            .addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .addValue("blankComponentId", blankComponentDetail.getBlankComponentId())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[blankComponentDetails.size()]));

    }



    @Override
    public void deleteAll(List<Long> blankComponentIds) {
        String sql = "UPDATE BLANK_COMPONENT SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE BLANK_COMPONENT_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", blankComponentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());
        namedParameterJdbcTemplate.update(sql, params);

    }
}
