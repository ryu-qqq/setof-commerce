package com.ryuqq.setof.adapter.out.persistence.brand.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * BrandJpaEntity - 브랜드 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Entity
@Table(name = "brands")
public class BrandJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_name", nullable = false, length = 100)
    private String brandName;

    @Column(name = "brand_icon_image_url", nullable = false, length = 500)
    private String brandIconImageUrl;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "displayed", nullable = false)
    private boolean displayed;

    protected BrandJpaEntity() {
        super();
    }

    private BrandJpaEntity(
            Long id,
            String brandName,
            String brandIconImageUrl,
            String displayName,
            int displayOrder,
            boolean displayed,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.brandName = brandName;
        this.brandIconImageUrl = brandIconImageUrl;
        this.displayName = displayName;
        this.displayOrder = displayOrder;
        this.displayed = displayed;
    }

    public static BrandJpaEntity create(
            Long id,
            String brandName,
            String brandIconImageUrl,
            String displayName,
            int displayOrder,
            boolean displayed,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new BrandJpaEntity(
                id,
                brandName,
                brandIconImageUrl,
                displayName,
                displayOrder,
                displayed,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getBrandIconImageUrl() {
        return brandIconImageUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public boolean isDisplayed() {
        return displayed;
    }
}
