package com.setof.connectly.module.mileage.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.Reason;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "mileage_history")
@Entity
public class MileageHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mileage_history_id")
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "mileage_id")
    private long mileageId;

    @Column(name = "change_amount")
    private double changeAmount;

    @Column(name = "reason")
    @Enumerated(value = EnumType.STRING)
    private Reason reason;

    @Column(name = "issue_type")
    private MileageIssueType issueType;

    @Column(name = "target_id")
    private long targetId;
}
