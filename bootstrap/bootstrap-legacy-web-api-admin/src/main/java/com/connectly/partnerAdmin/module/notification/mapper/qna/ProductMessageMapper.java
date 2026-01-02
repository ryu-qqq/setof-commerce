package com.connectly.partnerAdmin.module.notification.mapper.qna;


import com.connectly.partnerAdmin.module.notification.dto.qna.ProductQnaMessage;
import com.connectly.partnerAdmin.module.notification.dto.qna.QnaSheet;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMessageMapper implements AlimTalkMessageMapper<ProductQnaMessage, QnaSheet> {

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
        return AlimTalkTemplateCode.CS_PRODUCT;
    }
}
