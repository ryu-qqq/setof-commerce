package com.connectly.partnerAdmin.module.display.repository.component.tab;

import com.connectly.partnerAdmin.module.display.dto.component.tab.TabComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class TabComponentJdbcRepositoryImpl implements TabComponentJdbcRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void deleteAll(List<Long> tabComponentIds) {
        String sql = "UPDATE tab_component SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE tab_component_id IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", tabComponentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }


    @Override
    public void update(long tabComponentId, TabDetail tabDetail) {
        String sql = "UPDATE tab_component SET sticky_yn = :stickyYn," +
                "tab_moving_type = :tabMovingType," +
                "display_order  = :displayOrder," +
                "update_operator = :updateOperator, " +
                "update_date = :updateDate " +
                "WHERE tab_component_id  = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", tabComponentId);
        params.put("stickyYn", tabDetail.getStickyYn().getName());
        params.put("tabMovingType", tabDetail.getTabMovingType().getName());
        params.put("displayOrder", tabDetail.getDisplayOrder());
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }
}
