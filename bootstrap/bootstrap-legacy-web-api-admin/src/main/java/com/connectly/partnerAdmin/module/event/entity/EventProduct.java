package com.connectly.partnerAdmin.module.event.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.event.enums.EventPayType;
import com.connectly.partnerAdmin.module.event.enums.EventProductType;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "event_product")
@Entity
public class EventProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_product_id")
    private long id;

    @Column(name = "event_id")
    private long eventId;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "limit_yn")
    @Enumerated(EnumType.STRING)
    private Yn limitYn;

    @Column(name = "limit_qty")
    private int limitQty;

    @Column(name = "event_pay_type")
    @Enumerated(EnumType.STRING)
    private EventPayType eventPayType;

    @Column(name = "event_product_type")
    @Enumerated(EnumType.STRING)
    private EventProductType eventProductType;

    @Enumerated(EnumType.STRING)
    @Column(name = "rewards_mileage")
    private Yn rewardsMileage;


}
