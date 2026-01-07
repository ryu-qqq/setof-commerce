package com.connectly.partnerAdmin.module.display.repository.content;


import com.connectly.partnerAdmin.module.display.entity.content.Content;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ContentJdbcRepositoryImpl implements ContentJdbcRepository{


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void update(Content content){
        String sql = "UPDATE content SET title = :title, memo = :memo, display_yn = :displayYn, image_url = :imageUrl, display_start_date = :displayStartDate, display_end_date = :displayEndDate, update_operator = :updateOperator, update_date = :updateDate WHERE content_id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("title", content.getTitle());
        params.put("memo", content.getMemo());
        params.put("displayYn", content.getDisplayYn().name());
        params.put("imageUrl", content.getImageUrl());
        params.put("displayStartDate", content.getDisplayPeriod().getDisplayStartDate());
        params.put("displayEndDate", content.getDisplayPeriod().getDisplayEndDate());
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        params.put("id", content.getId());

        namedParameterJdbcTemplate.update(sql, params);
    }




}
