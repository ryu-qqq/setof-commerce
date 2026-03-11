package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductGroupDescriptionJpaEntity - 상품그룹 상세설명 JPA 엔티티.
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
@Table(name = "product_group_descriptions")
public class ProductGroupDescriptionJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "cdn_path", length = 1000)
    private String cdnPath;

    protected ProductGroupDescriptionJpaEntity() {
        super();
    }

    private ProductGroupDescriptionJpaEntity(
            Long id,
            Long productGroupId,
            String content,
            String cdnPath,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.content = content;
        this.cdnPath = cdnPath;
    }

    public static ProductGroupDescriptionJpaEntity create(
            Long id,
            Long productGroupId,
            String content,
            String cdnPath,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ProductGroupDescriptionJpaEntity(
                id, productGroupId, content, cdnPath, createdAt, updatedAt, deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getContent() {
        return content;
    }

    public String getCdnPath() {
        return cdnPath;
    }
}
