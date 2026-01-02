package com.connectly.partnerAdmin.module.payment.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "payment_method")
public class PaymentMethod extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private long id;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethodEnum;

    @Column(name = "payment_method_merchant_key")
    private String paymentMethodMerchantKey;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    public static PaymentMethod defaultOf(){
        return new PaymentMethod(1L, PaymentMethodEnum.CARD, "", Yn.Y);
    }

}
