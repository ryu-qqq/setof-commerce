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
@Table(name = "ORDER_SNAPSHOT_PRODUCT_GROUP_DETAIL_DESCRIPTION")
@Entity
public class OrderSnapShotProductGroupDetailDescription extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_PRODUCT_GROUP_DETAIL_DESCRIPTION_ID")
    private long id;

    @Embedded
    private SnapShotProductGroupDetailDescription snapShotProductGroupDetailDescription;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

}
