package com.ryuqq.setof.adapter.out.persistence.productgroup.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.Instant;
import org.springframework.data.domain.Persistable;

/**
 * ProductGroupJpaEntity - 상품 그룹 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: Persistable 구현으로 persist/merge 제어 (레거시 PK 동기화 지원).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Entity
@Table(name = "product_groups")
public class ProductGroupJpaEntity extends SoftDeletableEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient private boolean isNew = false;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "shipping_policy_id", nullable = false)
    private Long shippingPolicyId;

    @Column(name = "refund_policy_id", nullable = false)
    private Long refundPolicyId;

    @Column(name = "product_group_name", nullable = false, length = 500)
    private String productGroupName;

    @Column(name = "option_type", nullable = false, length = 50)
    private String optionType;

    @Column(name = "regular_price", nullable = false)
    private int regularPrice;

    @Column(name = "current_price", nullable = false)
    private int currentPrice;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    protected ProductGroupJpaEntity() {
        super();
        this.isNew = false;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    private ProductGroupJpaEntity(
            Long id,
            Long sellerId,
            Long brandId,
            Long categoryId,
            Long shippingPolicyId,
            Long refundPolicyId,
            String productGroupName,
            String optionType,
            int regularPrice,
            int currentPrice,
            String status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.sellerId = sellerId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.shippingPolicyId = shippingPolicyId;
        this.refundPolicyId = refundPolicyId;
        this.productGroupName = productGroupName;
        this.optionType = optionType;
        this.regularPrice = regularPrice;
        this.currentPrice = currentPrice;
        this.status = status;
    }

    public static ProductGroupJpaEntity create(
            Long id,
            Long sellerId,
            Long brandId,
            Long categoryId,
            Long shippingPolicyId,
            Long refundPolicyId,
            String productGroupName,
            String optionType,
            int regularPrice,
            int currentPrice,
            String status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        ProductGroupJpaEntity entity =
                new ProductGroupJpaEntity(
                        id,
                        sellerId,
                        brandId,
                        categoryId,
                        shippingPolicyId,
                        refundPolicyId,
                        productGroupName,
                        optionType,
                        regularPrice,
                        currentPrice,
                        status,
                        createdAt,
                        updatedAt,
                        deletedAt);
        entity.isNew = true;
        return entity;
    }

    public Long getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getShippingPolicyId() {
        return shippingPolicyId;
    }

    public Long getRefundPolicyId() {
        return refundPolicyId;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public String getOptionType() {
        return optionType;
    }

    public int getRegularPrice() {
        return regularPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public String getStatus() {
        return status;
    }
}
