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
                "UPDATE product_stock "
                        + "SET stock_quantity = stock_quantity - :stockQuantity, "
                        + "update_date = :updateDate "
                        + "WHERE product_id = :productId";

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
                "UPDATE product_stock "
                        + "SET stock_quantity = stock_quantity + :stockQuantity, "
                        + "update_date = :updateDate "
                        + "WHERE product_id = :productId";

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
                "UPDATE product p SET p.sold_out_yn = CASE WHEN (SELECT ps.stock_quantity FROM"
                    + " product_stock ps WHERE ps.product_id = p.product_id) > 0 THEN 'N' ELSE 'Y'"
                    + " END, p.update_date = CURRENT_TIMESTAMP WHERE p.product_id IN (:productIds)";

        List<Long> productIds =
                stocks.stream().map(StockDto::getProductId).collect(Collectors.toList());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("productIds", productIds);

        namedParameterJdbcTemplate.update(updateProductSoldOutSql, parameters);
    }
}
