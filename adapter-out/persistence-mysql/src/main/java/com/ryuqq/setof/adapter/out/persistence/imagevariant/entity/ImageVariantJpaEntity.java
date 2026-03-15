package com.ryuqq.setof.adapter.out.persistence.imagevariant.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ImageVariantJpaEntity - 이미지 Variant JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지.
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "image_variants")
public class ImageVariantJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_image_id", nullable = false)
    private Long sourceImageId;

    @Column(name = "source_type", nullable = false, length = 30)
    private String sourceType;

    @Column(name = "variant_type", nullable = false, length = 30)
    private String variantType;

    @Column(name = "result_asset_id", nullable = false, length = 100)
    private String resultAssetId;

    @Column(name = "variant_url", nullable = false, length = 500)
    private String variantUrl;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    protected ImageVariantJpaEntity() {
        super();
    }

    private ImageVariantJpaEntity(
            Long id,
            Long sourceImageId,
            String sourceType,
            String variantType,
            String resultAssetId,
            String variantUrl,
            Integer width,
            Integer height,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.sourceImageId = sourceImageId;
        this.sourceType = sourceType;
        this.variantType = variantType;
        this.resultAssetId = resultAssetId;
        this.variantUrl = variantUrl;
        this.width = width;
        this.height = height;
    }

    public static ImageVariantJpaEntity create(
            Long id,
            Long sourceImageId,
            String sourceType,
            String variantType,
            String resultAssetId,
            String variantUrl,
            Integer width,
            Integer height,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ImageVariantJpaEntity(
                id,
                sourceImageId,
                sourceType,
                variantType,
                resultAssetId,
                variantUrl,
                width,
                height,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getSourceImageId() {
        return sourceImageId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getVariantType() {
        return variantType;
    }

    public String getResultAssetId() {
        return resultAssetId;
    }

    public String getVariantUrl() {
        return variantUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
