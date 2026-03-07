package com.ryuqq.setof.storage.legacy.refundaccount.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
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
 * LegacyRefundAccountEntity - 레거시 환불 계좌 엔티티.
 *
 * <p>레거시 DB의 refund_account 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "refund_account")
public class LegacyRefundAccountEntity extends LegacyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_account_id")
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyRefundAccountEntity() {}

    public static LegacyRefundAccountEntity create(
            long userId,
            String bankName,
            String accountNumber,
            String accountHolderName,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyRefundAccountEntity entity = new LegacyRefundAccountEntity();
        entity.userId = userId;
        entity.bankName = bankName;
        entity.accountNumber = accountNumber;
        entity.accountHolderName = accountHolderName;
        entity.deleteYn = Yn.N;
        return new LegacyRefundAccountEntity(insertDate, updateDate, entity);
    }

    private LegacyRefundAccountEntity(
            LocalDateTime insertDate, LocalDateTime updateDate, LegacyRefundAccountEntity source) {
        super(insertDate, updateDate);
        this.id = source.id;
        this.userId = source.userId;
        this.bankName = source.bankName;
        this.accountNumber = source.accountNumber;
        this.accountHolderName = source.accountHolderName;
        this.deleteYn = source.deleteYn;
    }

    public static LegacyRefundAccountEntity reconstitute(
            Long id,
            long userId,
            String bankName,
            String accountNumber,
            String accountHolderName,
            Yn deleteYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyRefundAccountEntity entity = new LegacyRefundAccountEntity();
        entity.id = id;
        entity.userId = userId;
        entity.bankName = bankName;
        entity.accountNumber = accountNumber;
        entity.accountHolderName = accountHolderName;
        entity.deleteYn = deleteYn;
        return new LegacyRefundAccountEntity(insertDate, updateDate, entity);
    }

    /**
     * 계좌 정보를 수정합니다 (Dirty Checking 기반).
     *
     * @param bankName 새 은행명
     * @param accountNumber 새 계좌번호
     * @param accountHolderName 새 예금주명
     */
    public void update(String bankName, String accountNumber, String accountHolderName) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
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

    public Yn getDeleteYn() {
        return deleteYn;
    }
}
