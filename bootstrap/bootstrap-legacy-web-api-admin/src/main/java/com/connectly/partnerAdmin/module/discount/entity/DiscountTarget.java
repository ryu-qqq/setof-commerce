package com.connectly.partnerAdmin.module.discount.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "discount_target")
@Entity
public class DiscountTarget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_target_id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type")
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_yn")
    private Yn activeYn;

    @Column(name = "target_id")
    private long targetId;

    @Column(name = "discount_policy_id")
    private long discountPolicyId;

    @OneToMany(mappedBy = "discountTarget", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DiscountTargetHistory> histories = new LinkedHashSet<>();

    public DiscountTarget(DiscountPolicy discountPolicy, long targetId) {
        this.issueType = discountPolicy.getDiscountDetails().getIssueType();
        this.targetId = targetId;
        this.activeYn = Yn.Y;
        this.discountPolicyId = discountPolicy.getId();
    }

    public void addHistory() {
        DiscountTargetHistory history = new DiscountTargetHistory(this);
        histories.add(history);
        history.setDiscountTarget(this);
    }


    public DiscountTarget setActiveYn(Yn yn) {
        addHistory();
        activeYn = yn;
        return this;
    }

    public BaseDiscountInfo getDiscountInfo(DiscountPolicy discountPolicy) {
        return BaseDiscountInfo.builder()
                .discountType(discountPolicy.getDiscountDetails().getDiscountType())
                .issueType(discountPolicy.getDiscountDetails().getIssueType())
                .discountLimitYn(discountPolicy.getDiscountDetails().getDiscountLimitYn())
                .maxDiscountPrice(discountPolicy.getDiscountDetails().maxDiscountPrice)
                .discountRatio(discountPolicy.getDiscountDetails().getDiscountRatio())
                .policyStartDate(discountPolicy.getDiscountDetails().getPolicyStartDate())
                .policyEndDate(discountPolicy.getDiscountDetails().getPolicyEndDate())
                .priority(discountPolicy.getDiscountDetails().getPriority())
                .targetId(targetId)
                .shareRatio(discountPolicy.getDiscountDetails().getShareRatio())
                .build();
    }

}
