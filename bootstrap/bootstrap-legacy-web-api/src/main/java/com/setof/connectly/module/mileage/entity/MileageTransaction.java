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
@Table(name = "mileage_transaction")
@Entity
public class MileageTransaction extends BaseEntity {

    @Id
    @Column(name = "mileage_transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mileage_id")
    private Long mileageId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "issue_type")
    @Enumerated(EnumType.STRING)
    private MileageIssueType issueType;

    @Column(name = "target_id")
    private long targetId;

    @Column(name = "expected_mileage_amount")
    private double expectedMileageAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MileageStatus status;

    public void delete() {
        this.status = MileageStatus.CANCELLED;
        this.setDeleteYn(Yn.Y);
    }
}
