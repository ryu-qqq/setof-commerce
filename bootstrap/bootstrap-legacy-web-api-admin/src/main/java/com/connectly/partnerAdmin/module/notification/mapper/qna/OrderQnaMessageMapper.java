package com.connectly.partnerAdmin.module.notification.mapper.qna;

import com.connectly.partnerAdmin.module.notification.dto.qna.OrderQnaMessage;
import com.connectly.partnerAdmin.module.notification.dto.qna.QnaSheet;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderQnaMessageMapper implements AlimTalkMessageMapper<OrderQnaMessage, QnaSheet> {
    @Override
    public OrderQnaMessage toAlimTalkMessage(QnaSheet qnaSheet) {
        return OrderQnaMessage.builder()
                .orderId(qnaSheet.getTargetId())
                .questionType(qnaSheet.getQnaDetailType().getDisplayName())
                .productGroupName(qnaSheet.getSubStringProductGroupName())
                .recipientNo(qnaSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.CS_ORDER;
    }
}
