package com.connectly.partnerAdmin.module.order.entity.snapshot.image;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.embedded.SnapShotProductGroupImage;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "ORDER_SNAPSHOT_PRODUCT_GROUP_IMAGE")
@Entity
public class OrderSnapShotProductGroupImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_PRODUCT_GROUP_IMAGE_ID")
    private long id;

    @Embedded
    private SnapShotProductGroupImage snapShotProductGroupImage;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

}
