package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.core.MessageContext;
import com.setof.connectly.module.notification.core.MessageQueueContext;
import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.enums.MessageStatus;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageProvider;
import com.setof.connectly.module.qna.enums.QnaType;
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
        if(qnaType.isOrderQna()) return AlimTalkTemplateCode.CS_ORDER_S;
        return AlimTalkTemplateCode.CS_PRODUCT_S;
    }
}
