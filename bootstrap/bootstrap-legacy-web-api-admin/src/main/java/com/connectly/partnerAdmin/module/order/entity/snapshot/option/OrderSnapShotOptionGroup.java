package com.connectly.partnerAdmin.module.order.entity.snapshot.option;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.embedded.SnapShotOptionGroup;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "ORDER_SNAPSHOT_OPTION_GROUP")
@Entity
public class OrderSnapShotOptionGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_SNAPSHOT_OPTION_GROUP_ID")
    private long id;

    @Embedded
    private SnapShotOptionGroup snapShotOptionGroup;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;


}
