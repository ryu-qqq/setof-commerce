package com.connectly.partnerAdmin.module.display.repository.component.category;

import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
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
public class CategoryComponentJdbcRepositoryImpl implements CategoryComponentJdbcRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void deleteAll(List<Long> categoryComponentIds) {
        String sql = "UPDATE CATEGORY_COMPONENT SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE CATEGORY_COMPONENT_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", categoryComponentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }


    @Override
    public void updateAll(List<CategoryComponentDetail> componentDetails) {
        String sql = "UPDATE CATEGORY_COMPONENT SET CATEGORY_ID = :categoryId, UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE CATEGORY_COMPONENT_ID = :categoryComponentId";

        List<Map<String, Object>> batchValues = new ArrayList<>(componentDetails.size());
        for (CategoryComponentDetail categoryComponentDetail : componentDetails) {
            batchValues.add(
                    new MapSqlParameterSource("categoryComponentId", categoryComponentDetail.getCategoryComponentId())
                            .addValue("categoryId", categoryComponentDetail.getCategoryId())
                            .addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[componentDetails.size()]));
    }
}
