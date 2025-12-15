package com.setof.connectly.module.exception.notification;

import org.springframework.http.HttpStatus;

public class SlackNotificationException extends NotificationException {

    public static final String CODE = "SLACK-400";
    public static final String MESSAGE = "슬랙 메세지 발송에 실패했습니다.";

    public SlackNotificationException(String errorMsg) {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE + errorMsg);
    }
}
