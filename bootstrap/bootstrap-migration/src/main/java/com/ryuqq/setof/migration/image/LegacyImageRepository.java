package com.ryuqq.setof.migration.image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 레거시 이미지 테이블 조회 Repository
 *
 * <p>레거시 DB의 product_group_image, product_group_detail_description 테이블에서 이미지 데이터를 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class LegacyImageRepository {

    private static final String CLOUDFRONT_DOMAIN = "d3fej89xf1vai5.cloudfront.net";

    private final JdbcTemplate legacyJdbcTemplate;

    public LegacyImageRepository(@Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate) {
        this.legacyJdbcTemplate = legacyJdbcTemplate;
    }

    /**
     * 마이그레이션 대상 product_group ID 목록 조회 (커서 기반 페이징)
     *
     * <p>삭제되지 않은 상품그룹 중 CloudFront 이미지를 가진 것만 조회합니다.
     *
     * @param lastProductGroupId 마지막으로 처리한 상품그룹 ID (첫 조회 시 0)
     * @param limit 조회 개수
     * @return 상품그룹 ID 목록
     */
    public List<Long> findProductGroupIdsForMigration(long lastProductGroupId, int limit) {
        String sql =
                """
                SELECT DISTINCT pg.PRODUCT_GROUP_ID
                FROM product_group pg
                WHERE pg.DELETE_YN = 'N'
                  AND pg.PRODUCT_GROUP_ID > ?
                  AND (
                      EXISTS (
                          SELECT 1 FROM product_group_image pgi
                          WHERE pgi.PRODUCT_GROUP_ID = pg.PRODUCT_GROUP_ID
                            AND pgi.DELETE_YN = 'N'
                            AND pgi.IMAGE_URL LIKE ?
                      )
                      OR EXISTS (
                          SELECT 1 FROM product_group_detail_description pgdd
                          WHERE pgdd.PRODUCT_GROUP_ID = pg.PRODUCT_GROUP_ID
                            AND pgdd.DELETE_YN = 'N'
                            AND pgdd.IMAGE_URL LIKE ?
                      )
                  )
                ORDER BY pg.PRODUCT_GROUP_ID
                LIMIT ?
                """;

        String cloudFrontPattern = "%" + CLOUDFRONT_DOMAIN + "%";
        return legacyJdbcTemplate.queryForList(
                sql, Long.class, lastProductGroupId, cloudFrontPattern, cloudFrontPattern, limit);
    }

    /**
     * 특정 상품그룹의 product_group_image 이미지 목록 조회
     *
     * <p>CloudFront URL을 가진 이미지만 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 이미지 목록
     */
    public List<LegacyImageDto> findImagesByProductGroupId(Long productGroupId) {
        String sql =
                """
                SELECT
                    PRODUCT_GROUP_IMAGE_ID,
                    PRODUCT_GROUP_ID,
                    PRODUCT_GROUP_IMAGE_TYPE,
                    IMAGE_URL
                FROM product_group_image
                WHERE PRODUCT_GROUP_ID = ?
                  AND DELETE_YN = 'N'
                  AND IMAGE_URL LIKE ?
                ORDER BY PRODUCT_GROUP_IMAGE_ID
                """;

        String cloudFrontPattern = "%" + CLOUDFRONT_DOMAIN + "%";
        return legacyJdbcTemplate.query(
                sql, new ProductGroupImageRowMapper(), productGroupId, cloudFrontPattern);
    }

    /**
     * 특정 상품그룹의 product_group_detail_description 이미지 조회
     *
     * <p>CloudFront URL을 가진 이미지만 조회합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return 이미지 (없으면 null)
     */
    public LegacyImageDto findDetailDescriptionByProductGroupId(Long productGroupId) {
        String sql =
                """
                SELECT
                    PRODUCT_GROUP_ID,
                    PRODUCT_GROUP_IMAGE_TYPE,
                    IMAGE_URL
                FROM product_group_detail_description
                WHERE PRODUCT_GROUP_ID = ?
                  AND DELETE_YN = 'N'
                  AND IMAGE_URL LIKE ?
                """;

        String cloudFrontPattern = "%" + CLOUDFRONT_DOMAIN + "%";
        List<LegacyImageDto> results =
                legacyJdbcTemplate.query(
                        sql, new DetailDescriptionRowMapper(), productGroupId, cloudFrontPattern);

        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * 특정 상품그룹의 모든 이미지 조회 (product_group_image + detail_description)
     *
     * @param productGroupId 상품그룹 ID
     * @return 모든 이미지 목록
     */
    public List<LegacyImageDto> findAllImagesByProductGroupId(Long productGroupId) {
        List<LegacyImageDto> allImages = new ArrayList<>();

        // product_group_image 조회
        allImages.addAll(findImagesByProductGroupId(productGroupId));

        // product_group_detail_description 조회
        LegacyImageDto detailDescription = findDetailDescriptionByProductGroupId(productGroupId);
        if (detailDescription != null) {
            allImages.add(detailDescription);
        }

        return allImages;
    }

    /**
     * 전체 마이그레이션 대상 이미지 수 조회
     *
     * @return 전체 이미지 수
     */
    public long countAllMigrationTargetImages() {
        String cloudFrontPattern = "%" + CLOUDFRONT_DOMAIN + "%";

        String sqlImages =
                """
                SELECT COUNT(*)
                FROM product_group_image pgi
                JOIN product_group pg ON pgi.PRODUCT_GROUP_ID = pg.PRODUCT_GROUP_ID
                WHERE pgi.DELETE_YN = 'N'
                  AND pg.DELETE_YN = 'N'
                  AND pgi.IMAGE_URL LIKE ?
                """;

        String sqlDescriptions =
                """
                SELECT COUNT(*)
                FROM product_group_detail_description pgdd
                JOIN product_group pg ON pgdd.PRODUCT_GROUP_ID = pg.PRODUCT_GROUP_ID
                WHERE pgdd.DELETE_YN = 'N'
                  AND pg.DELETE_YN = 'N'
                  AND pgdd.IMAGE_URL LIKE ?
                """;

        Long imageCount =
                legacyJdbcTemplate.queryForObject(sqlImages, Long.class, cloudFrontPattern);
        Long descriptionCount =
                legacyJdbcTemplate.queryForObject(sqlDescriptions, Long.class, cloudFrontPattern);

        return (imageCount != null ? imageCount : 0L)
                + (descriptionCount != null ? descriptionCount : 0L);
    }

    /**
     * 전체 마이그레이션 대상 상품그룹 수 조회
     *
     * @return 전체 상품그룹 수
     */
    public long countAllMigrationTargetProductGroups() {
        String sql =
                """
                SELECT COUNT(DISTINCT pg.PRODUCT_GROUP_ID)
                FROM product_group pg
                WHERE pg.DELETE_YN = 'N'
                  AND (
                      EXISTS (
                          SELECT 1 FROM product_group_image pgi
                          WHERE pgi.PRODUCT_GROUP_ID = pg.PRODUCT_GROUP_ID
                            AND pgi.DELETE_YN = 'N'
                            AND pgi.IMAGE_URL LIKE ?
                      )
                      OR EXISTS (
                          SELECT 1 FROM product_group_detail_description pgdd
                          WHERE pgdd.PRODUCT_GROUP_ID = pg.PRODUCT_GROUP_ID
                            AND pgdd.DELETE_YN = 'N'
                            AND pgdd.IMAGE_URL LIKE ?
                      )
                  )
                """;

        String cloudFrontPattern = "%" + CLOUDFRONT_DOMAIN + "%";
        Long count =
                legacyJdbcTemplate.queryForObject(
                        sql, Long.class, cloudFrontPattern, cloudFrontPattern);
        return count != null ? count : 0L;
    }

    private static class ProductGroupImageRowMapper implements RowMapper<LegacyImageDto> {
        @Override
        public LegacyImageDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return LegacyImageDto.ofImage(
                    rs.getLong("PRODUCT_GROUP_IMAGE_ID"),
                    rs.getLong("PRODUCT_GROUP_ID"),
                    rs.getString("PRODUCT_GROUP_IMAGE_TYPE"),
                    rs.getString("IMAGE_URL"));
        }
    }

    private static class DetailDescriptionRowMapper implements RowMapper<LegacyImageDto> {
        @Override
        public LegacyImageDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return LegacyImageDto.ofDescription(
                    rs.getLong("PRODUCT_GROUP_ID"),
                    rs.getString("PRODUCT_GROUP_IMAGE_TYPE"),
                    rs.getString("IMAGE_URL"));
        }
    }
}
