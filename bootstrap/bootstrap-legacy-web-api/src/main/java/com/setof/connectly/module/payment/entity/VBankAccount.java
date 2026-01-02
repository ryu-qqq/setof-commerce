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
@Table(name = "vbank_account")
@Entity
public class VBankAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vbank_account_id")
    private Long id;

    private long paymentId;
    private long userId;

    @Column(name = "vbank_name")
    private String vBankName;

    @Column(name = "vbank_number")
    private String vBankNumber;

    @Column(name = "vbank_holder")
    private String vBankHolder;

    @Column(name = "vbank_due_date")
    private LocalDateTime vBankDueDate;

    private long paymentAmount;

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
