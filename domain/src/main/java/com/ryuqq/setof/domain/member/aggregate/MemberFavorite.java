package com.ryuqq.setof.domain.member.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberFavoriteId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;

/**
 * 회원 찜 Aggregate.
 *
 * <p>회원의 상품 찜(즐겨찾기) 정보를 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class MemberFavorite {

    private final MemberFavoriteId id;
    private final MemberId memberId;
    private final ProductGroupId productGroupId;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private MemberFavorite(
            MemberFavoriteId id,
            MemberId memberId,
            ProductGroupId productGroupId,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.productGroupId = productGroupId;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 찜 생성.
     *
     * @param memberId 회원 ID
     * @param productGroupId 상품그룹 ID
     * @param occurredAt 생성 시각
     * @return 새 MemberFavorite 인스턴스
     */
    public static MemberFavorite forNew(
            MemberId memberId, ProductGroupId productGroupId, Instant occurredAt) {
        return new MemberFavorite(
                MemberFavoriteId.forNew(),
                memberId,
                productGroupId,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static MemberFavorite reconstitute(
            MemberFavoriteId id,
            MemberId memberId,
            ProductGroupId productGroupId,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new MemberFavorite(
                id, memberId, productGroupId, deletionStatus, createdAt, updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /** 찜 삭제 (소프트 삭제). */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    /** 삭제된 찜 복원. */
    public void restore(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.active();
        this.updatedAt = occurredAt;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    // VO Getters
    public MemberFavoriteId id() {
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

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
