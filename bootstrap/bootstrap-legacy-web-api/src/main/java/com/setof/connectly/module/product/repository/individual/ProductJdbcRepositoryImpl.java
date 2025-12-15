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
                "UPDATE PRODUCT_GROUP pg SET pg.SOLD_OUT_YN = 'Y', pg.UPDATE_DATE ="
                    + " CURRENT_TIMESTAMP WHERE pg.PRODUCT_GROUP_ID IN (:productGroupIds) AND NOT"
                    + " EXISTS (SELECT 1 FROM PRODUCT p WHERE p.PRODUCT_GROUP_ID ="
                    + " pg.PRODUCT_GROUP_ID AND p.SOLD_OUT_YN = 'N' AND p.DELETE_YN = 'N')";

        Map<String, Object> params = new HashMap<>();
        params.put("productGroupIds", productGroupIds);
        namedParameterJdbcTemplate.update(updateSql, params);
    }

    @Override
    public void updateProductGroupAvailableStatus(List<Long> productGroupIds) {
        String updateSql =
                "UPDATE PRODUCT_GROUP pg SET pg.SOLD_OUT_YN = 'N', pg.UPDATE_DATE ="
                        + " CURRENT_TIMESTAMP WHERE pg.PRODUCT_GROUP_ID IN (:productGroupIds) AND"
                        + " EXISTS (SELECT 1 FROM PRODUCT p WHERE p.PRODUCT_GROUP_ID ="
                        + " pg.PRODUCT_GROUP_ID AND p.SOLD_OUT_YN = 'N' AND p.DELETE_YN = 'N')";

        Map<String, Object> params = new HashMap<>();
        params.put("productGroupIds", productGroupIds);
        namedParameterJdbcTemplate.update(updateSql, params);
    }
}
