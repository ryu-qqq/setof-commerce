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
        String sql = "UPDATE CONTENT SET TITLE = :title, MEMO = :memo, DISPLAY_YN = :displayYn, IMAGE_URL = :imageUrl, DISPLAY_START_DATE = :displayStartDate, DISPLAY_END_DATE = :displayEndDate, UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE CONTENT_ID = :id";

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
