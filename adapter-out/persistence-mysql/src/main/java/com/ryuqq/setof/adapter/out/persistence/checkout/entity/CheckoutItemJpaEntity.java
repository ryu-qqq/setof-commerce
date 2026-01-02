package com.ryuqq.setof.adapter.out.persistence.checkout.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * CheckoutItemJpaEntity - CheckoutItem JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 checkout_items 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등)
 *   <li>부모 엔티티 참조는 UUID FK 필드 사용 (checkoutId)
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "checkout_items")
public class CheckoutItemJpaEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    /** Checkout ID (UUID FK) */
    @Column(name = "checkout_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID checkoutId;

    @Column(name = "product_stock_id", nullable = false)
    private Long productStockId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "product_image", length = 500)
    private String productImage;

    @Column(name = "option_name", length = 200)
    private String optionName;

    @Column(name = "brand_name", length = 100)
    private String brandName;

    @Column(name = "seller_name", length = 100)
    private String sellerName;

    protected CheckoutItemJpaEntity() {
        // JPA 기본 생성자
    }

    private CheckoutItemJpaEntity(
            UUID id,
            UUID checkoutId,
            Long productStockId,
            Long productId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            String productName,
            String productImage,
            String optionName,
            String brandName,
            String sellerName) {
        this.id = id;
        this.checkoutId = checkoutId;
        this.productStockId = productStockId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.productName = productName;
        this.productImage = productImage;
        this.optionName = optionName;
        this.brandName = brandName;
        this.sellerName = sellerName;
    }

    public static CheckoutItemJpaEntity of(
            UUID id,
            UUID checkoutId,
            Long productStockId,
            Long productId,
            Long sellerId,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            String productName,
            String productImage,
            String optionName,
            String brandName,
            String sellerName) {
        return new CheckoutItemJpaEntity(
                id,
                checkoutId,
                productStockId,
                productId,
                sellerId,
                quantity,
                unitPrice,
                totalPrice,
                productName,
                productImage,
                optionName,
                brandName,
                sellerName);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCheckoutId() {
        return checkoutId;
    }

    public Long getProductStockId() {
        return productStockId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getOptionName() {
        return optionName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getSellerName() {
        return sellerName;
    }
}
