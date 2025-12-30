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
        String sql = "INSERT INTO TEXT_COMPONENT (COMPONENT_ID, CONTENT, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?)";

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
        String sql = "UPDATE TEXT_COMPONENT " +
                "SET CONTENT = :content, " +
                "UPDATE_OPERATOR = :updateOperator," +
                "UPDATE_DATE = :updateDate " +
                "WHERE TEXT_COMPONENT_ID = :textComponentId";

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

        String sql = "UPDATE TEXT_COMPONENT SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE TEXT_COMPONENT_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", textComponentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);

    }
}
