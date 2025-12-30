package com.ryuqq.setof.adapter.out.persistence.order.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * OrderJpaEntity - Order JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 orders 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등)
 *   <li>자식 엔티티 조회는 별도 Repository 사용
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
@Table(name = "orders")
public class OrderJpaEntity extends BaseAuditEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "order_number", nullable = false, length = 30, unique = true)
    private String orderNumber;

    @Column(name = "checkout_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID checkoutId;

    @Column(name = "payment_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID paymentId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "receiver_name", nullable = false, length = 50)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "address_detail", length = 100)
    private String addressDetail;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "memo", length = 500)
    private String memo;

    @Column(name = "total_item_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalItemAmount;

    @Column(name = "shipping_fee", nullable = false, precision = 15, scale = 2)
    private BigDecimal shippingFee;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "ordered_at", nullable = false)
    private Instant orderedAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "shipped_at")
    private Instant shippedAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "legacy_order_id")
    private Long legacyOrderId;

    protected OrderJpaEntity() {
        // JPA 기본 생성자
    }

    private OrderJpaEntity(
            UUID id,
            String orderNumber,
            UUID checkoutId,
            UUID paymentId,
            Long sellerId,
            String memberId,
            String status,
            String receiverName,
            String receiverPhone,
            String address,
            String addressDetail,
            String zipCode,
            String memo,
            BigDecimal totalItemAmount,
            BigDecimal shippingFee,
            BigDecimal totalAmount,
            Instant orderedAt,
            Instant confirmedAt,
            Instant shippedAt,
            Instant deliveredAt,
            Instant completedAt,
            Instant cancelledAt,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.orderNumber = orderNumber;
        this.checkoutId = checkoutId;
        this.paymentId = paymentId;
        this.sellerId = sellerId;
        this.memberId = memberId;
        this.status = status;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipCode = zipCode;
        this.memo = memo;
        this.totalItemAmount = totalItemAmount;
        this.shippingFee = shippingFee;
        this.totalAmount = totalAmount;
        this.orderedAt = orderedAt;
        this.confirmedAt = confirmedAt;
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;
    }

    public static OrderJpaEntity of(
            UUID id,
            String orderNumber,
            UUID checkoutId,
            UUID paymentId,
            Long sellerId,
            String memberId,
            String status,
            String receiverName,
            String receiverPhone,
            String address,
            String addressDetail,
            String zipCode,
            String memo,
            BigDecimal totalItemAmount,
            BigDecimal shippingFee,
            BigDecimal totalAmount,
            Instant orderedAt,
            Instant confirmedAt,
            Instant shippedAt,
            Instant deliveredAt,
            Instant completedAt,
            Instant cancelledAt,
            Instant createdAt,
            Instant updatedAt) {
        return new OrderJpaEntity(
                id,
                orderNumber,
                checkoutId,
                paymentId,
                sellerId,
                memberId,
                status,
                receiverName,
                receiverPhone,
                address,
                addressDetail,
                zipCode,
                memo,
                totalItemAmount,
                shippingFee,
                totalAmount,
                orderedAt,
                confirmedAt,
                shippedAt,
                deliveredAt,
                completedAt,
                cancelledAt,
                createdAt,
                updatedAt);
    }

    public UUID getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public UUID getCheckoutId() {
        return checkoutId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getStatus() {
        return status;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getMemo() {
        return memo;
    }

    public BigDecimal getTotalItemAmount() {
        return totalItemAmount;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Instant getOrderedAt() {
        return orderedAt;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    public Instant getShippedAt() {
        return shippedAt;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public Long getLegacyOrderId() {
        return legacyOrderId;
    }
}
