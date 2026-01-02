package com.connectly.partnerAdmin.module.display.repository.component.item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.connectly.partnerAdmin.module.display.dto.component.DisplayProductGroupThumbnail;
import com.connectly.partnerAdmin.module.display.entity.component.ComponentItem;
import com.connectly.partnerAdmin.module.display.enums.SortType;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ComponentItemJdbcRepositoryImpl implements ComponentItemJdbcRepository{

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<ComponentItem> componentItems) {
        String sql = "INSERT INTO COMPONENT_ITEM (COMPONENT_TARGET_ID, PRODUCT_GROUP_ID, PRODUCT_DISPLAY_NAME, PRODUCT_DISPLAY_IMAGE, DISPLAY_ORDER, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (ComponentItem componentItem : componentItems) {
            Object[] values = new Object[] {
                    componentItem.getComponentTarget().getId(),
                    componentItem.getProductGroupId(),
                    componentItem.getProductDisplayName(),
                    componentItem.getProductDisplayImage(),
                    componentItem.getDisplayOrder(),
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
    public void deleteAll(List<Long> componentIds){
        if(componentIds.isEmpty() ) return;

        String sql = "UPDATE COMPONENT_ITEM SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE COMPONENT_TARGET_ID IN " +
                "(SELECT COMPONENT_TARGET_ID FROM COMPONENT_TARGET WHERE COMPONENT_ID IN (:ids))";


        Map<String, Object> params = new HashMap<>();
        params.put("ids", componentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAllWithTabIds(List<Long> tabIds) {
        if(tabIds.isEmpty() ) return;

        String sql = "UPDATE COMPONENT_ITEM SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE COMPONENT_TARGET_ID IN " +
                "(SELECT COMPONENT_TARGET_ID FROM COMPONENT_TARGET WHERE TAB_ID IN (:ids))";


        Map<String, Object> params = new HashMap<>();
        params.put("ids", tabIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAll(long componentId, SortType sortType,  List<Long> productGroupIds) {
        if(productGroupIds.isEmpty() ) return;

        String sql = "UPDATE COMPONENT_ITEM SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE PRODUCT_GROUP_ID IN (:ids) AND COMPONENT_TARGET_ID IN " +
                "(SELECT COMPONENT_TARGET_ID FROM COMPONENT_TARGET WHERE COMPONENT_ID = :id and SORT_TYPE = :sortType )";


        Map<String, Object> params = new HashMap<>();
        params.put("id", componentId);
        params.put("ids", productGroupIds);
        params.put("sortType", sortType.getName());
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void updateAll(long componentId, long componentTargetId, List<DisplayProductGroupThumbnail> updatedProducts) {
        String sql = "UPDATE COMPONENT_ITEM " +
                "SET PRODUCT_DISPLAY_NAME = :productGroupName, " +
                "PRODUCT_DISPLAY_IMAGE = :productImageUrl, " +
                "COMPONENT_TARGET_ID = :componentTargetId, " +
                "DISPLAY_ORDER = :displayOrder " +
                "WHERE PRODUCT_GROUP_ID = :productGroupId " +
                "AND COMPONENT_TARGET_ID IN (SELECT COMPONENT_TARGET_ID FROM COMPONENT_TARGET WHERE COMPONENT_ID = :componentId)";

        List<Map<String, Object>> batchValues = new ArrayList<>(updatedProducts.size());
        for (DisplayProductGroupThumbnail productGroupThumbnail : updatedProducts) {
            batchValues.add(
                    new MapSqlParameterSource("productGroupId", productGroupThumbnail.getProductGroupId())
                            .addValue("productGroupName", productGroupThumbnail.getProductGroupName())
                            .addValue("productImageUrl", productGroupThumbnail.getProductImageUrl())
                            .addValue("componentTargetId", componentTargetId)
                            .addValue("displayOrder", productGroupThumbnail.getDisplayOrder())
                            .addValue("componentId", componentId)
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[updatedProducts.size()]));
    }
}
