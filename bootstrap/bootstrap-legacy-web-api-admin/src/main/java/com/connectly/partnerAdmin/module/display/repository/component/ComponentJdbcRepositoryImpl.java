package com.connectly.partnerAdmin.module.display.repository.component;


import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
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
public class ComponentJdbcRepositoryImpl implements ComponentJdbcRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void deleteAll(List<Long> componentIds) {
        if (componentIds.isEmpty()) {
            return;
        }

        String sql = "UPDATE component SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE component_id IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", componentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void updateAll(List<SubComponent> components) {
        String sql = "UPDATE component " +
                "SET component_name = :componentName, " + // 콤마로 수정
                "exposed_products = :exposedProducts, " + // 공백 제거
                "display_start_date = :displayStartDate," +
                "display_end_date = :displayEndDate," +
                "display_order = :displayOrder," +
                "display_yn = :displayYn," +
                "list_type = :listYn," +
                "order_type = :orderType," +
                "badge_type = :badgeType," +
                "filter_yn = :filterYn," +
                "update_operator = :updateOperator," +
                "update_date = :updateDate " +
                "WHERE component_id = :componentId";

        List<Map<String, Object>> batchValues = new ArrayList<>(components.size());
        for (SubComponent subComponent : components) {
            batchValues.add(
                    new MapSqlParameterSource("componentId", subComponent.getComponentId())
                            .addValue("componentName", subComponent.getComponentName())
                            .addValue("exposedProducts", subComponent.getExposedProducts())
                            .addValue("displayStartDate", subComponent.getDisplayPeriod().getDisplayStartDate())
                            .addValue("displayEndDate", subComponent.getDisplayPeriod().getDisplayEndDate())
                            .addValue("displayOrder", subComponent.getDisplayOrder())
                            .addValue("displayYn", subComponent.getDisplayYn().getName())
                            .addValue("listYn", subComponent.getComponentDetails().getListType().getName())
                            .addValue("orderType", subComponent.getComponentDetails().getOrderType().getName())
                            .addValue("badgeType", subComponent.getComponentDetails().getBadgeType().getName())
                            .addValue("filterYn", subComponent.getComponentDetails().getFilterYn().getName())
                            .addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[components.size()]));
    }
}
