package com.setof.connectly.module.event.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.event.enums.EventPayType;
import com.setof.connectly.module.event.enums.EventProductType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
