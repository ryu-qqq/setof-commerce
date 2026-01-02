package com.setof.connectly.module.payment.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.payment.enums.method.PaymentMethodEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "payment_method")
@Entity
public class PaymentMethod extends BaseEntity {

    @Id
    @Column(name = "PAYMENT_METHOD_ID")
    private long id;

    @Column(name = "PAYMENT_METHOD")
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethodEnum;

    @Column(name = "PAYMENT_METHOD_MERCHANT_KEY")
    private String paymentMethodMerchantKey;

    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;
}
