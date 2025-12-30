package com.connectly.partnerAdmin.module.display.repository.component.tab;


import com.connectly.partnerAdmin.module.display.dto.component.tab.TabDetail;
import com.connectly.partnerAdmin.module.display.entity.component.item.Tab;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TextComponent;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TabJdbcRepositoryImpl implements TabJdbcRepository{

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void saveAll(List<Tab> tabs) {
        String sql = "INSERT INTO TAB (TAB_NAME, TAB_COMPONENT_ID, DISPLAY_ORDER) " +
                "VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (Tab tab : tabs) {
            Object[] values = new Object[] {
                    tab.getTabName(),
                    tab.getTabComponentId(),
                    tab.getDisplayOrder()
            };
            batchArgs.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void update(TabDetail tabDetail) {
        String sql = "UPDATE TAB SET TAB_NAME = :tabName, " +
                "DISPLAY_ORDER = :displayOrder, " +
                "UPDATE_OPERATOR = :updateOperator, " +
                "UPDATE_DATE = :updateDate " +
                "WHERE TAB_ID = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", tabDetail.getTabId());
        params.put("tabName", tabDetail.getTabName());
        params.put("displayOrder", tabDetail.getDisplayOrder());
        params.put("updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }


    @Override
    public void deleteAllWithTabComponentIds(List<Long> tabComponentIds) {
        String sql = "UPDATE TAB SET DELETE_YN = 'Y' WHERE TAB_COMPONENT_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", tabComponentIds);

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAll(List<Long> tabIds) {

        String sql = "UPDATE TAB SET DELETE_YN = 'Y' WHERE TAB_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", tabIds);

        namedParameterJdbcTemplate.update(sql, params);
    }


}
