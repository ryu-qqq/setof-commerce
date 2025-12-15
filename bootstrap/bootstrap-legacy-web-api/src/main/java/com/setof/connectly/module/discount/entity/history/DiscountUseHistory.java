package com.setof.connectly.module.discount.entity.history;

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
@Table(name = "DISCOUNT_USE_HISTORY")
@Entity
public class DiscountUseHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISCOUNT_USE_HISTORY")
    private long id;

    @Column(name = "DISCOUNT_POLICY_ID")
    private long discountPolicyId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Column(name = "USE_DATE")
    private LocalDateTime useDate;
}
