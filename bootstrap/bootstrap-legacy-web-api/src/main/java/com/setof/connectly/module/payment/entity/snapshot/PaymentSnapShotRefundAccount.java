package com.setof.connectly.module.payment.entity.snapshot;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "payment_snapshot_refund_account")
@Entity
public class PaymentSnapShotRefundAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_SNAPSHOT_REFUND_ACCOUNT_ID")
    private long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "REFUND_ACCOUNT_ID")
    private long refundAccountId;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_HOLDER_NAME")
    private String accountHolderName;

    public PaymentSnapShotRefundAccount(long paymentId, RefundAccountInfo refundAccountInfo) {
        this.paymentId = paymentId;
        this.refundAccountId = refundAccountInfo.getRefundAccountId();
        this.bankName = refundAccountInfo.getBankName();
        this.accountNumber = refundAccountInfo.getAccountNumber();
        this.accountHolderName = refundAccountInfo.getAccountHolderName();
    }
}
