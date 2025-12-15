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
@Table(name = "MILEAGE_HISTORY")
@Entity
public class MileageHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MILEAGE_HISTORY_ID")
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "MILEAGE_ID")
    private long mileageId;

    @Column(name = "CHANGE_AMOUNT")
    private double changeAmount;

    @Column(name = "REASON")
    @Enumerated(value = EnumType.STRING)
    private Reason reason;

    @Column(name = "ISSUE_TYPE")
    private MileageIssueType issueType;

    @Column(name = "TARGET_ID")
    private long targetId;
}
