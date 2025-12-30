package com.connectly.partnerAdmin.module.user.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "REFUND_ACCOUNT")
@Entity
public class RefundAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFUND_ACCOUNT_ID")
    private long id;

    @Column(name = "BANK_NAME", length = 30, nullable = false)
    private String bankName;

    @Column(name = "ACCOUNT_NUMBER", length = 40, nullable = false)
    private String accountNumber;

    @Column(name = "ACCOUNT_HOLDER_NAME", length = 30, nullable = false)
    private String accountHolderName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private Users users;


}