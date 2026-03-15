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
 * RefundAccountJpaEntity - 환불 계좌 JPA 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "refund_accounts")
public class RefundAccountJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "bank_name", nullable = false, length = 50)
    private String bankName;

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @Column(name = "account_holder_name", nullable = false, length = 50)
    private String accountHolderName;

    protected RefundAccountJpaEntity() {
        super();
    }

    private RefundAccountJpaEntity(
            Long id,
            Long memberId,
            String bankName,
            String accountNumber,
            String accountHolderName,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.memberId = memberId;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
    }

    public static RefundAccountJpaEntity create(
            Long id,
            Long memberId,
            String bankName,
            String accountNumber,
            String accountHolderName,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new RefundAccountJpaEntity(
                id,
                memberId,
                bankName,
                accountNumber,
                accountHolderName,
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

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }
}
