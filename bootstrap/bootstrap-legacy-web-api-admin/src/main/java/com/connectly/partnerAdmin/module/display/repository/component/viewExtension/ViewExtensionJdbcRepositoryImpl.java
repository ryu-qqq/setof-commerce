package com.connectly.partnerAdmin.module.display.repository.component.viewExtension;

import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class ViewExtensionJdbcRepositoryImpl implements ViewExtensionJdbcRepository {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void deleteAll(List<Long> viewExtensionIds) {

        String sql = "UPDATE VIEW_EXTENSION SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE VIEW_EXTENSION_ID = :id";


        List<Map<String, Object>> batchValues = new ArrayList<>(viewExtensionIds.size());
        for (Long viewExtensionId : viewExtensionIds) {
            batchValues.add(
                    new MapSqlParameterSource("id", viewExtensionId)
                            .addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .getValues());
        }


        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[viewExtensionIds.size()]));
    }

    @Override
    public void updateAll(List<ViewExtension> viewExtensions) {

        String sql = "UPDATE VIEW_EXTENSION SET " +
                "VIEW_EXTENSION_TYPE = :viewExtensionType, " +
                "LINK_URL = :linkUrl, " +
                "BUTTON_NAME = :buttonName, " +
                "PRODUCT_COUNT_PER_CLICK = :productCountPerClick, " +
                "MAX_CLICK_COUNT = :maxClickCount, " +
                "AFTER_MAX_ACTION_TYPE = :afterMaxActionType, " +
                "AFTER_MAX_ACTION_LINK_URL = :afterMaxActionLinkUrl," +
                "UPDATE_OPERATOR = :updateOperator," +
                "UPDATE_DATE = :updateDate " +
                "WHERE VIEW_EXTENSION_ID = :viewExtensionId";


        List<Map<String, Object>> batchValues = new ArrayList<>(viewExtensions.size());
        for (ViewExtension viewExtension : viewExtensions) {
            batchValues.add(
                    new MapSqlParameterSource("viewExtensionId", viewExtension.getId())
                            .addValue("viewExtensionType", viewExtension.getViewExtensionDetails().getViewExtensionType().getName())
                            .addValue("linkUrl", viewExtension.getViewExtensionDetails().getLinkUrl())
                            .addValue("buttonName", viewExtension.getViewExtensionDetails().getButtonName())
                            .addValue("productCountPerClick", viewExtension.getViewExtensionDetails().getProductCountPerClick())
                            .addValue("maxClickCount", viewExtension.getViewExtensionDetails().getMaxClickCount())
                            .addValue("afterMaxActionType", viewExtension.getViewExtensionDetails().getAfterMaxActionType().getName())
                            .addValue("afterMaxActionLinkUrl", viewExtension.getViewExtensionDetails().getAfterMaxActionLinkUrl())
                            .addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[viewExtensions.size()]));

    }
}
