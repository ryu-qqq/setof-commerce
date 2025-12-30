package com.connectly.partnerAdmin.module.user.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.mileage.entity.Mileage;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "USER_MILEAGE")
@Entity
public class UserMileage extends BaseEntity {
    @Id
    @Column(name = "USER_ID")
    private long id;

    @Setter
    
    @Column(name = "CURRENT_MILEAGE")
    private BigDecimal currentMileage;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "USER_ID")
    private Users users;

    @OneToMany(mappedBy = "userMileage", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Mileage> mileages = new LinkedHashSet<>();

    public UserMileage(BigDecimal initialMileage) {
        this.currentMileage = initialMileage;
    }

    public void setUsers(Users users) {
        if (this.users != null && this.users.equals(users)) {
            return;
        }
        this.users = users;
        if (users != null && !Objects.equals(users.getUserMileage(), this)) {
            users.setUserMileage(this);
        }
    }

    public void addMileage(Mileage mileage) {
        Money currentMoneyMileage = Money.wons(this.currentMileage);
        Money toAddMoney = Money.wons(mileage.getMileageAmount());
        this.currentMileage = currentMoneyMileage.plus(toAddMoney).getAmount();
        mileages.add(mileage);
        mileage.setUserMileage(this);
    }



    public void refundMileage(Mileage mileage, Money usedAmount, Long orderId) {
        Money currentMoneyMileage = Money.wons(this.currentMileage);
        this.currentMileage = currentMoneyMileage.plus(usedAmount).getAmount();
        mileage.refund(usedAmount, orderId);
    }

    public void expireMileage(Mileage mileage) {
        Money currentMoneyMileage = Money.wons(mileage.getMileageAmount());
        this.currentMileage = currentMoneyMileage.minus(currentMoneyMileage).getAmount();
    }


}