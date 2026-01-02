package com.connectly.partnerAdmin.module.order.entity.snapshot.option;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.embedded.SnapShotOptionDetail;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "order_snapshot_option_detail")
@Entity
public class OrderSnapShotOptionDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_snapshot_option_detail_id")
    private long id;

    @Embedded
    private SnapShotOptionDetail snapShotOptionDetail;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


}
