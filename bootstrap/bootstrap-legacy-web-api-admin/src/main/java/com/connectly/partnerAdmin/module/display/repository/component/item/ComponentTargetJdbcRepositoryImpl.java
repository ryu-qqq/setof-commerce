package com.connectly.partnerAdmin.module.display.repository.component.item;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Repository
public class ComponentTargetJdbcRepositoryImpl implements ComponentTargetJdbcRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void deleteAll(List<Long> componentTargetIds) {
        String sql = "UPDATE COMPONENT_TARGET SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE COMPONENT_TARGET_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", componentTargetIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAllWithTabIds(List<Long> tabIds) {
        String sql = "UPDATE COMPONENT_TARGET SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE TAB_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", tabIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }
}
