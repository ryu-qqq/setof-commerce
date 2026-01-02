package com.setof.connectly.module.event.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.event.enums.EventMileageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "event_mileage")
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
    private EventMileageType mileageType;

    @Column(name = "MILEAGE_AMOUNT")
    private double mileageAmount;

    @Column(name = "MILEAGE_RATE")
    private double mileageRate;

    @Column(name = "EXPIRATION_DATE")
    private int expirationDate;
}
