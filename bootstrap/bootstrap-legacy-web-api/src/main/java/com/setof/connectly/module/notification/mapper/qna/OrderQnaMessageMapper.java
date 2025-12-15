package com.setof.connectly.module.notification.mapper.qna;

import com.setof.connectly.module.notification.dto.qna.OrderQnaMessage;
import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
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
        return AlimTalkTemplateCode.CS_ORDER_S;
    }
}
