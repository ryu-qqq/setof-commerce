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
@Table(name = "refund_account")
@Entity
public class RefundAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_account_id")
    private Long id;

    @Column(name = "user_id")
    private long userId;

    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    public void update(CreateRefundAccount createRefundAccount) {
        this.bankName = createRefundAccount.getBankName();
        this.accountNumber = createRefundAccount.getAccountNumber();
        this.accountHolderName = createRefundAccount.getAccountHolderName();
    }
}
