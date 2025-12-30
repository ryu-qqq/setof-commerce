package com.connectly.partnerAdmin.module.notification.service.slack;


import com.connectly.partnerAdmin.module.notification.core.BaseMessageContext;
import com.connectly.partnerAdmin.module.notification.core.MessageConversion;
import com.connectly.partnerAdmin.module.notification.enums.SlackTemplateCode;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageMapper;
import com.connectly.partnerAdmin.module.notification.mapper.SlackMessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public abstract class SlackMessageConversion<R> implements MessageConversion<R> {

    private final SlackMessageProvider<? extends SlackMessage, R> slackMessageProvider;

    public  abstract List<BaseMessageContext> convert(R r) ;


    protected String toString(Object o){
        return o.toString();
    }


    protected SlackMessageMapper<? extends SlackMessage, R> getMapperProvider(SlackTemplateCode slackTemplateCode){
        return slackMessageProvider.get(slackTemplateCode);
    }


}
