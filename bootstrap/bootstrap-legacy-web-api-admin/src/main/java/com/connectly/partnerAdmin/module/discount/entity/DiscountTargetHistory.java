package com.connectly.partnerAdmin.module.discount.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "discount_target_history")
@Entity
public class DiscountTargetHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_target_history_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISCOUNT_TARGET_ID", nullable = false)
    private DiscountTarget discountTarget;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type")
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_yn")
    private Yn activeYn;

    @Column(name = "target_id")
    private long targetId;




    public DiscountTargetHistory(DiscountTarget discountTarget) {
        this.discountTarget = discountTarget;
        this.issueType = discountTarget.getIssueType();
        this.activeYn = discountTarget.getActiveYn();
        this.targetId = discountTarget.getTargetId();
    }

    public void setDiscountTarget(DiscountTarget discountTarget) {
        if (this.discountTarget != null) {
            this.discountTarget.getHistories().remove(this);
        }
        this.discountTarget = discountTarget;

        if (discountTarget != null && !discountTarget.getHistories().contains(this)) {
            discountTarget.getHistories().add(this);
        }
    }

}
