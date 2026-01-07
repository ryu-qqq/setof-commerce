package com.ryuqq.setof.migration.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 이미지 마이그레이션 Repository
 *
 * <p>레거시 DB의 이미지 URL을 새 CDN URL로 업데이트합니다.
 *
 * <p><strong>주의:</strong> 이 Repository는 예외적으로 레거시 DB에 쓰기 작업을 수행합니다. 이미지 URL 마이그레이션 용도로만 사용해야 합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ImageMigrationRepository {

    private static final Logger log = LoggerFactory.getLogger(ImageMigrationRepository.class);

    private final JdbcTemplate legacyJdbcTemplate;

    public ImageMigrationRepository(
            @Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate) {
        this.legacyJdbcTemplate = legacyJdbcTemplate;
    }

    /**
     * product_group_image 테이블의 이미지 URL 업데이트
     *
     * @param imageId 이미지 ID
     * @param newUrl 새 CDN URL
     * @return 업데이트된 행 수
     */
    public int updateProductGroupImageUrl(Long imageId, String newUrl) {
        String sql =
                """
                UPDATE product_group_image
                SET IMAGE_URL = ?,
                    UPDATED_AT = NOW()
                WHERE PRODUCT_GROUP_IMAGE_ID = ?
                """;

        int updatedCount = legacyJdbcTemplate.update(sql, newUrl, imageId);

        if (updatedCount > 0) {
            log.debug("product_group_image URL 업데이트 성공: imageId={}, newUrl={}", imageId, newUrl);
        } else {
            log.warn("product_group_image URL 업데이트 실패: imageId={}", imageId);
        }

        return updatedCount;
    }

    /**
     * product_group_detail_description 테이블의 이미지 URL 업데이트
     *
     * @param productGroupId 상품그룹 ID
     * @param newUrl 새 CDN URL
     * @return 업데이트된 행 수
     */
    public int updateDetailDescriptionUrl(Long productGroupId, String newUrl) {
        String sql =
                """
                UPDATE product_group_detail_description
                SET IMAGE_URL = ?,
                    UPDATED_AT = NOW()
                WHERE PRODUCT_GROUP_ID = ?
                """;

        int updatedCount = legacyJdbcTemplate.update(sql, newUrl, productGroupId);

        if (updatedCount > 0) {
            log.debug(
                    "product_group_detail_description URL 업데이트 성공: productGroupId={}, newUrl={}",
                    productGroupId,
                    newUrl);
        } else {
            log.warn(
                    "product_group_detail_description URL 업데이트 실패: productGroupId={}",
                    productGroupId);
        }

        return updatedCount;
    }

    /**
     * LegacyImageDto 기반으로 URL 업데이트
     *
     * @param image 이미지 정보
     * @param newUrl 새 CDN URL
     * @return 업데이트된 행 수
     */
    public int updateImageUrl(LegacyImageDto image, String newUrl) {
        return switch (image.sourceType()) {
            case IMAGE -> updateProductGroupImageUrl(image.id(), newUrl);
            case DESCRIPTION -> updateDetailDescriptionUrl(image.productGroupId(), newUrl);
        };
    }

    /**
     * 마이그레이션 완료 상태 기록 (선택적)
     *
     * <p>마이그레이션 이력 테이블에 완료 상태를 기록합니다. 테이블이 없으면 무시합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @param imageCount 마이그레이션된 이미지 수
     */
    public void recordMigrationComplete(Long productGroupId, int imageCount) {
        try {
            // 마이그레이션 이력 테이블이 있는 경우에만 기록
            String checkTableSql =
                    """
                    SELECT COUNT(*) FROM information_schema.tables
                    WHERE table_schema = DATABASE()
                      AND table_name = 'image_migration_history'
                    """;

            Integer tableExists = legacyJdbcTemplate.queryForObject(checkTableSql, Integer.class);
            if (tableExists == null || tableExists == 0) {
                return; // 테이블이 없으면 무시
            }

            String sql =
                    """
INSERT INTO image_migration_history (product_group_id, image_count, migrated_at)
VALUES (?, ?, NOW())
ON DUPLICATE KEY UPDATE
    image_count = VALUES(image_count),
    migrated_at = NOW()
""";

            legacyJdbcTemplate.update(sql, productGroupId, imageCount);

        } catch (Exception e) {
            log.debug(
                    "마이그레이션 이력 기록 실패 (무시): productGroupId={}, error={}",
                    productGroupId,
                    e.getMessage());
        }
    }
}
