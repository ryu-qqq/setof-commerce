package com.setof.connectly.module.display.repository.component.viewExtension;

import com.setof.connectly.module.display.entity.component.ViewExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ViewExtensionJdbcRepositoryImpl implements ViewExtensionJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void deleteAll(List<Long> viewExtensionIds) {

        String sql =
                "UPDATE view_extension SET delete_yn = 'Y', update_operator = :updateOperator,"
                        + " update_date = :updateDate WHERE view_extension_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", viewExtensionIds);
        params.put("updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void updateAll(List<ViewExtension> viewExtensions) {

        String sql =
                "UPDATE view_extension SET "
                        + "view_extension_TYPE = :viewExtensionType, "
                        + "LINK_URL = :linkUrl, "
                        + "BUTTON_name = :buttonName, "
                        + "product_COUNT_PER_CLICK = :productCountPerClick, "
                        + "MAX_CLICK_COUNT = :maxClickCount, "
                        + "AFTER_MAX_ACTION_TYPE = :afterMaxActionType, "
                        + "AFTER_MAX_ACTION_LINK_URL = :afterMaxActionLinkUrl,"
                        + "update_operator = :updateOperator,"
                        + "update_date = :updateDate "
                        + "WHERE view_extension_ID = :viewExtensionId";

        List<Map<String, Object>> batchValues = new ArrayList<>(viewExtensions.size());
        for (ViewExtension viewExtension : viewExtensions) {
            batchValues.add(
                    new MapSqlParameterSource("viewExtensionId", viewExtension.getId())
                            .addValue(
                                    "viewExtensionType",
                                    viewExtension.getViewExtensionDetails().getViewExtensionType())
                            .addValue(
                                    "linkUrl", viewExtension.getViewExtensionDetails().getLinkUrl())
                            .addValue(
                                    "buttonName",
                                    viewExtension.getViewExtensionDetails().getButtonName())
                            .addValue(
                                    "productCountPerClick",
                                    viewExtension
                                            .getViewExtensionDetails()
                                            .getProductCountPerClick())
                            .addValue(
                                    "maxClickCount",
                                    viewExtension.getViewExtensionDetails().getMaxClickCount())
                            .addValue(
                                    "afterMaxActionType",
                                    viewExtension.getViewExtensionDetails().getAfterMaxActionType())
                            .addValue(
                                    "afterMaxActionLinkUrl",
                                    viewExtension
                                            .getViewExtensionDetails()
                                            .getAfterMaxActionLinkUrl())
                            .addValue(
                                    "updateOperator",
                                    MDC.get("user") == null ? "Anonymous" : MDC.get("user"))
                            .addValue("updateDate", LocalDateTime.now())
                            .getValues());
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new Map[viewExtensions.size()]));
    }
}
