package com.ryuqq.setof.adapter.out.persistence.product.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * ProductJpaEntity - 상품(SKU) JPA 엔티티.
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
@Table(name = "products")
public class ProductJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "sku_code", length = 100)
    private String skuCode;

    @Column(name = "regular_price", nullable = false)
    private int regularPrice;

    @Column(name = "current_price", nullable = false)
    private int currentPrice;

    @Column(name = "sale_price")
    private Integer salePrice;

    @Column(name = "discount_rate", nullable = false)
    private int discountRate;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected ProductJpaEntity() {
        super();
    }

    private ProductJpaEntity(
            Long id,
            Long productGroupId,
            String skuCode,
            int regularPrice,
            int currentPrice,
            Integer salePrice,
            int discountRate,
            int stockQuantity,
            String status,
            int sortOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.skuCode = skuCode;
        this.regularPrice = regularPrice;
        this.currentPrice = currentPrice;
        this.salePrice = salePrice;
        this.discountRate = discountRate;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.sortOrder = sortOrder;
    }

    public static ProductJpaEntity create(
            Long id,
            Long productGroupId,
            String skuCode,
            int regularPrice,
            int currentPrice,
            Integer salePrice,
            int discountRate,
            int stockQuantity,
            String status,
            int sortOrder,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new ProductJpaEntity(
                id,
                productGroupId,
                skuCode,
                regularPrice,
                currentPrice,
                salePrice,
                discountRate,
                stockQuantity,
                status,
                sortOrder,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public int getRegularPrice() {
        return regularPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getStatus() {
        return status;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
