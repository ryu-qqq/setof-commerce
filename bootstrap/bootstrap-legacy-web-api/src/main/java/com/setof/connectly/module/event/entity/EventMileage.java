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
    @Column(name = "event_mileage_id")
    private long id;

    @Column(name = "event_id")
    private long eventId;

    @Column(name = "mileage_type")
    @Enumerated(EnumType.STRING)
    private EventMileageType mileageType;

    @Column(name = "mileage_amount")
    private double mileageAmount;

    @Column(name = "mileage_rate")
    private double mileageRate;

    @Column(name = "expiration_date")
    private int expirationDate;
}
