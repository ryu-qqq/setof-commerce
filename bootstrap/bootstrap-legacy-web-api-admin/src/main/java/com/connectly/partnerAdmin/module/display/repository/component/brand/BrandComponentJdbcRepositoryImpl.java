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
        String sql = "INSERT INTO BRAND_COMPONENT (COMPONENT_ID) VALUES (?, ?, ?)";

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
        String sql = "UPDATE BRAND_COMPONENT SET DELETE_YN = 'Y' WHERE BRAND_COMPONENT_ID IN (:ids)";

        Map<String, Object> params = new HashMap<>();
        params.put("ids", brandComponentIds);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
