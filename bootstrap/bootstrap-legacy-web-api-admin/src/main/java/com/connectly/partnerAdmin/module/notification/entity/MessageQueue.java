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
@Table(name = "MESSAGE_QUEUE")
@Entity
public class MessageQueue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
    private long id;

    @Column(name = "TEMPLATE_CODE")
    @Enumerated(EnumType.STRING)
    private AlimTalkTemplateCode templateCode;

    @Column(name = "PARAMETERS")
    private String parameters;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

}
