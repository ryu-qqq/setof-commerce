package com.connectly.partnerAdmin.module.event.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.event.entity.embedded.EventDetail;
import com.connectly.partnerAdmin.module.event.enums.EventType;
import lombok.*;
import jakarta.persistence.*;



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

    @Embedded
    private EventDetail eventDetail;


}
