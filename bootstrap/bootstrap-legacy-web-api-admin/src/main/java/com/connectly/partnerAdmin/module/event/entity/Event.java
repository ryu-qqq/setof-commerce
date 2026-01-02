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
@Table(name = "EVENT")
@Entity
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private long id;

    @Column(name = "EVENT_TYPE")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Embedded
    private EventDetail eventDetail;


}
