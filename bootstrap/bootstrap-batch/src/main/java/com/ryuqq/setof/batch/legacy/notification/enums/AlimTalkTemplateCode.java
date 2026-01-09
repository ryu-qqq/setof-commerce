package com.ryuqq.setof.batch.legacy.notification.enums;

/**
 * 알림톡 템플릿 코드
 *
 * @author development-team
 * @since 1.0.0
 */
public enum AlimTalkTemplateCode {
    CANCEL_ORDER_AUTO("CANCEL_ORDER_AUTO"),
    CANCEL_NOTIFY("CANCEL_NOTIFY");

    private final String code;

    AlimTalkTemplateCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
