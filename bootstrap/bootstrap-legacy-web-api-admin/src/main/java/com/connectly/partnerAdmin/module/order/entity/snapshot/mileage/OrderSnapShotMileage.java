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
@Table(name = "order_snapshot_mileage")
@Entity
public class OrderSnapShotMileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_mileage_id")
    private long id;

    @Column(name = "payment_id")
    private long paymentId;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "orderSnapShotMileage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderSnapShotMileageDetail> mileageDetails = new ArrayList<>();

    public void addMileageDetail(OrderSnapShotMileageDetail mileageDetail) {
        mileageDetails.add(mileageDetail);
        mileageDetail.setOrderSnapShotMileage(this);
    }

}
