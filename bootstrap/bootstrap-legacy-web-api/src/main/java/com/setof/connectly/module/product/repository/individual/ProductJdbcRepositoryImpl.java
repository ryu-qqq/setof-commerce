package com.setof.connectly.module.product.repository.individual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductJdbcRepositoryImpl implements ProductJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void updateProductGroupSoldOutStatus(List<Long> productGroupIds) {

        String updateSql =
                "UPDATE product_group pg SET pg.sold_out_yn = 'Y', pg.update_date ="
                    + " CURRENT_TIMESTAMP WHERE pg.product_group_id IN (:productGroupIds) AND NOT"
                    + " EXISTS (SELECT 1 FROM product p WHERE p.product_group_id ="
                    + " pg.product_group_id AND p.sold_out_yn = 'N' AND p.delete_yn = 'N')";

        Map<String, Object> params = new HashMap<>();
        params.put("productGroupIds", productGroupIds);
        namedParameterJdbcTemplate.update(updateSql, params);
    }

    @Override
    public void updateProductGroupAvailableStatus(List<Long> productGroupIds) {
        String updateSql =
                "UPDATE product_group pg SET pg.sold_out_yn = 'N', pg.update_date ="
                        + " CURRENT_TIMESTAMP WHERE pg.product_group_id IN (:productGroupIds) AND"
                        + " EXISTS (SELECT 1 FROM product p WHERE p.product_group_id ="
                        + " pg.product_group_id AND p.sold_out_yn = 'N' AND p.delete_yn = 'N')";

        Map<String, Object> params = new HashMap<>();
        params.put("productGroupIds", productGroupIds);
        namedParameterJdbcTemplate.update(updateSql, params);
    }
}
