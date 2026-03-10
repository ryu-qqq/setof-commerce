package com.ryuqq.setof.storage.legacy.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyOrderEntity - 레거시 주문 엔티티.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "orders")
public class LegacyOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "seller_id")
    private long sellerId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "order_amount")
    private long orderAmount;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private LegacyOrderStatus orderStatus;

    @Column(name = "settlement_date")
    private LocalDateTime settlementDate;

    @Column(name = "review_yn")
    @Enumerated(EnumType.STRING)
    private Yn reviewYn;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyOrderEntity() {}

    /**
     * 주문 레코드 생성용 팩토리 메서드.
     *
     * @param paymentId 결제 ID
     * @param productId 상품 ID
     * @param sellerId 판매자 ID
     * @param userId 사용자 ID
     * @param orderAmount 주문 금액
     * @param quantity 주문 수량
     * @param orderStatus 주문 상태
     * @return 새 주문 엔티티
     */
    public static LegacyOrderEntity create(
            long paymentId,
            long productId,
            long sellerId,
            long userId,
            long orderAmount,
            int quantity,
            LegacyOrderStatus orderStatus) {
        LegacyOrderEntity entity = new LegacyOrderEntity();
        entity.paymentId = paymentId;
        entity.productId = productId;
        entity.sellerId = sellerId;
        entity.userId = userId;
        entity.orderAmount = orderAmount;
        entity.quantity = quantity;
        entity.orderStatus = orderStatus;
        entity.reviewYn = Yn.N;
        entity.insertDate = java.time.LocalDateTime.now();
        entity.updateDate = java.time.LocalDateTime.now();
        return entity;
    }

    public Long getId() {
        return id;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public long getProductId() {
        return productId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public long getUserId() {
        return userId;
    }

    public long getOrderAmount() {
        return orderAmount;
    }

    public LegacyOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getSettlementDate() {
        return settlementDate;
    }

    public Yn getReviewYn() {
        return reviewYn;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /**
     * 주문 상태를 변경합니다.
     *
     * @param orderStatus 새 주문 상태
     */
    public void updateOrderStatus(LegacyOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.updateDate = LocalDateTime.now();
    }

    /** 리뷰 작성 여부 Enum. */
    public enum Yn {
        Y,
        N
    }
}
