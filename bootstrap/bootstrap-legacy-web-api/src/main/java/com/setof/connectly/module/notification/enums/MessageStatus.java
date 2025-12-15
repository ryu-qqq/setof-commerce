package com.setof.connectly.module.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatus {
    PENDING,
    SEND,
    FAILED,
}
