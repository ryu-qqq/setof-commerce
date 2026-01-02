package com.connectly.partnerAdmin.module.display.repository.component.image;


import com.connectly.partnerAdmin.module.display.entity.component.item.ImageComponentItem;
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
public class ImageComponentItemJdbcRepositoryImpl implements ImageComponentItemJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<ImageComponentItem> imageComponentItems) {
        String sql = "INSERT INTO IMAGE_COMPONENT_ITEM (IMAGE_COMPONENT_ID, IMAGE_URL, DISPLAY_ORDER, LINK_URL, WIDTH, HEIGHT, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (ImageComponentItem imageComponentItem : imageComponentItems) {
            Object[] values = new Object[] {
                    imageComponentItem.getImageComponentId(),
                    imageComponentItem.getImageUrl(),
                    imageComponentItem.getDisplayOrder(),
                    imageComponentItem.getLinkUrl(),
                    imageComponentItem.getImageSize().getWidth(),
                    imageComponentItem.getImageSize().getHeight(),
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
    public void deleteAll(List<Long> imageComponentIds) {

        String sql = "UPDATE IMAGE_COMPONENT_ITEM SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE IMAGE_COMPONENT_ITEM_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", imageComponentIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());
        namedParameterJdbcTemplate.update(sql, params);

    }
    @Override
    public void updateAll(List<ImageComponentItem> imageComponentItems){

        String sql = "UPDATE IMAGE_COMPONENT_ITEM " +
                "SET IMAGE_URL = :imageUrl, " +
                "DISPLAY_ORDER = :displayOrder, " +
                "LINK_URL = :linkUrl," +
                "WIDTH = :width," +
                "HEIGHT = :height," +
                "UPDATE_OPERATOR = :updateOperator," +
                "UPDATE_DATE = :updateDate " +
                "WHERE IMAGE_COMPONENT_ITEM_ID = :imageComponentItemId";

        List<Map<String, Object>> batchValues = new ArrayList<>(imageComponentItems.size());
        for (ImageComponentItem imageComponentItem : imageComponentItems) {
            batchValues.add(
                    new MapSqlParameterSource("imageComponentItemId", imageComponentItem.getId())
                            .addValue("imageUrl", imageComponentItem.getImageUrl())
                            .addValue("displayOrder", imageComponentItem.getDisplayOrder())
                            .addValue("linkUrl", imageComponentItem.getLinkUrl())
                            .addValue("width", imageComponentItem.getImageSize().getWidth())
                            .addValue("height", imageComponentItem.getImageSize().getHeight())
                            .addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[imageComponentItems.size()]));
    }
}
