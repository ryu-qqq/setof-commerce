package com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
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
 * ProductGroupPriceJpaEntity - 상품 그룹 가격 JPA 엔티티.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: Persistable 구현으로 persist/merge 제어.
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "product_group_prices")
public class ProductGroupPriceJpaEntity extends BaseAuditEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient private boolean isNew = false;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "sale_price", nullable = false)
    private int salePrice;

    @Column(name = "discount_rate", nullable = false)
    private int discountRate;

    @Column(name = "direct_discount_rate", nullable = false)
    private int directDiscountRate;

    @Column(name = "direct_discount_price", nullable = false)
    private int directDiscountPrice;

    protected ProductGroupPriceJpaEntity() {
        super();
        this.isNew = false;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    private ProductGroupPriceJpaEntity(
            Long id,
            Long productGroupId,
            int salePrice,
            int discountRate,
            int directDiscountRate,
            int directDiscountPrice,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.productGroupId = productGroupId;
        this.salePrice = salePrice;
        this.discountRate = discountRate;
        this.directDiscountRate = directDiscountRate;
        this.directDiscountPrice = directDiscountPrice;
    }

    public static ProductGroupPriceJpaEntity create(
            Long productGroupId,
            int salePrice,
            int discountRate,
            int directDiscountRate,
            int directDiscountPrice) {
        Instant now = Instant.now();
        ProductGroupPriceJpaEntity entity =
                new ProductGroupPriceJpaEntity(
                        null,
                        productGroupId,
                        salePrice,
                        discountRate,
                        directDiscountRate,
                        directDiscountPrice,
                        now,
                        now);
        entity.isNew = true;
        return entity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
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
}
