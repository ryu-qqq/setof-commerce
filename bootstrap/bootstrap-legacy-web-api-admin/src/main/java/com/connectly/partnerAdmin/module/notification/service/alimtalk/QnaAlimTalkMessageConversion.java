package com.connectly.partnerAdmin.module.notification.service.alimtalk;

import com.connectly.partnerAdmin.module.notification.core.MessageQueueContext;
import com.connectly.partnerAdmin.module.notification.dto.qna.QnaSheet;

import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.enums.MessageStatus;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageProvider;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class QnaAlimTalkMessageConversion extends AlimTalkMessageConversion<QnaSheet>{

    public QnaAlimTalkMessageConversion(AlimTalkMessageProvider<? extends AlimTalkMessage, QnaSheet> alimTalkMessageProvider) {
        super(alimTalkMessageProvider);
    }

    @Override
    public List<MessageQueueContext> convert(QnaSheet qnaSheet) {
        AlimTalkTemplateCode alimTalkTemplateCode = getServiceByQnaType(qnaSheet.getQnaType());

        AlimTalkMessageMapper<? extends AlimTalkMessage, QnaSheet> mapperProvider = getMapperProvider(alimTalkTemplateCode);
        AlimTalkMessage alimTalkMessage = mapperProvider.toAlimTalkMessage(qnaSheet);

        String parameter = toJson(alimTalkMessage);

        MessageQueueContext messageQueueContext = new MessageQueueContext(alimTalkTemplateCode, MessageStatus.PENDING, parameter);

        return Collections.singletonList(messageQueueContext);
    }


    private AlimTalkTemplateCode getServiceByQnaType(QnaType qnaType) {
        if(qnaType.isOrderQna()) return AlimTalkTemplateCode.CS_ORDER;
        return AlimTalkTemplateCode.CS_PRODUCT;
    }

}
