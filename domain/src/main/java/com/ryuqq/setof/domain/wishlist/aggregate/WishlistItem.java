package com.ryuqq.setof.domain.wishlist.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.wishlist.id.WishlistItemId;
import java.time.Instant;

/**
 * 찜 항목 Aggregate Root.
 *
 * <p>회원이 상품그룹을 찜한 기록을 나타냅니다. 상품 스냅샷을 저장하지 않으며, 조회 시점에 상품 정보를 참조합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class WishlistItem {

    private final WishlistItemId id;
    private final MemberId memberId;
    private final ProductGroupId productGroupId;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;

    private WishlistItem(
            WishlistItemId id,
            MemberId memberId,
            ProductGroupId productGroupId,
            DeletionStatus deletionStatus,
            Instant createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.productGroupId = productGroupId;
        this.deletionStatus = deletionStatus;
        this.createdAt = createdAt;
    }

    public static WishlistItem create(MemberId memberId, ProductGroupId productGroupId) {
        return new WishlistItem(
                WishlistItemId.forNew(),
                memberId,
                productGroupId,
                DeletionStatus.active(),
                Instant.now());
    }

    public static WishlistItem reconstitute(
            WishlistItemId id,
            MemberId memberId,
            ProductGroupId productGroupId,
            DeletionStatus deletionStatus,
            Instant createdAt) {
        return new WishlistItem(id, memberId, productGroupId, deletionStatus, createdAt);
    }

    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
    }

    public WishlistItemId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public MemberId memberId() {
        return memberId;
    }

    public String memberIdValue() {
        return memberId.value();
    }

    public ProductGroupId productGroupId() {
        return productGroupId;
    }

    public Long productGroupIdValue() {
        return productGroupId.value();
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public Instant createdAt() {
        return createdAt;
    }
}
