package com.connectly.partnerAdmin.module.order.entity.snapshot.notice;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.notice.embedded.SnapShotNotice;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "order_snapshot_product_notice")
@Entity
public class OrderSnapShotProductNotice extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_product_notice_id")
    private long id;

    @Embedded
    private SnapShotNotice snapShotNotice;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
