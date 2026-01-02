package com.ryuqq.setof.migration.product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 레거시 상품 테이블 조회 Repository
 *
 * <p>레거시 DB의 PRODUCT_GROUP, PRODUCT, PRODUCT_STOCK 테이블에서 데이터를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class LegacyProductRepository {

    private final JdbcTemplate legacyJdbcTemplate;

    public LegacyProductRepository(
            @Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate) {
        this.legacyJdbcTemplate = legacyJdbcTemplate;
    }

    /**
     * 마이그레이션 대상 상품그룹 목록 조회 (커서 기반 페이징)
     *
     * <p>DELETE_YN = 'N' 조건으로 삭제되지 않은 상품그룹만 조회합니다. OFFSET 대신 커서 기반 페이징을 사용하여 대용량 데이터에서도 성능을 보장합니다.
     *
     * @param lastProductGroupId 마지막으로 처리한 상품그룹 ID (첫 조회 시 0)
     * @param limit 조회 개수
     * @return 레거시 상품그룹 목록
     */
    public List<LegacyProductGroupDto> findProductGroupsForMigration(
            long lastProductGroupId, int limit) {
        String sql =
                """
                SELECT
                    PRODUCT_GROUP_ID,
                    PRODUCT_GROUP_NAME,
                    SELLER_ID,
                    BRAND_ID,
                    CATEGORY_ID,
                    OPTION_TYPE,
                    REGULAR_PRICE,
                    CURRENT_PRICE,
                    SOLD_OUT_YN,
                    DISPLAY_YN,
                    CREATED_AT,
                    UPDATED_AT
                FROM PRODUCT_GROUP
                WHERE DELETE_YN = 'N'
                  AND PRODUCT_GROUP_ID > ?
                ORDER BY PRODUCT_GROUP_ID
                LIMIT ?
                """;

        return legacyJdbcTemplate.query(
                sql, new ProductGroupRowMapper(), lastProductGroupId, limit);
    }

    /**
     * 특정 시간 이후 수정된 상품그룹 목록 조회 (증분 동기화용)
     *
     * <p>DELETE_YN = 'N' 조건으로 삭제되지 않은 상품그룹만 조회합니다.
     *
     * @param modifiedAfter 이 시간 이후 수정된 상품그룹만 조회
     * @param limit 최대 조회 개수
     * @return 수정된 상품그룹 목록
     */
    public List<LegacyProductGroupDto> findProductGroupsModifiedAfter(
            LocalDateTime modifiedAfter, int limit) {
        String sql =
                """
                SELECT
                    PRODUCT_GROUP_ID,
                    PRODUCT_GROUP_NAME,
                    SELLER_ID,
                    BRAND_ID,
                    CATEGORY_ID,
                    OPTION_TYPE,
                    REGULAR_PRICE,
                    CURRENT_PRICE,
                    SOLD_OUT_YN,
                    DISPLAY_YN,
                    CREATED_AT,
                    UPDATED_AT
                FROM PRODUCT_GROUP
                WHERE DELETE_YN = 'N'
                  AND UPDATED_AT > ?
                ORDER BY UPDATED_AT
                LIMIT ?
                """;

        return legacyJdbcTemplate.query(sql, new ProductGroupRowMapper(), modifiedAfter, limit);
    }

    /**
     * 전체 상품그룹 수 조회
     *
     * <p>DELETE_YN = 'N' 조건으로 삭제되지 않은 상품그룹만 카운트합니다.
     *
     * @return 전체 상품그룹 수
     */
    public long countAllProductGroups() {
        String sql = "SELECT COUNT(*) FROM PRODUCT_GROUP WHERE DELETE_YN = 'N'";
        Long count = legacyJdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    /**
     * 특정 상품그룹의 개별 상품(SKU) 목록 조회
     *
     * <p>PRODUCT, PRODUCT_STOCK, PRODUCT_OPTION, OPTION_GROUP, OPTION_DETAIL을 조인하여 조회합니다. DELETE_YN
     * = 'N' 조건으로 삭제되지 않은 상품만 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 개별 상품 목록
     */
    public List<LegacyProductDto> findProductsByProductGroupId(Long productGroupId) {
        String sql =
                """
SELECT
    p.PRODUCT_ID,
    p.PRODUCT_GROUP_ID,
    p.SOLD_OUT_YN,
    p.DISPLAY_YN,
    COALESCE(po.ADDITIONAL_PRICE, 0) AS ADDITIONAL_PRICE,
    og1.OPTION_NAME AS OPTION1_NAME,
    od1.OPTION_VALUE AS OPTION1_VALUE,
    og2.OPTION_NAME AS OPTION2_NAME,
    od2.OPTION_VALUE AS OPTION2_VALUE,
    COALESCE(ps.STOCK_QUANTITY, 0) AS STOCK_QUANTITY,
    p.CREATED_AT,
    p.UPDATED_AT
FROM PRODUCT p
LEFT JOIN PRODUCT_STOCK ps ON p.PRODUCT_ID = ps.PRODUCT_ID AND ps.DELETE_YN = 'N'
LEFT JOIN (
    SELECT
        po1.PRODUCT_ID,
        po1.ADDITIONAL_PRICE,
        po1.OPTION_GROUP_ID AS OG1_ID,
        po1.OPTION_DETAIL_ID AS OD1_ID,
        po2.OPTION_GROUP_ID AS OG2_ID,
        po2.OPTION_DETAIL_ID AS OD2_ID
    FROM PRODUCT_OPTION po1
    LEFT JOIN PRODUCT_OPTION po2
        ON po1.PRODUCT_ID = po2.PRODUCT_ID
        AND po1.OPTION_GROUP_ID < po2.OPTION_GROUP_ID
        AND po2.DELETE_YN = 'N'
    WHERE po1.DELETE_YN = 'N'
      AND po1.PRODUCT_OPTION_ID = (
        SELECT MIN(PRODUCT_OPTION_ID)
        FROM PRODUCT_OPTION
        WHERE PRODUCT_ID = po1.PRODUCT_ID AND DELETE_YN = 'N'
    )
) po ON p.PRODUCT_ID = po.PRODUCT_ID
LEFT JOIN OPTION_GROUP og1 ON po.OG1_ID = og1.OPTION_GROUP_ID AND og1.DELETE_YN = 'N'
LEFT JOIN OPTION_DETAIL od1 ON po.OD1_ID = od1.OPTION_DETAIL_ID AND od1.DELETE_YN = 'N'
LEFT JOIN OPTION_GROUP og2 ON po.OG2_ID = og2.OPTION_GROUP_ID AND og2.DELETE_YN = 'N'
LEFT JOIN OPTION_DETAIL od2 ON po.OD2_ID = od2.OPTION_DETAIL_ID AND od2.DELETE_YN = 'N'
WHERE p.PRODUCT_GROUP_ID = ?
  AND p.DELETE_YN = 'N'
ORDER BY p.PRODUCT_ID
""";

        return legacyJdbcTemplate.query(sql, new ProductRowMapper(), productGroupId);
    }

    /**
     * 특정 시간 이후 수정된 개별 상품 목록 조회 (증분 동기화용)
     *
     * <p>DELETE_YN = 'N' 조건으로 삭제되지 않은 상품만 조회합니다.
     *
     * @param modifiedAfter 이 시간 이후 수정된 상품만 조회
     * @param limit 최대 조회 개수
     * @return 수정된 상품 목록
     */
    public List<LegacyProductDto> findProductsModifiedAfter(
            LocalDateTime modifiedAfter, int limit) {
        String sql =
                """
SELECT
    p.PRODUCT_ID,
    p.PRODUCT_GROUP_ID,
    p.SOLD_OUT_YN,
    p.DISPLAY_YN,
    COALESCE(po.ADDITIONAL_PRICE, 0) AS ADDITIONAL_PRICE,
    og1.OPTION_NAME AS OPTION1_NAME,
    od1.OPTION_VALUE AS OPTION1_VALUE,
    og2.OPTION_NAME AS OPTION2_NAME,
    od2.OPTION_VALUE AS OPTION2_VALUE,
    COALESCE(ps.STOCK_QUANTITY, 0) AS STOCK_QUANTITY,
    p.CREATED_AT,
    p.UPDATED_AT
FROM PRODUCT p
LEFT JOIN PRODUCT_STOCK ps ON p.PRODUCT_ID = ps.PRODUCT_ID AND ps.DELETE_YN = 'N'
LEFT JOIN (
    SELECT
        po1.PRODUCT_ID,
        po1.ADDITIONAL_PRICE,
        po1.OPTION_GROUP_ID AS OG1_ID,
        po1.OPTION_DETAIL_ID AS OD1_ID,
        po2.OPTION_GROUP_ID AS OG2_ID,
        po2.OPTION_DETAIL_ID AS OD2_ID
    FROM PRODUCT_OPTION po1
    LEFT JOIN PRODUCT_OPTION po2
        ON po1.PRODUCT_ID = po2.PRODUCT_ID
        AND po1.OPTION_GROUP_ID < po2.OPTION_GROUP_ID
        AND po2.DELETE_YN = 'N'
    WHERE po1.DELETE_YN = 'N'
      AND po1.PRODUCT_OPTION_ID = (
        SELECT MIN(PRODUCT_OPTION_ID)
        FROM PRODUCT_OPTION
        WHERE PRODUCT_ID = po1.PRODUCT_ID AND DELETE_YN = 'N'
    )
) po ON p.PRODUCT_ID = po.PRODUCT_ID
LEFT JOIN OPTION_GROUP og1 ON po.OG1_ID = og1.OPTION_GROUP_ID AND og1.DELETE_YN = 'N'
LEFT JOIN OPTION_DETAIL od1 ON po.OD1_ID = od1.OPTION_DETAIL_ID AND od1.DELETE_YN = 'N'
LEFT JOIN OPTION_GROUP og2 ON po.OG2_ID = og2.OPTION_GROUP_ID AND og2.DELETE_YN = 'N'
LEFT JOIN OPTION_DETAIL od2 ON po.OD2_ID = od2.OPTION_DETAIL_ID AND od2.DELETE_YN = 'N'
WHERE p.DELETE_YN = 'N'
  AND (p.UPDATED_AT > ? OR ps.UPDATED_AT > ?)
ORDER BY p.UPDATED_AT
LIMIT ?
""";

        return legacyJdbcTemplate.query(
                sql, new ProductRowMapper(), modifiedAfter, modifiedAfter, limit);
    }

    private static class ProductGroupRowMapper implements RowMapper<LegacyProductGroupDto> {
        @Override
        public LegacyProductGroupDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LegacyProductGroupDto(
                    rs.getLong("PRODUCT_GROUP_ID"),
                    rs.getString("PRODUCT_GROUP_NAME"),
                    rs.getLong("SELLER_ID"),
                    rs.getLong("BRAND_ID"),
                    rs.getLong("CATEGORY_ID"),
                    rs.getString("OPTION_TYPE"),
                    rs.getBigDecimal("REGULAR_PRICE"),
                    rs.getBigDecimal("CURRENT_PRICE"),
                    rs.getString("SOLD_OUT_YN"),
                    rs.getString("DISPLAY_YN"),
                    getLocalDateTime(rs, "CREATED_AT"),
                    getLocalDateTime(rs, "UPDATED_AT"));
        }

        private LocalDateTime getLocalDateTime(ResultSet rs, String columnName)
                throws SQLException {
            java.sql.Timestamp timestamp = rs.getTimestamp(columnName);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }

    private static class ProductRowMapper implements RowMapper<LegacyProductDto> {
        @Override
        public LegacyProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LegacyProductDto(
                    rs.getLong("PRODUCT_ID"),
                    rs.getLong("PRODUCT_GROUP_ID"),
                    rs.getString("SOLD_OUT_YN"),
                    rs.getString("DISPLAY_YN"),
                    getBigDecimal(rs, "ADDITIONAL_PRICE"),
                    rs.getString("OPTION1_NAME"),
                    rs.getString("OPTION1_VALUE"),
                    rs.getString("OPTION2_NAME"),
                    rs.getString("OPTION2_VALUE"),
                    rs.getInt("STOCK_QUANTITY"),
                    getLocalDateTime(rs, "CREATED_AT"),
                    getLocalDateTime(rs, "UPDATED_AT"));
        }

        private BigDecimal getBigDecimal(ResultSet rs, String columnName) throws SQLException {
            BigDecimal value = rs.getBigDecimal(columnName);
            return value != null ? value : BigDecimal.ZERO;
        }

        private LocalDateTime getLocalDateTime(ResultSet rs, String columnName)
                throws SQLException {
            java.sql.Timestamp timestamp = rs.getTimestamp(columnName);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
