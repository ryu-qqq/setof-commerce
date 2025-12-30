package com.connectly.partnerAdmin.module.user.repository;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.user.core.QWebUserContext;
import com.connectly.partnerAdmin.module.user.core.WebUserContext;
import com.connectly.partnerAdmin.module.user.filter.UserFilter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.connectly.partnerAdmin.module.user.entity.QUserGrade.userGrade;
import static com.connectly.partnerAdmin.module.user.entity.QUserMileage.userMileage;
import static com.connectly.partnerAdmin.module.user.entity.QUsers.users;


@Repository
public class UserFetchRepositoryImpl extends AbstractCommonRepository implements UserFetchRepository {
    protected UserFetchRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public List<WebUserContext> fetchUsers(UserFilter filter, Pageable pageable){
        return getQueryFactory()
                .select(
                        new QWebUserContext(
                                users.id,
                                users.socialLoginType,
                                users.name,
                                users.email,
                                users.phoneNumber,
                                userGrade.gradeName,
                                users.withdrawalYn,
                                users.socialPkId.coalesce(""),
                                userMileage.currentMileage,
                                users.insertDate
                        )
                )
                .from(users)
                .innerJoin(users.userGrade, userGrade)
                .innerJoin(users.userMileage, userMileage)
                .where(
                        userIdEq(filter.getUserId()),
                        isOurMallUser(),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        joinBetween(filter.getStartDate(), filter.getEndDate())
                ).fetch();

    }
    @Override
    public JPAQuery<Long> fetchUserCountQuery(UserFilter filter){
        return getQueryFactory()
                .select(users.count())
                .from(users)
                .where(
                        userIdEq(filter.getUserId()),
                        isOurMallUser(),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        joinBetween(filter.getStartDate(), filter.getEndDate())

                )
                .distinct();
    }


    private BooleanExpression userIdEq(Long userId){
        if(userId != null) return users.id.eq(userId);
        return null;
    }

    private BooleanExpression isOurMallUser(){
        return users.exMallUserYn.eq(Yn.N);
    }

    private BooleanExpression joinBetween(LocalDateTime startDate, LocalDateTime endDate){
        return users.insertDate.after(startDate)
                    .and(users.insertDate.before(endDate));
    }

}
