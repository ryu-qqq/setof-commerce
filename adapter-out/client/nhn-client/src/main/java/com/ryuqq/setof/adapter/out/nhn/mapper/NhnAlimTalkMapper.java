package com.ryuqq.setof.adapter.out.nhn.mapper;

import com.ryuqq.setof.adapter.out.nhn.dto.NhnAlimTalkSendRequest;
import com.ryuqq.setof.domain.notification.vo.NotificationEventType;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * NHN Cloud 알림톡 매퍼.
 *
 * <p>도메인 이벤트를 NHN Cloud API 요청으로 변환하고, NotificationEventType → NHN 템플릿 코드 매핑을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class NhnAlimTalkMapper {

    private static final Map<NotificationEventType, String> TEMPLATE_MAP =
            new EnumMap<>(NotificationEventType.class);

    static {
        TEMPLATE_MAP.put(NotificationEventType.ORDER_ACCEPTED, "order_accepted");
        TEMPLATE_MAP.put(NotificationEventType.ORDER_COMPLETED, "order_completed");
        TEMPLATE_MAP.put(NotificationEventType.DELIVERY_STARTED, "delivery_started");
        TEMPLATE_MAP.put(NotificationEventType.CANCEL_REQUESTED, "cancel_requested");
        TEMPLATE_MAP.put(
                NotificationEventType.CANCEL_REQUESTED_TO_SELLER, "cancel_requested_seller");
        TEMPLATE_MAP.put(NotificationEventType.CANCEL_AUTO, "cancel_auto");
        TEMPLATE_MAP.put(NotificationEventType.CANCEL_COMPLETED, "cancel_completed");
        TEMPLATE_MAP.put(NotificationEventType.REFUND_REQUESTED, "refund_requested");
        TEMPLATE_MAP.put(
                NotificationEventType.REFUND_REQUESTED_TO_SELLER, "refund_requested_seller");
        TEMPLATE_MAP.put(NotificationEventType.REFUND_REJECTED, "refund_rejected");
        TEMPLATE_MAP.put(NotificationEventType.REFUND_ACCEPTED, "refund_accepted");
        TEMPLATE_MAP.put(NotificationEventType.QNA_PRODUCT_CREATED, "qna_product_created");
        TEMPLATE_MAP.put(
                NotificationEventType.QNA_PRODUCT_CREATED_TO_SELLER, "qna_product_created_seller");
        TEMPLATE_MAP.put(NotificationEventType.QNA_ORDER_CREATED, "qna_order_created");
        TEMPLATE_MAP.put(
                NotificationEventType.QNA_ORDER_CREATED_TO_SELLER, "qna_order_created_seller");
        TEMPLATE_MAP.put(NotificationEventType.MEMBER_JOINED, "member_joined");
        TEMPLATE_MAP.put(NotificationEventType.MILEAGE_EXPIRING_SOON, "mileage_expiring_soon");
    }

    public String resolveTemplateCode(NotificationEventType eventType) {
        String templateCode = TEMPLATE_MAP.get(eventType);
        if (templateCode == null) {
            throw new IllegalArgumentException("매핑되지 않은 알림 이벤트 유형: " + eventType);
        }
        return templateCode;
    }

    public NhnAlimTalkSendRequest toSendRequest(
            String senderKey,
            NotificationEventType eventType,
            String recipientPhone,
            Map<String, String> templateVariables) {
        String templateCode = resolveTemplateCode(eventType);
        return NhnAlimTalkSendRequest.of(
                senderKey, templateCode, recipientPhone, templateVariables);
    }
}
