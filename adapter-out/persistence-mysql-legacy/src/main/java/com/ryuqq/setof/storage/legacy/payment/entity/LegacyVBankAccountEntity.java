package com.ryuqq.setof.storage.legacy.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyVBankAccountEntity - 레거시 가상계좌 엔티티.
 *
 * <p>레거시 DB의 vbank_account 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "vbank_account")
public class LegacyVBankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vbank_account_id")
    private Long id;

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "vbank_name")
    private String vBankName;

    @Column(name = "vbank_number")
    private String vBankNumber;

    @Column(name = "vbank_holder")
    private String vBankHolder;

    @Column(name = "vbank_due_date")
    private LocalDateTime vBankDueDate;

    @Column(name = "payment_amount")
    private long paymentAmount;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyVBankAccountEntity() {}

    public Long getId() {
        return id;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public long getUserId() {
        return userId;
    }

    public String getVBankName() {
        return vBankName;
    }

    public String getVBankNumber() {
        return vBankNumber;
    }

    public String getVBankHolder() {
        return vBankHolder;
    }

    public LocalDateTime getVBankDueDate() {
        return vBankDueDate;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
