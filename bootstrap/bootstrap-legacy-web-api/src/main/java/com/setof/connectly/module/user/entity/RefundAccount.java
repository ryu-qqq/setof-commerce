package com.setof.connectly.module.user.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.user.dto.account.CreateRefundAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "REFUND_ACCOUNT")
@Entity
public class RefundAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFUND_ACCOUNT_ID")
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    private String bankName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_HOLDER_NAME")
    private String accountHolderName;

    public void update(CreateRefundAccount createRefundAccount) {
        this.bankName = createRefundAccount.getBankName();
        this.accountNumber = createRefundAccount.getAccountNumber();
        this.accountHolderName = createRefundAccount.getAccountHolderName();
    }
}
