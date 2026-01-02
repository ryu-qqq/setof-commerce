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
        String sql = "INSERT INTO BRAND_COMPONENT_ITEM (BRAND_ID, CATEGORY_ID, BRAND_COMPONENT_ID, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)";

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

        String sql = "UPDATE BRAND_COMPONENT_ITEM SET DELETE_YN = 'Y', UPDATE_OPERATOR = :updateOperator, UPDATE_DATE = :updateDate WHERE BRAND_COMPONENT_ID IN (:brandComponentIds) AND BRAND_ID IN (:brandIds)";

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
        String sql = "INSERT INTO BRAND_COMPONENT_ITEM (BRAND_ID, CATEGORY_ID, BRAND_COMPONENT_ID, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
        String sql = "UPDATE BRAND_COMPONENT_ITEM SET CATEGORY_ID = :categoryId WHERE BRAND_COMPONENT_ID = :brandComponentId AND BRAND_ID = :brandId";

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
