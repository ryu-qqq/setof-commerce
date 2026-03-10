package com.ryuqq.setof.adapter.out.nhn.dto;

import java.util.List;

/**
 * NHN Cloud 알림톡 발송 응답 DTO.
 *
 * @param header 응답 헤더
 * @param message 응답 메시지
 * @author ryu-qqq
 * @since 1.1.0
 */
public record NhnAlimTalkSendResponse(Header header, Message message) {

    public record Header(int resultCode, String resultMessage, boolean isSuccessful) {}

    public record Message(String requestId, List<SendResult> sendResults) {}

    public record SendResult(
            String recipientSeq, String recipientNo, int resultCode, String resultMessage) {}

    public boolean isSuccessful() {
        return header != null && header.isSuccessful();
    }
}
