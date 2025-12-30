package com.connectly.partnerAdmin.module.notification.dto.qna;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class OrderQnaMessage extends AbstractQnaMessage {
    private long orderId;
}
