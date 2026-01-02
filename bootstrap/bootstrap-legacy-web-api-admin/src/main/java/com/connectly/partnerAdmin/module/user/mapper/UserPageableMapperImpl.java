package com.connectly.partnerAdmin.module.user.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.user.core.WebUserContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPageableMapperImpl implements UserPageableMapper{
    @Override
    public CustomPageable<WebUserContext> toWebUserContext(List<WebUserContext> webUserContexts, Pageable pageable, long total) {
        Long lastDomainId = webUserContexts.isEmpty() ? null : webUserContexts.getLast().getUserId();
        return new CustomPageable<>(webUserContexts, pageable, total, lastDomainId);
    }
}

