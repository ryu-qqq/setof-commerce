package com.connectly.partnerAdmin.module.display.repository.component.text;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TextComponent;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TitleComponent;
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


@RequiredArgsConstructor
@Repository
public class TextComponentJdbcRepositoryImpl implements TextComponentJdbcRepository{

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void saveAll(List<TextComponent> textComponents) {
        String sql = "INSERT INTO text_component (component_id, content, insert_operator, update_operator, insert_date, update_date) VALUES (?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (TextComponent textComponent : textComponents) {
            Object[] values = new Object[] {
                    textComponent.getComponent().getId(),
                    textComponent.getContent(),
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
    public void updateAll(List<TextComponentDetail> textComponents) {
        String sql = "UPDATE text_component " +
                "SET content = :content, " +
                "update_operator = :updateOperator," +
                "update_date = :updateDate " +
                "WHERE text_component_id = :textComponentId";

        List<Map<String, Object>> batchValues = new ArrayList<>(textComponents.size());
        for (TextComponentDetail textComponentDetail : textComponents) {
            batchValues.add(
                    new MapSqlParameterSource("content", textComponentDetail.getContent())
                            .addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .addValue("textComponentId", textComponentDetail.getTextComponentId())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[textComponents.size()]));

    }

    @Override
    public void deleteAll(List<Long> textComponentIds) {

        String sql = "UPDATE text_component SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE text_component_id IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", textComponentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);

    }
}
