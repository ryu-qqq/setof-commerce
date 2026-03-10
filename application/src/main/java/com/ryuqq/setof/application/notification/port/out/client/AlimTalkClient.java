package com.ryuqq.setof.application.notification.port.out.client;

import com.ryuqq.setof.domain.notification.vo.NotificationEventType;
import java.util.Map;

/**
 * AlimTalkClient - NHN Cloud 알림톡 API 호출 포트.
 *
 * <p>Consumer가 메시지 조립 완료 후 NHN Cloud 알림톡 API를 호출할 때 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface AlimTalkClient {

    /**
     * 알림톡 발송.
     *
     * @param eventType 이벤트 유형 (NHN 템플릿 코드 매핑용)
     * @param recipientPhone 수신자 전화번호
     * @param templateVariables 템플릿 치환 변수
     */
    void send(
            NotificationEventType eventType,
            String recipientPhone,
            Map<String, String> templateVariables);
}
