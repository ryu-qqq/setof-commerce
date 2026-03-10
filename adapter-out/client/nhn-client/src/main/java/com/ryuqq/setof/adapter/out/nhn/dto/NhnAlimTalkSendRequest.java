package com.ryuqq.setof.adapter.out.nhn.dto;

import java.util.List;
import java.util.Map;

/**
 * NHN Cloud 알림톡 발송 요청 DTO.
 *
 * <p>NHN Cloud AlimTalk API v2.3 스펙을 따릅니다.
 *
 * @param senderKey 발신 프로필 키
 * @param templateCode 템플릿 코드
 * @param recipientList 수신자 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NhnAlimTalkSendRequest(
        String senderKey, String templateCode, List<Recipient> recipientList) {

    public record Recipient(String recipientNo, Map<String, String> templateParameter) {}

    public static NhnAlimTalkSendRequest of(
            String senderKey,
            String templateCode,
            String recipientPhone,
            Map<String, String> templateVariables) {
        Recipient recipient = new Recipient(recipientPhone, templateVariables);
        return new NhnAlimTalkSendRequest(senderKey, templateCode, List.of(recipient));
    }
}
