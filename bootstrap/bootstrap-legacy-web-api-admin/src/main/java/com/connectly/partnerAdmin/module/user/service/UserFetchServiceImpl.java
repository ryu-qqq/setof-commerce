package com.connectly.partnerAdmin.module.user.service;


import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.user.core.WebUserContext;
import com.connectly.partnerAdmin.module.user.filter.UserFilter;
import com.connectly.partnerAdmin.module.user.mapper.UserPageableMapper;
import com.connectly.partnerAdmin.module.user.repository.UserFetchRepository;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserFetchServiceImpl implements UserFetchService {

    private final UserFetchRepository userFetchRepository;
    private final UserPageableMapper userPageableMapper;

    @Override
    public CustomPageable<WebUserContext> fetchUsers(UserFilter filter, Pageable pageable){
        List<WebUserContext> webUserContexts = userFetchRepository.fetchUsers(filter, pageable);
        long totalCount = fetchUserCountQuery(filter);
        return userPageableMapper.toWebUserContext(webUserContexts, pageable, totalCount);
    }

    private long fetchUserCountQuery(UserFilter filter){
        JPAQuery<Long> longJPAQuery = userFetchRepository.fetchUserCountQuery(filter);
        Long totalCount = longJPAQuery.fetchOne();
        if(totalCount == null) return 0L;
        return totalCount;
    }
}
