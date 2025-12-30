package com.connectly.partnerAdmin.module.payment.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "VBANK_ACCOUNT")
@Entity
public class VBankAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VBANK_ACCOUNT_ID")
    private long id;

    @Column(name = "VBANK_NAME")
    private String vBankName;

    @Column(name = "VBANK_NUMBER")
    private String vBankNumber;

    @Column(name = "VBANK_HOLDER")
    private String vBankHolder;

    @Column(name = "VBANK_DUE_DATE")
    private LocalDateTime vBankDueDate;

    @Column(name = "PAYMENT_AMOUNT")
    private BigDecimal paymentAmount;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

}
