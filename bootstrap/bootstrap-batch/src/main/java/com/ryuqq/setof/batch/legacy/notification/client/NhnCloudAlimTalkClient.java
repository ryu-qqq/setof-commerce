package com.ryuqq.setof.batch.legacy.notification.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ryuqq.setof.batch.legacy.notification.dto.AlimTalkSendResult;
import com.ryuqq.setof.batch.legacy.notification.dto.MessageQueueItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * NHN Cloud 알림톡 API 클라이언트
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class NhnCloudAlimTalkClient {

    private static final Logger log = LoggerFactory.getLogger(NhnCloudAlimTalkClient.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${nhn.alimtalk.url:https://api-alimtalk.cloud.toast.com}")
    private String baseUrl;

    @Value("${nhn.alimtalk.app-key:}")
    private String appKey;

    @Value("${nhn.alimtalk.secret-key:}")
    private String secretKey;

    @Value("${nhn.alimtalk.sender-key:}")
    private String senderKey;

    @Value("${nhn.alimtalk.dry-run:false}")
    private boolean dryRun;

    public NhnCloudAlimTalkClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 알림톡 발송
     *
     * @param messageQueueItem 발송할 메시지 정보
     * @return 발송 결과
     */
    public AlimTalkSendResult sendAlimTalk(MessageQueueItem messageQueueItem) {
        log.debug(
                "Sending alim talk: messageId={}, templateCode={}",
                messageQueueItem.messageId(),
                messageQueueItem.templateCode());

        // Dry-run 모드: 실제 발송 없이 성공 반환
        if (dryRun) {
            log.info(
                    "[DRY-RUN] Would send alim talk: messageId={}, templateCode={}, phone={}",
                    messageQueueItem.messageId(),
                    messageQueueItem.templateCode(),
                    maskPhoneNumber(messageQueueItem.phoneNumber()));
            return AlimTalkSendResult.success(messageQueueItem.messageId());
        }

        try {
            String url = String.format("%s/alimtalk/v2.2/appkeys/%s/messages", baseUrl, appKey);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Secret-Key", secretKey);

            ObjectNode requestBody = buildRequestBody(messageQueueItem);
            HttpEntity<String> request =
                    new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            return parseResponse(messageQueueItem.messageId(), response.getBody());

        } catch (RestClientException e) {
            log.error(
                    "Failed to send alim talk: messageId={}, error={}",
                    messageQueueItem.messageId(),
                    e.getMessage());
            return AlimTalkSendResult.failure(
                    messageQueueItem.messageId(), "CONNECTION_ERROR", e.getMessage());

        } catch (Exception e) {
            log.error(
                    "Unexpected error while sending alim talk: messageId={}",
                    messageQueueItem.messageId(),
                    e);
            return AlimTalkSendResult.failure(
                    messageQueueItem.messageId(), "UNKNOWN_ERROR", e.getMessage());
        }
    }

    private ObjectNode buildRequestBody(MessageQueueItem messageQueueItem) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("senderKey", senderKey);
        body.put("templateCode", messageQueueItem.templateCode().getCode());

        ArrayNode recipientList = objectMapper.createArrayNode();
        ObjectNode recipient = objectMapper.createObjectNode();
        recipient.put("recipientNo", messageQueueItem.phoneNumber());

        // 템플릿 변수 파싱
        if (messageQueueItem.templateVariables() != null
                && !messageQueueItem.templateVariables().isEmpty()) {
            try {
                JsonNode variables = objectMapper.readTree(messageQueueItem.templateVariables());
                recipient.set("templateParameter", variables);
            } catch (Exception e) {
                log.warn(
                        "Failed to parse template variables: {}",
                        messageQueueItem.templateVariables());
            }
        }

        recipientList.add(recipient);
        body.set("recipientList", recipientList);

        return body;
    }

    private String maskPhoneNumber(String phone) {
        if (phone == null || phone.length() < 7) {
            return "***";
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    private AlimTalkSendResult parseResponse(Long messageId, String responseBody) {
        try {
            JsonNode response = objectMapper.readTree(responseBody);
            JsonNode header = response.get("header");

            if (header != null && header.get("isSuccessful").asBoolean()) {
                log.info("Alim talk sent successfully: messageId={}", messageId);
                return AlimTalkSendResult.success(messageId);
            } else {
                String resultCode = header != null ? header.get("resultCode").asText() : "UNKNOWN";
                String resultMessage =
                        header != null ? header.get("resultMessage").asText() : "Unknown error";
                log.warn(
                        "Alim talk failed: messageId={}, code={}, message={}",
                        messageId,
                        resultCode,
                        resultMessage);
                return AlimTalkSendResult.failure(messageId, resultCode, resultMessage);
            }
        } catch (Exception e) {
            log.error(
                    "Failed to parse response: messageId={}, response={}",
                    messageId,
                    responseBody,
                    e);
            return AlimTalkSendResult.failure(messageId, "PARSE_ERROR", "Failed to parse response");
        }
    }
}
