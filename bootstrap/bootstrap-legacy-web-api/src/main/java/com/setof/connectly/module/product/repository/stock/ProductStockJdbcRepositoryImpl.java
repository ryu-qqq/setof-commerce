package com.setof.connectly.module.product.repository.stock;

import com.setof.connectly.module.product.dto.stock.StockDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStockJdbcRepositoryImpl implements ProductStockJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void subtractStocks(List<StockDto> stocks) {
        String sql =
                "UPDATE PRODUCT_STOCK "
                        + "SET STOCK_QUANTITY = STOCK_QUANTITY - :stockQuantity, "
                        + "UPDATE_DATE = :updateDate "
                        + "WHERE PRODUCT_ID = :productId";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(stocks.size());
        for (StockDto stock : stocks) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("stockQuantity", stock.getProductStockQuantity());
            parameterSource.addValue("productId", stock.getProductId());
            parameterSource.addValue("updateDate", LocalDateTime.now());
            batchValues.add(parameterSource);
        }
        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new MapSqlParameterSource[stocks.size()]));
        updateProductSoldOutStatus(stocks);
    }

    public void addStocks(List<StockDto> stocks) {
        String sql =
                "UPDATE PRODUCT_STOCK "
                        + "SET STOCK_QUANTITY = STOCK_QUANTITY + :stockQuantity, "
                        + "UPDATE_DATE = :updateDate "
                        + "WHERE PRODUCT_ID = :productId";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(stocks.size());
        for (StockDto stock : stocks) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("stockQuantity", stock.getProductStockQuantity());
            parameterSource.addValue("productId", stock.getProductId());
            parameterSource.addValue("updateDate", LocalDateTime.now());
            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new MapSqlParameterSource[stocks.size()]));
        updateProductSoldOutStatus(stocks);
    }

    private void updateProductSoldOutStatus(List<StockDto> stocks) {
        String updateProductSoldOutSql =
                "UPDATE PRODUCT p SET p.SOLD_OUT_YN = CASE WHEN (SELECT ps.STOCK_QUANTITY FROM"
                    + " PRODUCT_STOCK ps WHERE ps.PRODUCT_ID = p.PRODUCT_ID) > 0 THEN 'N' ELSE 'Y'"
                    + " END, p.UPDATE_DATE = CURRENT_TIMESTAMP WHERE p.PRODUCT_ID IN (:productIds)";

        List<Long> productIds =
                stocks.stream().map(StockDto::getProductId).collect(Collectors.toList());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("productIds", productIds);

        namedParameterJdbcTemplate.update(updateProductSoldOutSql, parameters);
    }
}
