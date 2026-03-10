package com.ryuqq.setof.adapter.out.persistence.notification;

import com.ryuqq.setof.application.notification.port.out.client.AlimTalkClient;
import com.ryuqq.setof.domain.notification.vo.NotificationEventType;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AlimTalkClientAdapter implements AlimTalkClient {

    @Override
    public void send(
            NotificationEventType eventType,
            String recipientPhone,
            Map<String, String> templateVariables) {
        // TODO: NHN Cloud 알림톡 API 연동 후 구현
        throw new UnsupportedOperationException("AlimTalk sending not yet implemented");
    }
}
