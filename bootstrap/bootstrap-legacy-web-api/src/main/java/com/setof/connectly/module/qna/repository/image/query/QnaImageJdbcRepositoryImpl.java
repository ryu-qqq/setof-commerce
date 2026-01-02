package com.setof.connectly.module.qna.repository.image.query;


import com.setof.connectly.module.qna.entity.QnaImage;
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
public class QnaImageJdbcRepositoryImpl implements QnaImageJdbcRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<QnaImage> qnaImages) {
        String sql = "INSERT INTO qna_image (QNA_issue_type, qna_id, qna_answer_id, image_url, display_order,  insert_operator, update_operator) " +
                "VALUES (:qnaIssueType, :qnaId, :qnaAnswerId, :imageUrl, :displayOrder, :insertOperator, :updateOperator)";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(qnaImages.size());
        for (QnaImage qnaImage : qnaImages) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("qnaIssueType", qnaImage.getQnaIssueType().toString());
            parameterSource.addValue("qnaId", qnaImage.getQnaId());
            parameterSource.addValue("qnaAnswerId", qnaImage.getQnaAnswerId());
            parameterSource.addValue("imageUrl", qnaImage.getImageUrl());
            parameterSource.addValue("displayOrder", qnaImage.getDisplayOrder());
            parameterSource.addValue("insertOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new MapSqlParameterSource[qnaImages.size()]));
    }

    @Override
    public void updateAll(List<QnaImage> qnaImages) {
        String sql = "UPDATE qna_image " +
                "SET image_url = :imageUrl, " +
                "display_order = :displayOrder," +
                "update_operator = :updateOperator," +
                "update_date = :updateDate " +
                "WHERE qna_image_id = :qnaImage";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(qnaImages.size());
        for (QnaImage qnaImage : qnaImages) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("qnaImage", qnaImage.getId());
            parameterSource.addValue("displayOrder", qnaImage.getDisplayOrder());
            parameterSource.addValue("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue("imageUrl", qnaImage.getImageUrl());
            parameterSource.addValue("updateDate", LocalDateTime.now());
            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new MapSqlParameterSource[qnaImages.size()]));
    }

    @Override
    public void deleteAll(List<Long> qnaImageIds) {

        String sql = "UPDATE qna_image SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE qna_image_id IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", qnaImageIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());
        namedParameterJdbcTemplate.update(sql, params);
    }

}
