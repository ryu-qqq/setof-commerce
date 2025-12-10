package com.ryuqq.setof.adapter.out.persistence.refundaccount.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * RefundAccountJpaEntity - RefundAccount JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 refund_accounts 테이블과 매핑됩니다.
 *
 * <p><strong>SoftDeletableEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt, deletedAt
 *   <li>소프트 딜리트 지원 (deletedAt != null -> 삭제)
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>member_id: UUID v7 문자열로 직접 관리 (FK 없음)
 *   <li>bank_id: Long으로 직접 관리 (FK 없음)
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "refund_accounts")
public class RefundAccountJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 회원 ID (UUID v7 - FK 없이 String으로 관리) */
    @Column(name = "member_id", nullable = false, length = 36, unique = true)
    private String memberId;

    /** 은행 ID (Long FK 전략 - FK 없이 관리) */
    @Column(name = "bank_id", nullable = false)
    private Long bankId;

    /** 계좌번호 (마스킹 저장) */
    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;

    /** 예금주명 */
    @Column(name = "account_holder_name", nullable = false, length = 50)
    private String accountHolderName;

    /** 계좌 검증 여부 */
    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    /** 계좌 검증 일시 */
    @Column(name = "verified_at")
    private Instant verifiedAt;

    /**
     * JPA 기본 생성자 (protected)
     */
    protected RefundAccountJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     */
    private RefundAccountJpaEntity(
            Long id,
            String memberId,
            Long bankId,
            String accountNumber,
            String accountHolderName,
            boolean isVerified,
            Instant verifiedAt,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.memberId = memberId;
        this.bankId = bankId;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.isVerified = isVerified;
        this.verifiedAt = verifiedAt;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     */
    public static RefundAccountJpaEntity of(
            Long id,
            String memberId,
            Long bankId,
            String accountNumber,
            String accountHolderName,
            boolean isVerified,
            Instant verifiedAt,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new RefundAccountJpaEntity(
                id,
                memberId,
                bankId,
                accountNumber,
                accountHolderName,
                isVerified,
                verifiedAt,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public Long getBankId() {
        return bankId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }
}
