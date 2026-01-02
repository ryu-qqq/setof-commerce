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
@Table(name = "event_product")
@Entity
public class EventProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_product_id")
    private Long id;

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
