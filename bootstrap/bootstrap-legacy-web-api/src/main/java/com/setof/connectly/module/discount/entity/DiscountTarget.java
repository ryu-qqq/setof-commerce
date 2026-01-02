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
    @Column(name = "discount_target_id")
    private Long id;

    @Column(name = "discount_policy_id")
    private long discountPolicyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type")
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_yn")
    private Yn activeYn;

    @Column(name = "target_id")
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
