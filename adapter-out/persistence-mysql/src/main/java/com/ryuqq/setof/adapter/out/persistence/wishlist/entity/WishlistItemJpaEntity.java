package com.ryuqq.setof.adapter.out.persistence.wishlist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * WishlistItemJpaEntity - 찜 목록 JPA 엔티티.
 *
 * <p>updated_at이 없고 created_at + deleted_at만 있는 구조입니다. BaseAuditEntity를 상속하지 않고 직접 필드를 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "wishlist_items")
public class WishlistItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "legacy_member_id")
    private Long legacyMemberId;

    @Column(name = "product_group_id", nullable = false)
    private Long productGroupId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    protected WishlistItemJpaEntity() {}

    private WishlistItemJpaEntity(
            Long id,
            Long memberId,
            Long legacyMemberId,
            Long productGroupId,
            Instant createdAt,
            Instant deletedAt) {
        this.id = id;
        this.memberId = memberId;
        this.legacyMemberId = legacyMemberId;
        this.productGroupId = productGroupId;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public static WishlistItemJpaEntity create(
            Long id,
            Long memberId,
            Long legacyMemberId,
            Long productGroupId,
            Instant createdAt,
            Instant deletedAt) {
        return new WishlistItemJpaEntity(
                id, memberId, legacyMemberId, productGroupId, createdAt, deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getLegacyMemberId() {
        return legacyMemberId;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
