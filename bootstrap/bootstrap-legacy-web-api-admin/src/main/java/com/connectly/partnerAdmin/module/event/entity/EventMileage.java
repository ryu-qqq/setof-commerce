package com.connectly.partnerAdmin.module.event.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.mileage.enums.MileageIssueType;
import lombok.*;
import jakarta.persistence.*;




@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "event_mileage")
@Entity
public class EventMileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_mileage_id")
    private long id;

    @Column(name = "event_id")
    private long eventId;

    @Column(name = "mileage_type")
    @Enumerated(EnumType.STRING)
    private MileageIssueType mileageType;

    @Column(name = "mileage_rate")
    private double mileageRate;

    @Column(name = "mileage_amount")
    private double mileageAmount;

    @Column(name = "expiration_date")
    private int expirationDate;

}
