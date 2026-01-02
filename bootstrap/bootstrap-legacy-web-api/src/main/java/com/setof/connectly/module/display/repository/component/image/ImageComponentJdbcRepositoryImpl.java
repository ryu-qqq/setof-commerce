package com.setof.connectly.module.display.repository.component.image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageComponentJdbcRepositoryImpl implements ImageComponentJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void deleteAll(List<Long> imageComponentIds) {

        String sql =
                "UPDATE IMAGE_component SET delete_yn = 'Y' WHERE IMAGE_component_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", imageComponentIds);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
