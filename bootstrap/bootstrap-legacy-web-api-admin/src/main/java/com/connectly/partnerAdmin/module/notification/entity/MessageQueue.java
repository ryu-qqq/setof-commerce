package com.connectly.partnerAdmin.module.notification.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.enums.MessageStatus;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "message_queue")
@Entity
public class MessageQueue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private long id;

    @Column(name = "template_code")
    @Enumerated(EnumType.STRING)
    private AlimTalkTemplateCode templateCode;

    @Column(name = "parameters")
    private String parameters;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

}
