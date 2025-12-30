package com.connectly.partnerAdmin.module.user.service;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.user.core.WebUserContext;
import com.connectly.partnerAdmin.module.user.filter.UserFilter;
import org.springframework.data.domain.Pageable;

public interface UserFetchService {
    CustomPageable<WebUserContext> fetchUsers(UserFilter filter, Pageable pageable);
}
