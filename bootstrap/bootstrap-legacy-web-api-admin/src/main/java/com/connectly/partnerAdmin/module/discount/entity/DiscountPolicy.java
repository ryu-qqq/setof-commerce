package com.connectly.partnerAdmin.module.discount.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.entity.embedded.DiscountDetails;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "discount_policy")
@Entity
public class DiscountPolicy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_policy_id")
    private long id;

    @Embedded
    private DiscountDetails discountDetails;


    @OneToMany(mappedBy = "discountPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DiscountPolicyHistory> histories = new LinkedHashSet<>();

    public DiscountPolicy(DiscountDetails discountDetails) {
        this.discountDetails = discountDetails;
    }


    public void addHistory() {
        DiscountPolicyHistory history = new DiscountPolicyHistory(this);
        history.setDiscountPolicy(this);
        histories.add(history);
    }

    public void setDiscountDetails(DiscountDetails discountDetails) {
        if (isDiscountDetailsChanged(discountDetails)) {
            addHistory();
            this.discountDetails = discountDetails;
        }
    }

    public DiscountPolicy setActiveYn(Yn yn) {
        addHistory();
        discountDetails.setActiveYn(yn);
        return this;
    }

    private boolean isDiscountDetailsChanged(DiscountDetails newDetails) {
        return !this.discountDetails.equals(newDetails);
    }

}
