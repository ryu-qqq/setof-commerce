package com.connectly.partnerAdmin.module.order.entity.snapshot.mileage;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "ORDER_SNAPSHOT_MILEAGE_DETAIL")
@Entity
public class OrderSnapShotMileageDetail extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_MILEAGE_DETAIL_ID")
    private long id;

    @Column(name = "USED_AMOUNT")
    private BigDecimal usedAmount;

    @Column(name = "MILEAGE_BALANCE")
    private BigDecimal mileageBalance;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_SNAPSHOT_MILEAGE_ID")
    private OrderSnapShotMileage orderSnapShotMileage;

    @Column(name = "MILEAGE_ID")
    private long mileageId;


}
