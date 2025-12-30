package com.connectly.partnerAdmin.module.display.repository.component.title;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TitleComponent;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupImage;
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
public class TitleComponentJdbcRepositoryImpl implements TitleComponentJdbcRepository{

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void saveAll(List<TitleComponent> titleComponents) {

        String sql = "INSERT INTO TITLE_COMPONENT (COMPONENT_ID, TITLE1, TITLE2, SUB_TITLE1, SUB_TITLE2, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (TitleComponent titleComponent : titleComponents) {
            Object[] values = new Object[] {
                    titleComponent.getComponent().getId(),
                    titleComponent.getTitleDetails().getTitle1(),
                    titleComponent.getTitleDetails().getTitle2(),
                    titleComponent.getTitleDetails().getSubTitle1(),
                    titleComponent.getTitleDetails().getSubTitle2(),
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
    public void updateAll(List<TitleComponentDetail> titleComponents) {
        String sql = "UPDATE TITLE_COMPONENT " +
                "SET TITLE1 = :title1, " +
                "TITLE2 = :title2, " +
                "SUB_TITLE1 = :subTitle1," +
                "SUB_TITLE2 = :subTitle2," +
                "UPDATE_OPERATOR = :updateOperator," +
                "UPDATE_DATE = :updateDate " +
                "WHERE TITLE_COMPONENT_ID = :titleComponentId";

        List<Map<String, Object>> batchValues = new ArrayList<>(titleComponents.size());
        for (TitleComponentDetail titleComponent : titleComponents) {
            batchValues.add(
                    new MapSqlParameterSource("title1", titleComponent.getTitle1())
                            .addValue("title2", titleComponent.getTitle2())
                            .addValue("subTitle1", titleComponent.getSubTitle1())
                            .addValue("subTitle2", titleComponent.getSubTitle2())
                            .addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .addValue("titleComponentId", titleComponent.getTitleComponentId())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[titleComponents.size()]));

    }

    @Override
    public void deleteAll(List<Long> titleComponentIds) {

        String sql = "UPDATE TITLE_COMPONENT SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE TITLE_COMPONENT_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", titleComponentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }
}
