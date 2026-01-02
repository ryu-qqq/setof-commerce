package com.connectly.partnerAdmin.module.mileage.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.generic.money.converter.MoneyConverter;
import com.connectly.partnerAdmin.module.mileage.enums.MileageIssueType;
import com.connectly.partnerAdmin.module.mileage.enums.Reason;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    
    @Column(name = "CHANGE_AMOUNT")
    private BigDecimal changeAmount;

    @Column(name = "REASON")
    @Enumerated(value = EnumType.STRING)
    private Reason reason;

    @Column(name = "ISSUE_TYPE")
    private MileageIssueType issueType;

    @Column(name = "TARGET_ID")
    private long targetId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MILEAGE_ID", nullable = false)
    private Mileage mileage;

}
