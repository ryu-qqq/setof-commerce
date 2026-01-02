package com.setof.connectly.module.event.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.event.entity.embedded.EventDetail;
import com.setof.connectly.module.event.enums.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Builder
@AllArgsConstructor
@Getter
@Table(name = "event")
@Entity
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private long id;

    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Embedded private EventDetail eventDetail;

    public void setDisplayPeriod(DisplayPeriod displayPeriod) {
        eventDetail.setDisplayPeriod(displayPeriod);
    }
}
