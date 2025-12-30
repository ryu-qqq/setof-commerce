package com.connectly.partnerAdmin.module.notification.mapper;


import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.service.alimtalk.AlimTalkMessage;

public interface AlimTalkMessageMapper<T extends AlimTalkMessage, R> {

    T toAlimTalkMessage(R r);
    AlimTalkTemplateCode getAlimTalkTemplateCode();

}
