package com.ryuqq.setof.storage.legacy.paymentmethod.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyPaymentMethodEntity - 레거시 결제 수단 JPA Entity.
 *
 * <p>luxurydb.payment_method 테이블과 매핑됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "payment_method")
public class LegacyPaymentMethodEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "payment_method_id")
    private Long id;

    @Column(name = "PAYMENT_METHOD", nullable = false)
    private String paymentMethod;

    @Column(name = "PAYMENT_METHOD_MERCHANT_KEY", nullable = false)
    private String paymentMethodMerchantKey;

    @Column(name = "DISPLAY_YN", nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "delete_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    protected LegacyPaymentMethodEntity() {}

    public Long getId() {
        return id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentMethodMerchantKey() {
        return paymentMethodMerchantKey;
    }

    public Yn getDisplayYn() {
        return displayYn;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public boolean isDisplayed() {
        return displayYn == Yn.Y;
    }
}
