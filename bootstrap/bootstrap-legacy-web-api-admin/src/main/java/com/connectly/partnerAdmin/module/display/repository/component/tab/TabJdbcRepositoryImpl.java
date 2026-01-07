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
        String sql = "INSERT INTO tab (tab_name, tab_component_id, display_order) " +
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
        String sql = "UPDATE tab SET tab_name = :tabName, " +
                "display_order = :displayOrder, " +
                "update_operator = :updateOperator, " +
                "update_date = :updateDate " +
                "WHERE tab_id = :id";

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
        String sql = "UPDATE tab SET delete_yn = 'Y' WHERE tab_component_id IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", tabComponentIds);

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAll(List<Long> tabIds) {

        String sql = "UPDATE tab SET delete_yn = 'Y' WHERE tab_id IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", tabIds);

        namedParameterJdbcTemplate.update(sql, params);
    }


}
