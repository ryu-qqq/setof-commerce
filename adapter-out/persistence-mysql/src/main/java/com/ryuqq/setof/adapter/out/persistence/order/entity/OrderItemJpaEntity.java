package com.ryuqq.setof.adapter.out.persistence.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * OrderItemJpaEntity - OrderItem JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 order_items 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등)
 *   <li>부모 엔티티 참조는 UUID FK 필드 사용 (orderId)
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
@Table(name = "order_items")
public class OrderItemJpaEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    /** Order ID (UUID FK) */
    @Column(name = "order_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_stock_id", nullable = false)
    private Long productStockId;

    @Column(name = "ordered_quantity", nullable = false)
    private int orderedQuantity;

    @Column(name = "cancelled_quantity", nullable = false)
    private int cancelledQuantity;

    @Column(name = "refunded_quantity", nullable = false)
    private int refundedQuantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

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

    @Column(name = "original_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal originalPrice;

    protected OrderItemJpaEntity() {
        // JPA 기본 생성자
    }

    private OrderItemJpaEntity(
            UUID id,
            UUID orderId,
            Long productId,
            Long productStockId,
            int orderedQuantity,
            int cancelledQuantity,
            int refundedQuantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            String status,
            String productName,
            String productImage,
            String optionName,
            String brandName,
            String sellerName,
            BigDecimal originalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productStockId = productStockId;
        this.orderedQuantity = orderedQuantity;
        this.cancelledQuantity = cancelledQuantity;
        this.refundedQuantity = refundedQuantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.status = status;
        this.productName = productName;
        this.productImage = productImage;
        this.optionName = optionName;
        this.brandName = brandName;
        this.sellerName = sellerName;
        this.originalPrice = originalPrice;
    }

    public static OrderItemJpaEntity of(
            UUID id,
            UUID orderId,
            Long productId,
            Long productStockId,
            int orderedQuantity,
            int cancelledQuantity,
            int refundedQuantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            String status,
            String productName,
            String productImage,
            String optionName,
            String brandName,
            String sellerName,
            BigDecimal originalPrice) {
        return new OrderItemJpaEntity(
                id,
                orderId,
                productId,
                productStockId,
                orderedQuantity,
                cancelledQuantity,
                refundedQuantity,
                unitPrice,
                totalPrice,
                status,
                productName,
                productImage,
                optionName,
                brandName,
                sellerName,
                originalPrice);
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getProductStockId() {
        return productStockId;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public int getCancelledQuantity() {
        return cancelledQuantity;
    }

    public int getRefundedQuantity() {
        return refundedQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
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

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }
}
