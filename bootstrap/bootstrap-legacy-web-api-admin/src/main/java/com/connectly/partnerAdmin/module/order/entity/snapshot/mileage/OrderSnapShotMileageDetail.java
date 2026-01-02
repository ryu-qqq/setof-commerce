package com.connectly.partnerAdmin.module.order.entity.snapshot.mileage;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "order_snapshot_mileage_detail")
@Entity
public class OrderSnapShotMileageDetail extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_mileage_detail_id")
    private long id;

    @Column(name = "used_amount")
    private BigDecimal usedAmount;

    @Column(name = "mileage_balance")
    private BigDecimal mileageBalance;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_snapshot_mileage_id")
    private OrderSnapShotMileage orderSnapShotMileage;

    @Column(name = "mileage_id")
    private long mileageId;


}
