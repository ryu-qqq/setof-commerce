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
@Table(name = "EVENT_PRODUCT")
@Entity
public class EventProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_PRODUCT_ID")
    private long id;

    @Column(name = "EVENT_ID")
    private long eventId;

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Column(name = "LIMIT_YN")
    @Enumerated(EnumType.STRING)
    private Yn limitYn;

    @Column(name = "LIMIT_QTY")
    private int limitQty;

    @Column(name = "EVENT_PAY_TYPE")
    @Enumerated(EnumType.STRING)
    private EventPayType eventPayType;

    @Column(name = "EVENT_PRODUCT_TYPE")
    @Enumerated(EnumType.STRING)
    private EventProductType eventProductType;

    @Enumerated(EnumType.STRING)
    @Column(name = "REWARDS_MILEAGE")
    private Yn rewardsMileage;


}
