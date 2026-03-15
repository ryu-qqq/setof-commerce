package com.ryuqq.setof.adapter.out.persistence.mileage.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * MileageLedgerJpaEntity - 마일리지 원장 JPA 엔티티.
 *
 * <p>회원당 1개의 원장을 가집니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "mileage_ledgers")
public class MileageLedgerJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "legacy_user_id")
    private Long legacyUserId;

    protected MileageLedgerJpaEntity() {
        super();
    }

    private MileageLedgerJpaEntity(
            Long id, Long memberId, Long legacyUserId, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.memberId = memberId;
        this.legacyUserId = legacyUserId;
    }

    public static MileageLedgerJpaEntity create(
            Long id, Long memberId, Long legacyUserId, Instant createdAt, Instant updatedAt) {
        return new MileageLedgerJpaEntity(id, memberId, legacyUserId, createdAt, updatedAt);
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
}
