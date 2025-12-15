package com.setof.connectly.module.notification.mapper;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.service.alimtalk.AlimTalkMessage;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AlimTalkMessageProvider<T extends AlimTalkMessage, R>
        extends AbstractProvider<AlimTalkTemplateCode, AlimTalkMessageMapper<T, R>> {

    public AlimTalkMessageProvider(List<AlimTalkMessageMapper<T, R>> mappers) {
        for (AlimTalkMessageMapper<T, R> mapper : mappers) {
            map.put(mapper.getAlimTalkTemplateCode(), mapper);
        }
    }
}
