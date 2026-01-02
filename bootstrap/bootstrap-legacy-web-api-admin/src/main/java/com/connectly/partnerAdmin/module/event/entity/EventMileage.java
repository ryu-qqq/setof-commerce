package com.connectly.partnerAdmin.module.event.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.mileage.enums.MileageIssueType;
import lombok.*;
import jakarta.persistence.*;




@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "EVENT_MILEAGE")
@Entity
public class EventMileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_MILEAGE_ID")
    private long id;

    @Column(name = "EVENT_ID")
    private long eventId;

    @Column(name = "MILEAGE_TYPE")
    @Enumerated(EnumType.STRING)
    private MileageIssueType mileageType;

    @Column(name = "MILEAGE_RATE")
    private double mileageRate;

    @Column(name = "MILEAGE_AMOUNT")
    private double mileageAmount;

    @Column(name = "EXPIRATION_DATE")
    private int expirationDate;

}
