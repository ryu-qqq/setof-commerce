package com.setof.connectly.auth.mapper;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.user.enums.SocialLoginType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocialMapperProvider extends AbstractProvider<SocialLoginType, SocialMapper> {

    @Autowired
    public SocialMapperProvider(List<SocialMapper> SocialMappers) {
        for (SocialMapper mapper : SocialMappers) {
            map.put(mapper.getSocialLoginType(), mapper);
        }
    }

    public SocialMapper getService(SocialLoginType type) {
        return map.get(type);
    }
}
