package com.connectly.partnerAdmin.module.discount.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.discount.entity.embedded.DiscountDetails;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "DISCOUNT_POLICY_HISTORY")
@Entity
public class DiscountPolicyHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISCOUNT_POLICY_HISTORY_ID")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISCOUNT_POLICY_ID", nullable = false)
    private DiscountPolicy discountPolicy;

    @Embedded
    private DiscountDetails discountDetails;

    public DiscountPolicyHistory(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
        this.discountDetails = discountPolicy.getDiscountDetails();
        discountPolicy.getHistories().add(this);
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        if (this.discountPolicy != null) {
            this.discountPolicy.getHistories().remove(this);
        }

        this.discountPolicy = discountPolicy;

        if (discountPolicy != null && !discountPolicy.getHistories().contains(this)) {
            discountPolicy.getHistories().add(this);
        }

    }
}
