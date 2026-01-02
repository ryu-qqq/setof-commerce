package com.connectly.partnerAdmin.module.order.entity.snapshot.delivery;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.delivery.embedded.SnapShotProductDelivery;
import jakarta.persistence.*;
import lombok.*;




@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "order_snapshot_product_delivery")
@Entity
public class OrderSnapShotProductDelivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_delivery_id")
    private long id;

    @Embedded
    private SnapShotProductDelivery snapShotProductDelivery;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


}
