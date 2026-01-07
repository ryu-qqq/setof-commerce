package com.connectly.partnerAdmin.module.display.repository.component.brand;


import com.connectly.partnerAdmin.module.display.entity.component.item.ImageComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.BrandComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BrandComponentJdbcRepositoryImpl implements BrandComponentJdbcRepository{

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;



    @Override
    public void saveAll(List<BrandComponent> brandComponents) {
        String sql = "INSERT INTO brand_component (component_id) VALUES (?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (BrandComponent brandComponent : brandComponents) {
            Object[] values = new Object[] {
                    brandComponent.getComponent().getId(),
            };
            batchArgs.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void deleteAll(List<Long> brandComponentIds) {
        String sql = "UPDATE brand_component SET delete_yn = 'Y' WHERE brand_component_id IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", brandComponentIds);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
