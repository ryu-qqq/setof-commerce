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
        String sql = "INSERT INTO component_item (component_target_id, product_group_id, product_display_name, product_display_image, display_order, insert_operator, update_operator, insert_date, update_date) " +
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

        String sql = "UPDATE component_item SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE component_target_id IN " +
                "(SELECT component_target_id FROM component_target WHERE component_id IN (:ids))";


        Map<String, Object> params = new HashMap<>();
        params.put("ids", componentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAllWithTabIds(List<Long> tabIds) {
        if(tabIds.isEmpty() ) return;

        String sql = "UPDATE component_item SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE component_target_id IN " +
                "(SELECT component_target_id FROM component_target WHERE tab_id IN (:ids))";


        Map<String, Object> params = new HashMap<>();
        params.put("ids", tabIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAll(long componentId, SortType sortType,  List<Long> productGroupIds) {
        if(productGroupIds.isEmpty() ) return;

        String sql = "UPDATE component_item SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE product_group_id IN (:ids) AND component_target_id IN " +
                "(SELECT component_target_id FROM component_target WHERE component_id = :id and sort_type = :sortType )";


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
        String sql = "UPDATE component_item " +
                "SET product_display_name = :productGroupName, " +
                "product_display_image = :productImageUrl, " +
                "component_target_id = :componentTargetId, " +
                "display_order = :displayOrder " +
                "WHERE product_group_id = :productGroupId " +
                "AND component_target_id IN (SELECT component_target_id FROM component_target WHERE component_id = :componentId)";

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
