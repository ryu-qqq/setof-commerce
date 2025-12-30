package com.connectly.partnerAdmin.module.order.entity.snapshot.stock;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.group.embedded.SnapShotProduct;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "ORDER_SNAPSHOT_PRODUCT")
@Entity
public class OrderSnapShotProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_PRODUCT_ID")
    private long id;

    @Embedded
    private SnapShotProduct snapShotProduct;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

}
