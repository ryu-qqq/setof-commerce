package com.setof.connectly.module.display.repository.component.image;

import com.setof.connectly.module.display.entity.component.item.ImageComponentItem;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageComponentItemJdbcRepositoryImpl implements ImageComponentItemJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<ImageComponentItem> imageComponentItems) {
        String sql =
                "INSERT INTO IMAGE_COMPONENT_ITEM (IMAGE_COMPONENT_ID, IMAGE_URL, DISPLAY_ORDER,"
                        + " LINK_URL, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE)"
                        + " VALUES (:imageComponentId, :imageUrl, :displayOrder, :linkUrl,"
                        + " :insertOperator, :updateOperator, :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (ImageComponentItem imageComponentItem : imageComponentItems) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("imageComponentId", imageComponentItem.getImageComponentId())
                    .addValue("imageUrl", imageComponentItem.getImageUrl())
                    .addValue("displayOrder", imageComponentItem.getDisplayOrder())
                    .addValue("linkUrl", imageComponentItem.getLinkUrl())
                    .addValue(
                            "insertOperator",
                            MDC.get("user") == null ? "Anonymous" : MDC.get("user"))
                    .addValue(
                            "updateOperator",
                            MDC.get("user") == null ? "Anonymous" : MDC.get("user"))
                    .addValue("insertDate", LocalDateTime.now())
                    .addValue("updateDate", LocalDateTime.now());

            parameters.add(paramSource);
        }

        SqlParameterSource[] batch = parameters.toArray(new SqlParameterSource[0]);
        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }

    @Override
    public void deleteAll(List<Long> imageComponentIds) {

        String sql =
                "UPDATE IMAGE_COMPONENT_ITEM SET DELETE_YN = 'Y', UPDATE_OPERATOR ="
                        + " :updateOperator, UPDATE_DATE = :updateDate WHERE IMAGE_COMPONENT_ID IN"
                        + " (:ids))";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", imageComponentIds);
        params.put("updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void updateAll(List<ImageComponentItem> imageComponentItems) {

        String sql =
                "UPDATE COMPONENT "
                        + "SET IMAGE_URL = :imageUrl, "
                        + "DISPLAY_ORDER = :displayOrder, "
                        + "LINK_URL = :linkUrl,"
                        + "UPDATE_OPERATOR = :updateOperator,"
                        + "UPDATE_DATE = :updateDate "
                        + "WHERE IMAGE_COMPONENT_ID = :imageComponentId";

        List<Map<String, Object>> batchValues = new ArrayList<>(imageComponentItems.size());
        for (ImageComponentItem imageComponentItem : imageComponentItems) {
            batchValues.add(
                    new MapSqlParameterSource("imageComponentId", imageComponentItem.getId())
                            .addValue("imageUrl", imageComponentItem.getImageUrl())
                            .addValue("displayOrder", imageComponentItem.getDisplayOrder())
                            .addValue("linkUrl", imageComponentItem.getLinkUrl())
                            .addValue(
                                    "updateOperator",
                                    MDC.get("user") == null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new Map[imageComponentItems.size()]));
    }
}
