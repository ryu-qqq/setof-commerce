package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.core.MessageContext;
import com.setof.connectly.module.notification.core.MessageConversion;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageProvider;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class AlimTalkMessageConversion<R> implements MessageConversion<R> {

    private final AlimTalkMessageProvider<? extends AlimTalkMessage, R> alimTalkMessageProvider;

    public abstract List<? extends MessageContext> convert(R r);

    protected String toJson(Object o) {
        return JsonUtils.toJson(o);
    }

    protected AlimTalkMessageMapper<? extends AlimTalkMessage, R> getMapperProvider(
            AlimTalkTemplateCode alimTalkTemplateCode) {
        return alimTalkMessageProvider.get(alimTalkTemplateCode);
    }
}
