package com.setof.connectly.module.discount.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.discount.enums.IssueType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "discount_target")
@Entity
public class DiscountTarget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISCOUNT_TARGET_ID")
    private long id;

    @Column(name = "DISCOUNT_POLICY_ID")
    private long discountPolicyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUE_TYPE")
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVE_YN")
    private Yn activeYn;

    @Column(name = "TARGET_ID")
    private long targetId;

    @Builder
    public DiscountTarget(
            long id, long discountPolicyId, IssueType issueType, Yn activeYn, long targetId) {
        this.id = id;
        this.discountPolicyId = discountPolicyId;
        this.issueType = issueType;
        this.activeYn = activeYn;
        this.targetId = targetId;
    }

    public DiscountTarget setActiveYn(Yn activeYn) {
        this.activeYn = activeYn;
        return this;
    }
}
