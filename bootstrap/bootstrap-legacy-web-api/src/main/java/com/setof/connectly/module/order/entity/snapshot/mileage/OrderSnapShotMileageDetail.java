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
@Table(name = "ORDER_SNAPSHOT_MILEAGE_DETAIL")
@Entity
public class OrderSnapShotMileageDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_MILEAGE_DETAIL_ID")
    private long id;

    @Column(name = "ORDER_SNAPSHOT_MILEAGE_ID")
    private long orderSnapShotMileageId;

    @Column(name = "MILEAGE_ID")
    private long mileageId;

    @Column(name = "USED_AMOUNT")
    private double usedAmount;

    @Column(name = "MILEAGE_BALANCE")
    private double mileageBalance;
}
