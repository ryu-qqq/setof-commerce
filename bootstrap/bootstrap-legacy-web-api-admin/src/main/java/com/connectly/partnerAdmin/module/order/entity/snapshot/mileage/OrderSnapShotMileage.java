package com.connectly.partnerAdmin.module.order.entity.snapshot.mileage;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "ORDER_SNAPSHOT_MILEAGE")
@Entity
public class OrderSnapShotMileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_MILEAGE_ID")
    private long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @OneToMany(mappedBy = "orderSnapShotMileage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderSnapShotMileageDetail> mileageDetails = new ArrayList<>();

    public void addMileageDetail(OrderSnapShotMileageDetail mileageDetail) {
        mileageDetails.add(mileageDetail);
        mileageDetail.setOrderSnapShotMileage(this);
    }

}
