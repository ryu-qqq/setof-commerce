package com.connectly.partnerAdmin.module.order.entity.snapshot.group;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.group.embedded.SnapShotProductGroup;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "order_snapshot_product_group")
@Entity
public class OrderSnapShotProductGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_group_id")
    private long id;

    @Embedded
    private SnapShotProductGroup snapShotProductGroup;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
