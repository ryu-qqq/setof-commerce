package com.connectly.partnerAdmin.module.mileage.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.mileage.enums.MileageIssueType;
import com.connectly.partnerAdmin.module.mileage.enums.MileageStatus;
import com.connectly.partnerAdmin.module.mileage.enums.Reason;
import com.connectly.partnerAdmin.module.user.entity.UserMileage;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "MILEAGE")
@Entity
public class Mileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MILEAGE_ID")
    private long id;

    
    @Column(name = "MILEAGE_AMOUNT")
    private BigDecimal mileageAmount;

    @Setter
    @Column(name = "USED_MILEAGE_AMOUNT")
    private BigDecimal usedMileageAmount;

    @Column(name = "ISSUED_DATE")
    private LocalDateTime issuedDate;

    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @Column(name = "ACTIVE_YN")
    @Enumerated(EnumType.STRING)
    private Yn activeYn;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "MILEAGE_STATUS")
    @Enumerated(EnumType.STRING)
    private MileageStatus mileageStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserMileage userMileage;

    @OneToMany(mappedBy = "mileage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MileageHistory> histories = new ArrayList<>();

    public void setUserMileage(UserMileage userMileage) {
        if (this.userMileage != null) {
            this.userMileage.getMileages().remove(this);
        }

        this.userMileage = userMileage;

        if (userMileage != null) {
            userMileage.getMileages().add(this);
        }
    }


    public void refund(Money usedAmount, Long orderId) {
        this.usedMileageAmount = Money.wons(this.usedMileageAmount).minus(usedAmount).getAmount();
        this.addHistory(usedAmount, Reason.SAVE, MileageIssueType.REFUND, orderId);
    }

    public void expire(){
        this.activeYn = Yn.N;
        Money remainMileage = Money.wons(mileageAmount).minus(Money.wons(usedMileageAmount));
        this.addHistory(remainMileage, Reason.USE, MileageIssueType.EXPIRED, null);
        this.usedMileageAmount = mileageAmount;
        userMileage.expireMileage(this);
    }


    public void addHistory(Money changeAmount, Reason reason, MileageIssueType issueType, Long targetId) {
        MileageHistory history = toMileageHistory(changeAmount, reason, issueType, targetId);
        histories.add(history);
        history.setMileage(this);
    }

    public void approved(){
        this.mileageStatus = MileageStatus.APPROVED;
    }

    private MileageHistory toMileageHistory(Money changeAmount, Reason reason, MileageIssueType issueType, Long targetId){
        return MileageHistory.builder()
                .mileage(this)
                .changeAmount(changeAmount.getAmount())
                .reason(reason)
                .issueType(issueType)
                .targetId(targetId ==null? 0L: targetId)
                .build();
    }



    public boolean isValid() {
        return this.activeYn.isYes() && this.expirationDate.isAfter(LocalDateTime.now()) && this.getDeleteYn().isNo();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mileage that = (Mileage) o;

        return Objects.equals(id, that.id) &&
                Objects.equals(mileageAmount, that.mileageAmount) &&
                Objects.equals(usedMileageAmount, that.usedMileageAmount) &&
                Objects.equals(issuedDate, that.issuedDate) &&
                Objects.equals(activeYn, that.activeYn) &&
                Objects.equals(title, that.title) &&
                Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mileageAmount, usedMileageAmount, issuedDate, expirationDate, activeYn, title);
    }


}