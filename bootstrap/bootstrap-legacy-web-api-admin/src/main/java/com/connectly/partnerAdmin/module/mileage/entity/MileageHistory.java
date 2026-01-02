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
@Table(name = "mileage_history")
@Entity
public class MileageHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mileage_history_id")
    private long id;

    
    @Column(name = "change_amount")
    private BigDecimal changeAmount;

    @Column(name = "reason")
    @Enumerated(value = EnumType.STRING)
    private Reason reason;

    @Column(name = "issue_type")
    private MileageIssueType issueType;

    @Column(name = "target_id")
    private long targetId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MILEAGE_ID", nullable = false)
    private Mileage mileage;

}
