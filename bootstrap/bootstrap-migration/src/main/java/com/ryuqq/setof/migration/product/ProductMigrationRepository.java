package com.ryuqq.setof.migration.product;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 신규 상품 테이블 저장 Repository
 *
 * <p>신규 DB의 product_groups, products, product_stocks 테이블에 데이터를 저장합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ProductMigrationRepository {

    private final JdbcTemplate setofJdbcTemplate;

    public ProductMigrationRepository(
            @Qualifier("setofJdbcTemplate") JdbcTemplate setofJdbcTemplate) {
        this.setofJdbcTemplate = setofJdbcTemplate;
    }

    /**
     * 레거시 상품그룹 ID로 신규 상품그룹 존재 여부 확인
     *
     * @param legacyProductGroupId 레거시 상품그룹 ID
     * @return 존재하면 true
     */
    public boolean existsByLegacyProductGroupId(Long legacyProductGroupId) {
        String sql = "SELECT COUNT(*) FROM product_groups WHERE legacy_product_group_id = ?";
        Integer count = setofJdbcTemplate.queryForObject(sql, Integer.class, legacyProductGroupId);
        return count != null && count > 0;
    }

    /**
     * 레거시 상품 ID로 신규 상품 존재 여부 확인
     *
     * @param legacyProductId 레거시 상품 ID
     * @return 존재하면 true
     */
    public boolean existsByLegacyProductId(Long legacyProductId) {
        String sql = "SELECT COUNT(*) FROM products WHERE legacy_product_id = ?";
        Integer count = setofJdbcTemplate.queryForObject(sql, Integer.class, legacyProductId);
        return count != null && count > 0;
    }

    /**
     * 레거시 상품그룹 ID로 신규 상품그룹 ID 조회
     *
     * @param legacyProductGroupId 레거시 상품그룹 ID
     * @return 신규 상품그룹 ID (없으면 null)
     */
    public Long findProductGroupIdByLegacyId(Long legacyProductGroupId) {
        String sql = "SELECT id FROM product_groups WHERE legacy_product_group_id = ?";
        return setofJdbcTemplate.query(
                sql, rs -> rs.next() ? rs.getLong("id") : null, legacyProductGroupId);
    }

    /**
     * 상품그룹 직접 INSERT
     *
     * <p>Domain 레이어를 거치지 않고 직접 INSERT합니다.
     *
     * @param legacyProductGroup 레거시 상품그룹 정보
     * @return 생성된 상품그룹 ID
     */
    public Long insertProductGroupDirectly(LegacyProductGroupDto legacyProductGroup) {
        String sql =
                """
                INSERT INTO product_groups (
                    seller_id, category_id, brand_id,
                    shipping_policy_id, refund_policy_id,
                    name, option_type, regular_price, current_price, status,
                    legacy_product_group_id,
                    created_at, updated_at
                ) VALUES (?, ?, ?, NULL, NULL, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        String status =
                mapProductStatus(legacyProductGroup.soldOutYn(), legacyProductGroup.displayYn());

        setofJdbcTemplate.update(
                sql,
                legacyProductGroup.sellerId(),
                legacyProductGroup.categoryId(),
                legacyProductGroup.brandId(),
                legacyProductGroup.productGroupName(),
                legacyProductGroup.optionType(),
                legacyProductGroup.regularPrice(),
                legacyProductGroup.currentPrice(),
                status,
                legacyProductGroup.productGroupId(),
                now,
                now);

        return findProductGroupIdByLegacyId(legacyProductGroup.productGroupId());
    }

    /**
     * 상품그룹 UPDATE
     *
     * @param legacyProductGroup 레거시 상품그룹 정보
     */
    public void updateProductGroup(LegacyProductGroupDto legacyProductGroup) {
        String sql =
                """
                UPDATE product_groups SET
                    name = ?,
                    regular_price = ?,
                    current_price = ?,
                    status = ?,
                    updated_at = ?
                WHERE legacy_product_group_id = ?
                """;

        String status =
                mapProductStatus(legacyProductGroup.soldOutYn(), legacyProductGroup.displayYn());

        setofJdbcTemplate.update(
                sql,
                legacyProductGroup.productGroupName(),
                legacyProductGroup.regularPrice(),
                legacyProductGroup.currentPrice(),
                status,
                Timestamp.valueOf(LocalDateTime.now()),
                legacyProductGroup.productGroupId());
    }

    /**
     * 개별 상품 직접 INSERT
     *
     * @param productGroupId 신규 상품그룹 ID
     * @param legacyProduct 레거시 상품 정보
     * @return 생성된 상품 ID
     */
    public Long insertProductDirectly(Long productGroupId, LegacyProductDto legacyProduct) {
        String sql =
                """
                INSERT INTO products (
                    product_group_id, option_type,
                    option1_name, option1_value, option2_name, option2_value,
                    additional_price, sold_out, display_yn,
                    legacy_product_id,
                    created_at, updated_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        boolean soldOut = "Y".equals(legacyProduct.soldOutYn());
        boolean displayYn = "Y".equals(legacyProduct.displayYn());
        String optionType = determineOptionType(legacyProduct);

        setofJdbcTemplate.update(
                sql,
                productGroupId,
                optionType,
                legacyProduct.option1Name(),
                legacyProduct.option1Value(),
                legacyProduct.option2Name(),
                legacyProduct.option2Value(),
                legacyProduct.additionalPrice(),
                soldOut,
                displayYn,
                legacyProduct.productId(),
                now,
                now);

        return findProductIdByLegacyId(legacyProduct.productId());
    }

    /**
     * 개별 상품 UPDATE
     *
     * @param legacyProduct 레거시 상품 정보
     */
    public void updateProduct(LegacyProductDto legacyProduct) {
        String sql =
                """
                UPDATE products SET
                    sold_out = ?,
                    display_yn = ?,
                    updated_at = ?
                WHERE legacy_product_id = ?
                """;

        boolean soldOut = "Y".equals(legacyProduct.soldOutYn());
        boolean displayYn = "Y".equals(legacyProduct.displayYn());

        setofJdbcTemplate.update(
                sql,
                soldOut,
                displayYn,
                Timestamp.valueOf(LocalDateTime.now()),
                legacyProduct.productId());
    }

    /**
     * 레거시 상품 ID로 신규 상품 ID 조회
     *
     * @param legacyProductId 레거시 상품 ID
     * @return 신규 상품 ID (없으면 null)
     */
    public Long findProductIdByLegacyId(Long legacyProductId) {
        String sql = "SELECT id FROM products WHERE legacy_product_id = ?";
        return setofJdbcTemplate.query(
                sql, rs -> rs.next() ? rs.getLong("id") : null, legacyProductId);
    }

    /**
     * 재고 INSERT
     *
     * @param productId 신규 상품 ID
     * @param quantity 재고 수량
     */
    public void insertProductStock(Long productId, Integer quantity) {
        String sql =
                """
                INSERT INTO product_stocks (product_id, quantity, version, created_at, updated_at)
                VALUES (?, ?, 0, ?, ?)
                """;

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        setofJdbcTemplate.update(sql, productId, quantity, now, now);
    }

    /**
     * 재고 UPDATE
     *
     * @param productId 신규 상품 ID
     * @param quantity 재고 수량
     */
    public void updateProductStock(Long productId, Integer quantity) {
        String sql = "UPDATE product_stocks SET quantity = ?, updated_at = ? WHERE product_id = ?";
        setofJdbcTemplate.update(sql, quantity, Timestamp.valueOf(LocalDateTime.now()), productId);
    }

    /**
     * 재고 존재 여부 확인
     *
     * @param productId 신규 상품 ID
     * @return 존재하면 true
     */
    public boolean existsProductStock(Long productId) {
        String sql = "SELECT COUNT(*) FROM product_stocks WHERE product_id = ?";
        Integer count = setofJdbcTemplate.queryForObject(sql, Integer.class, productId);
        return count != null && count > 0;
    }

    private String mapProductStatus(String soldOutYn, String displayYn) {
        if ("Y".equals(soldOutYn)) {
            return "SOLD_OUT";
        }
        if ("N".equals(displayYn)) {
            return "HIDDEN";
        }
        return "SELLING";
    }

    private String determineOptionType(LegacyProductDto legacyProduct) {
        if (legacyProduct.option2Name() != null && !legacyProduct.option2Name().isEmpty()) {
            return "COMBINATION";
        }
        if (legacyProduct.option1Name() != null && !legacyProduct.option1Name().isEmpty()) {
            return "SINGLE";
        }
        return "NONE";
    }
}
