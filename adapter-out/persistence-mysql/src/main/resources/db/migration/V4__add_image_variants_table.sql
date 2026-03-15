CREATE TABLE IF NOT EXISTS image_variants (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    source_image_id BIGINT          NOT NULL,
    source_type     VARCHAR(30)     NOT NULL,
    variant_type    VARCHAR(30)     NOT NULL,
    result_asset_id VARCHAR(100)    NOT NULL,
    variant_url     VARCHAR(500)    NOT NULL,
    width           INT             NULL,
    height          INT             NULL,
    created_at      DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at      DATETIME(6)     NULL,
    PRIMARY KEY (id),
    INDEX idx_image_variants_source (source_image_id, deleted_at),
    INDEX idx_image_variants_type (source_image_id, variant_type, deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
