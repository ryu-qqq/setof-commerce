package com.setof.connectly.module.notification.mapper;

import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.service.alimtalk.AlimTalkMessage;

public interface AlimTalkMessageMapper<T extends AlimTalkMessage, R> {

    T toAlimTalkMessage(R r);

    AlimTalkTemplateCode getAlimTalkTemplateCode();
}
