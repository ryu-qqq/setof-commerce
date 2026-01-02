package com.connectly.partnerAdmin.module.user.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.user.core.WebUserContext;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserPageableMapper {
    CustomPageable<WebUserContext> toWebUserContext(List<WebUserContext> webUserContexts, Pageable pageable, long total);

}
