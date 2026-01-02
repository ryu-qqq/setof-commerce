package com.setof.connectly.module.order.entity.snapshot.mileage;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "order_snapshot_mileage_detail")
@Entity
public class OrderSnapShotMileageDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_mileage_detail_id")
    private Long id;

    @Column(name = "order_snapshot_mileage_id")
    private long orderSnapShotMileageId;

    @Column(name = "mileage_id")
    private long mileageId;

    @Column(name = "used_amount")
    private double usedAmount;

    @Column(name = "mileage_balance")
    private double mileageBalance;
}
