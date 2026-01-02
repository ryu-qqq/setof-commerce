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
    @Column(name = "payment_snapshot_refund_account_id")
    private long id;

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "refund_account_id")
    private long refundAccountId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    public PaymentSnapShotRefundAccount(long paymentId, RefundAccountInfo refundAccountInfo) {
        this.paymentId = paymentId;
        this.refundAccountId = refundAccountInfo.getRefundAccountId();
        this.bankName = refundAccountInfo.getBankName();
        this.accountNumber = refundAccountInfo.getAccountNumber();
        this.accountHolderName = refundAccountInfo.getAccountHolderName();
    }
}
