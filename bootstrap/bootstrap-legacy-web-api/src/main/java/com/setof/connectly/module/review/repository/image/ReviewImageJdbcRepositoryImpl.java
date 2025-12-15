package com.setof.connectly.module.review.repository.image;

import com.setof.connectly.module.review.entity.ReviewImage;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewImageJdbcRepositoryImpl implements ReviewImageJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<ReviewImage> reviewImages) {
        String sql =
                "INSERT INTO REVIEW_IMAGE (REVIEW_ID, REVIEW_IMAGE_TYPE, IMAGE_URL,"
                        + " INSERT_OPERATOR, UPDATE_OPERATOR) VALUES (:reviewId, :reviewImageType,"
                        + " :imageUrl, :insertOperator, :updateOperator)";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(reviewImages.size());
        for (ReviewImage reviewImage : reviewImages) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("reviewId", reviewImage.getReviewId());
            parameterSource.addValue("reviewImageType", reviewImage.getReviewImageType().name());
            parameterSource.addValue("imageUrl", reviewImage.getImageUrl());
            parameterSource.addValue(
                    "insertOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue(
                    "updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new MapSqlParameterSource[reviewImages.size()]));
    }
}
