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

        String sql = "UPDATE view_extension SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE view_extension_id = :id";


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

        String sql = "UPDATE view_extension SET " +
                "view_extension_type = :viewExtensionType, " +
                "link_url = :linkUrl, " +
                "button_name = :buttonName, " +
                "product_count_per_click = :productCountPerClick, " +
                "max_click_count = :maxClickCount, " +
                "after_max_action_type = :afterMaxActionType, " +
                "after_max_action_link_url = :afterMaxActionLinkUrl," +
                "update_operator = :updateOperator," +
                "update_date = :updateDate " +
                "WHERE view_extension_id = :viewExtensionId";


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
