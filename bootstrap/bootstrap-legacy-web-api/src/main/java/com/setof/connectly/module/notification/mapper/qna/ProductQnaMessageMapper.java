package com.setof.connectly.module.notification.mapper.qna;

import com.setof.connectly.module.notification.dto.qna.ProductQnaMessage;
import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductQnaMessageMapper implements AlimTalkMessageMapper<ProductQnaMessage, QnaSheet> {

    @Override
    public ProductQnaMessage toAlimTalkMessage(QnaSheet qnaSheet) {
        return ProductQnaMessage.builder()
                .productGroupId(qnaSheet.getTargetId())
                .productGroupName(qnaSheet.getSubStringProductGroupName())
                .questionType(qnaSheet.getQnaDetailType().getDisplayName())
                .recipientNo(qnaSheet.getPhoneNumber())
                .build();
    }

    @Override
    public AlimTalkTemplateCode getAlimTalkTemplateCode() {
        return AlimTalkTemplateCode.CS_PRODUCT_S;
    }
}
