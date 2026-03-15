package com.ryuqq.setof.adapter.out.persistence.cart.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * CartItemJpaEntity - 장바구니 아이템 JPA 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "cart_items")
public class CartItemJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "legacy_user_id")
    private Long legacyUserId;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected CartItemJpaEntity() {
        super();
    }

    private CartItemJpaEntity(
            Long id,
            Long memberId,
            Long legacyUserId,
            Long productGroupId,
            Long productId,
            Long sellerId,
            int quantity,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.memberId = memberId;
        this.legacyUserId = legacyUserId;
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.quantity = quantity;
    }

    public static CartItemJpaEntity create(
            Long id,
            Long memberId,
            Long legacyUserId,
            Long productGroupId,
            Long productId,
            Long sellerId,
            int quantity,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new CartItemJpaEntity(
                id,
                memberId,
                legacyUserId,
                productGroupId,
                productId,
                sellerId,
                quantity,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getLegacyUserId() {
        return legacyUserId;
    }

    public Long getProductGroupId() {
        return productGroupId;
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
}
