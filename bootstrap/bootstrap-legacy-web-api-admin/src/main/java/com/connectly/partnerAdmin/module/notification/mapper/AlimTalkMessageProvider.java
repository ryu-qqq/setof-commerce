package com.connectly.partnerAdmin.module.notification.mapper;

import com.connectly.partnerAdmin.module.common.provider.AbstractProvider;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.service.alimtalk.AlimTalkMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlimTalkMessageProvider<T extends AlimTalkMessage, R> extends AbstractProvider<AlimTalkTemplateCode, AlimTalkMessageMapper<T, R>> {

    public AlimTalkMessageProvider(List<AlimTalkMessageMapper<T, R>> mappers) {
        for (AlimTalkMessageMapper<T, R> mapper : mappers) {
            map.put(mapper.getAlimTalkTemplateCode(), mapper);
        }
    }

}
