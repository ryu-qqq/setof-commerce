package com.setof.connectly.module.discount.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.discount.entity.embedded.DiscountDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "DISCOUNT_POLICY")
@Entity
public class DiscountPolicy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISCOUNT_POLICY_ID")
    private long id;

    @Embedded private DiscountDetails discountDetails;

    @Builder
    public DiscountPolicy(long id, DiscountDetails discountDetails) {
        this.id = id;
        this.discountDetails = discountDetails;
    }

    public void updateDiscountDetails(DiscountDetails discountDetails) {
        this.discountDetails = discountDetails;
    }

    public DiscountPolicy notUsePolicy() {
        discountDetails.setActiveYn(Yn.N);
        return this;
    }

    public DiscountPolicy usePolicy() {
        discountDetails.setActiveYn(Yn.Y);
        return this;
    }
}
