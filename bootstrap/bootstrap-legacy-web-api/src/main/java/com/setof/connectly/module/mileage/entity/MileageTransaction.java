package com.setof.connectly.module.mileage.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.mileage.enums.MileageIssueType;
import com.setof.connectly.module.mileage.enums.MileageStatus;
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
@Table(name = "MILEAGE_TRANSACTION")
@Entity
public class MileageTransaction extends BaseEntity {

    @Id
    @Column(name = "MILEAGE_TRANSACTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "MILEAGE_ID")
    private Long mileageId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "ISSUE_TYPE")
    @Enumerated(EnumType.STRING)
    private MileageIssueType issueType;

    @Column(name = "TARGET_ID")
    private long targetId;

    @Column(name = "EXPECTED_MILEAGE_AMOUNT")
    private double expectedMileageAmount;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MileageStatus status;

    public void delete() {
        this.status = MileageStatus.CANCELLED;
        this.setDeleteYn(Yn.Y);
    }
}
