package com.setof.connectly.module.notification.dto.qna;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class OrderQnaMessage extends AbstractQnaMessage {
    private long orderId;
}
