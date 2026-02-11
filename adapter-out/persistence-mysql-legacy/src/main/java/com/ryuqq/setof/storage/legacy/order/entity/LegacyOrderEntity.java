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

    /** 리뷰 작성 여부 Enum. */
    public enum Yn {
        Y,
        N
    }
}
