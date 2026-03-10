package com.ryuqq.setof.adapter.out.nhn.adapter;

import com.ryuqq.setof.adapter.out.nhn.config.NhnClientProperties;
import com.ryuqq.setof.adapter.out.nhn.dto.NhnAlimTalkSendRequest;
import com.ryuqq.setof.adapter.out.nhn.dto.NhnAlimTalkSendResponse;
import com.ryuqq.setof.adapter.out.nhn.mapper.NhnAlimTalkMapper;
import com.ryuqq.setof.application.notification.port.out.client.AlimTalkClient;
import com.ryuqq.setof.domain.notification.vo.NotificationEventType;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * NHN Cloud 알림톡 발송 어댑터.
 *
 * <p>NHN Cloud AlimTalk API v2.3을 호출합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(prefix = "nhn.alimtalk", name = "app-key")
public class NhnAlimTalkAdapter implements AlimTalkClient {

    private static final Logger log = LoggerFactory.getLogger(NhnAlimTalkAdapter.class);

    private final RestClient restClient;
    private final NhnAlimTalkMapper mapper;
    private final String senderKey;

    public NhnAlimTalkAdapter(
            RestClient nhnAlimTalkRestClient,
            NhnAlimTalkMapper mapper,
            NhnClientProperties properties) {
        this.restClient = nhnAlimTalkRestClient;
        this.mapper = mapper;
        this.senderKey = properties.senderKey();
    }

    @Override
    public void send(
            NotificationEventType eventType,
            String recipientPhone,
            Map<String, String> templateVariables) {

        NhnAlimTalkSendRequest request =
                mapper.toSendRequest(senderKey, eventType, recipientPhone, templateVariables);

        try {
            NhnAlimTalkSendResponse response =
                    restClient
                            .post()
                            .uri("/messages")
                            .body(request)
                            .retrieve()
                            .body(NhnAlimTalkSendResponse.class);

            if (response == null || !response.isSuccessful()) {
                String errorMessage =
                        response != null && response.header() != null
                                ? response.header().resultMessage()
                                : "응답 없음";
                log.error(
                        "알림톡 발송 실패: eventType={}, recipientPhone={}, error={}",
                        eventType,
                        recipientPhone,
                        errorMessage);
                throw new RuntimeException("알림톡 발송 실패: " + errorMessage);
            }

            log.debug("알림톡 발송 성공: eventType={}, recipientPhone={}", eventType, recipientPhone);
        } catch (RestClientException e) {
            log.error(
                    "알림톡 API 호출 실패: eventType={}, recipientPhone={}, error={}",
                    eventType,
                    recipientPhone,
                    e.getMessage());
            throw new RuntimeException("알림톡 API 호출 실패", e);
        }
    }
}
