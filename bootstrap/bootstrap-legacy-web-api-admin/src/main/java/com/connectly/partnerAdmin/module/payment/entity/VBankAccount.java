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
@Table(name = "vbank_account")
@Entity
public class VBankAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vbank_account_id")
    private long id;

    @Column(name = "vbank_name")
    private String vBankName;

    @Column(name = "vbank_number")
    private String vBankNumber;

    @Column(name = "vbank_holder")
    private String vBankHolder;

    @Column(name = "vbank_due_date")
    private LocalDateTime vBankDueDate;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "payment_id")
    private long paymentId;

}
