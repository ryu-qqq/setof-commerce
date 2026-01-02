package com.connectly.partnerAdmin.module.order.entity.snapshot.image;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.embedded.SnapShotProductGroupDetailDescription;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "order_snapshot_product_group_detail_description")
@Entity
public class OrderSnapShotProductGroupDetailDescription extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_group_detail_description_id")
    private long id;

    @Embedded
    private SnapShotProductGroupDetailDescription snapShotProductGroupDetailDescription;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
