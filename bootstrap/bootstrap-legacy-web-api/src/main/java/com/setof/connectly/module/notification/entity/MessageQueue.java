package com.setof.connectly.module.notification.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.enums.MessageStatus;
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
@Builder
@AllArgsConstructor
@Getter
@Table(name = "message_queue")
@Entity
public class MessageQueue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "template_code")
    @Enumerated(EnumType.STRING)
    private AlimTalkTemplateCode templateCode;

    @Column(name = "parameters")
    private String parameters;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
}
