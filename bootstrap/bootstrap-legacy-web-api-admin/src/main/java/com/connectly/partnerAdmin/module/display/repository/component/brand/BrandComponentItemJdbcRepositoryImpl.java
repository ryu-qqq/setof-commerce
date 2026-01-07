package com.connectly.partnerAdmin.module.display.repository.component.brand;


import com.connectly.partnerAdmin.module.display.dto.component.brand.BrandComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.item.BrandComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.item.ImageComponentItem;
import com.connectly.partnerAdmin.module.display.entity.component.sub.product.BrandComponent;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class BrandComponentItemJdbcRepositoryImpl implements BrandComponentItemJdbcRepository{

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void saveAll(List<BrandComponentItem> brandComponentItems) {
        String sql = "INSERT INTO brand_component_item (brand_id, category_id, brand_component_id, insert_operator, update_operator, insert_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (BrandComponentItem brandComponentItem : brandComponentItems) {
            Object[] values = new Object[] {
                    brandComponentItem.getBrandId(),
                    brandComponentItem.getCategoryId(),
                    brandComponentItem.getBrandComponentId(),
                    MDC.get("user") ==null ? "Anonymous" : MDC.get("user"),
                    MDC.get("user") ==null ? "Anonymous" : MDC.get("user"),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            };
            batchArgs.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void deleteAll(List<BrandComponentDetail> deleteBrandComponentItems) {
        if(deleteBrandComponentItems.isEmpty()) return;

        String sql = "UPDATE brand_component_item SET delete_yn = 'Y', update_operator = :updateOperator, update_date = :updateDate WHERE brand_component_id IN (:brandComponentIds) AND brand_id IN (:brandIds)";

        Map<String, Object> params = new HashMap<>();

        List<Long> brandIds = deleteBrandComponentItems.stream()
                .flatMap(brandComponentDetail -> brandComponentDetail.getBrandIdList().stream())
                .collect(Collectors.toList());

        List<Long> brandComponentIds = deleteBrandComponentItems.stream()
                .map(BrandComponentDetail::getBrandComponentId)
                .collect(Collectors.toList());

        if (brandIds.isEmpty() || brandComponentIds.isEmpty()) {
            return;
        }

        params.put("brandComponentIds", brandComponentIds);
        params.put("brandIds", brandIds);
        params.put("updateOperator", MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        params.put("updateDate", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void addAll(List<BrandComponentDetail> toAddBrandComponentItems) {
        String sql = "INSERT INTO brand_component_item (brand_id, category_id, brand_component_id, insert_operator, update_operator, insert_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (BrandComponentDetail BrandComponentDetail : toAddBrandComponentItems) {
            for(Long brandId : BrandComponentDetail.getBrandIdList()){
                Object[] values = new Object[] {
                        brandId,
                        BrandComponentDetail.getCategoryId(),
                        BrandComponentDetail.getBrandComponentId(),
                        MDC.get("user") ==null ? "Anonymous" : MDC.get("user"),
                        MDC.get("user") ==null ? "Anonymous" : MDC.get("user"),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                };
                batchArgs.add(values);
            }
        }
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void updateCategoryIdAll(List<BrandComponentDetail> toUpdateBrandComponentItems) {
        String sql = "UPDATE brand_component_item SET category_id = :categoryId WHERE brand_component_id = :brandComponentId AND brand_id = :brandId";

        List<Map<String, Object>> batchValues = new ArrayList<>(toUpdateBrandComponentItems.size());
        for (BrandComponentDetail brandComponentDetail : toUpdateBrandComponentItems) {
            for(Long brandId : brandComponentDetail.getBrandIdList()){
                batchValues.add(
                        new MapSqlParameterSource("categoryId", brandComponentDetail.getCategoryId())
                                .addValue("brandComponentId", brandComponentDetail.getBrandComponentId())
                                .addValue("brandId", brandId)
                                .getValues());
            }
        }
        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[toUpdateBrandComponentItems.size()]));
    }
}
