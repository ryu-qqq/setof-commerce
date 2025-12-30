package com.connectly.partnerAdmin.module.user.repository;

import com.connectly.partnerAdmin.module.user.core.WebUserContext;
import com.connectly.partnerAdmin.module.user.filter.UserFilter;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserFetchRepository {

    List<WebUserContext> fetchUsers(UserFilter filter, Pageable pageable);
    JPAQuery<Long> fetchUserCountQuery(UserFilter filter);
}
