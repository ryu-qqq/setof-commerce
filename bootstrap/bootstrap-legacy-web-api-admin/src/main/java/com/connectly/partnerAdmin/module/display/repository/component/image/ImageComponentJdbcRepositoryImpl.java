package com.connectly.partnerAdmin.module.display.repository.component.image;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ImageComponentJdbcRepositoryImpl implements ImageComponentJdbcRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void deleteAll(List<Long> imageComponentIds) {

        String sql = "UPDATE image_component SET delete_yn = 'Y' WHERE image_component_id IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", imageComponentIds);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
