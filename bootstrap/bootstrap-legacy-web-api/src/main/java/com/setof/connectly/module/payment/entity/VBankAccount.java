package com.setof.connectly.module.payment.entity;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private long paymentId;
    private long userId;

    @Column(name = "VBANK_NAME")
    private String vBankName;

    @Column(name = "VBANK_NUMBER")
    private String vBankNumber;

    @Column(name = "VBANK_HOLDER")
    private String vBankHolder;

    @Column(name = "VBANK_DUE_DATE")
    private LocalDateTime vBankDueDate;

    private long paymentAmount;

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
