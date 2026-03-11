package com.ryuqq.setof.adapter.out.persistence.productgroup.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductGroupJpaEntity - 상품 그룹 JPA 엔티티.
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
@Table(name = "product_groups")
public class ProductGroupJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "sale_price", nullable = false)
    private int salePrice;

    @Column(name = "discount_rate", nullable = false)
    private int discountRate;

    @Column(name = "direct_discount_rate", nullable = false)
    private int directDiscountRate;

    @Column(name = "direct_discount_price", nullable = false)
    private int directDiscountPrice;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    protected ProductGroupJpaEntity() {
        super();
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
            int salePrice,
            int discountRate,
            int directDiscountRate,
            int directDiscountPrice,
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
        this.salePrice = salePrice;
        this.discountRate = discountRate;
        this.directDiscountRate = directDiscountRate;
        this.directDiscountPrice = directDiscountPrice;
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
            int salePrice,
            int discountRate,
            int directDiscountRate,
            int directDiscountPrice,
            String status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ProductGroupJpaEntity(
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
                salePrice,
                discountRate,
                directDiscountRate,
                directDiscountPrice,
                status,
                createdAt,
                updatedAt,
                deletedAt);
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

    public int getSalePrice() {
        return salePrice;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getDirectDiscountRate() {
        return directDiscountRate;
    }

    public int getDirectDiscountPrice() {
        return directDiscountPrice;
    }

    public String getStatus() {
        return status;
    }
}
