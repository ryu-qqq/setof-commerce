package com.connectly.partnerAdmin.module.notification.service.alimtalk;


import com.connectly.partnerAdmin.module.notification.core.MessageContext;
import com.connectly.partnerAdmin.module.notification.core.MessageConversion;
import com.connectly.partnerAdmin.module.notification.enums.AlimTalkTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageMapper;
import com.connectly.partnerAdmin.module.notification.mapper.AlimTalkMessageProvider;
import com.connectly.partnerAdmin.module.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public abstract class AlimTalkMessageConversion<R> implements MessageConversion<R> {

    private final AlimTalkMessageProvider<? extends AlimTalkMessage, R> alimTalkMessageProvider;

    public abstract List<? extends MessageContext> convert(R r);


    protected String toJson(Object o){
        return JsonUtils.toJson(o);
    }

    protected AlimTalkMessageMapper<? extends AlimTalkMessage, R> getMapperProvider(AlimTalkTemplateCode alimTalkTemplateCode){
        return alimTalkMessageProvider.get(alimTalkTemplateCode);
    }

}
