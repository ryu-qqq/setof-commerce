package com.connectly.partnerAdmin.module.discount.repository;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.DiscountUseHistoryDto;
import com.connectly.partnerAdmin.module.discount.dto.QDiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.QDiscountUseHistoryDto;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.enums.PublisherType;
import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.connectly.partnerAdmin.module.discount.entity.QDiscountPolicy.discountPolicy;
import static com.connectly.partnerAdmin.module.discount.entity.QDiscountTarget.discountTarget;
import static com.connectly.partnerAdmin.module.discount.entity.history.QDiscountUseHistory.discountUseHistory;
import static com.connectly.partnerAdmin.module.order.entity.QOrder.order;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;
import static com.connectly.partnerAdmin.module.user.entity.QUsers.users;
import static com.querydsl.core.group.GroupBy.groupBy;


@Repository
public class DiscountPolicyHistoryFetchRepositoryImpl extends AbstractCommonRepository implements DiscountPolicyHistoryFetchRepository {


    protected DiscountPolicyHistoryFetchRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public List<DiscountPolicyResponseDto> fetchDiscountPolicyHistories(DiscountFilter filter, Pageable pageable) {
        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable, discountPolicy);

        return getQueryFactory()
                .from(discountUseHistory)
                .join(discountPolicy).on(discountPolicy.id.eq(discountUseHistory.discountPolicyId))
                .join(discountTarget)
                .on(discountTarget.discountPolicyId.eq(discountPolicy.id))
                .where(
                        discountUseHistoryHasPolicyId(filter.getDiscountPolicyId()),
                        periodTypeEq(filter.getStartDate(), filter.getEndDate()),
                        policyActiveYn(filter.getActiveYn()),
                        publisherTypeEq(filter.getPublisherType()),
                        issueTypeEq(filter.getIssueType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .distinct()
                .transform(
                        groupBy(discountPolicy.id).list(
                                new QDiscountPolicyResponseDto(
                                        discountPolicy.id,
                                        discountPolicy.discountDetails,
                                        discountPolicy.insertDate,
                                        discountPolicy.updateDate,
                                        discountPolicy.insertOperator,
                                        discountPolicy.updateOperator
                                )
                        )
                );
    }

    @Override
    public List<DiscountUseHistoryDto> fetchDiscountUseHistories(long discountPolicyId, DiscountFilter filter, Pageable pageable) {
        return getQueryFactory()
                .select(
                        discountUseHistory.id,
                        discountUseHistory.userId,
                        users.name,
                        discountUseHistory.orderId,
                        order.orderAmount,
                        discountUseHistory.paymentId,
                        discountUseHistory.productGroupId,
                        orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice,
                        discountUseHistory.insertDate
                )
                .from(discountUseHistory)
                .innerJoin(order)
                    .on(order.id.eq(discountUseHistory.orderId))
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup).fetchJoin()
                .innerJoin(users).on(users.id.eq(discountUseHistory.userId))
                .where(
                        discountUseHistoryHasPolicyId(discountPolicyId),
                        periodTypeEq(filter.getStartDate(), filter.getEndDate()),
                        userIdEq(filter.getUserId())
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(discountUseHistory.id,
                        discountUseHistory.userId,
                        users.name,
                        discountUseHistory.orderId,
                        order.orderAmount,
                        discountUseHistory.paymentId,
                        discountUseHistory.productGroupId,
                        orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice,
                        discountUseHistory.insertDate)
                .transform(
                        groupBy(discountUseHistory.id).list(
                                new QDiscountUseHistoryDto(
                                        discountUseHistory.id,
                                        discountUseHistory.userId,
                                        users.name,
                                        discountUseHistory.orderId,
                                        order.orderAmount,
                                        discountUseHistory.paymentId,
                                        discountUseHistory.productGroupId,
                                        orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice,
                                        discountUseHistory.insertDate,
                                        orderSnapShotProductGroup.snapShotProductGroup.price.directDiscountPrice.sum()
                                ))
                );
    }

    @Override
    public JPAQuery<Long> fetchDiscountPolicyCountQuery(DiscountFilter filter) {
        return getQueryFactory()
                .select(discountUseHistory.count())
                .from(discountUseHistory)
                .join(discountPolicy).on(discountPolicy.id.eq(discountUseHistory.discountPolicyId))
                .join(discountTarget)
                .on(discountTarget.discountPolicyId.eq(discountPolicy.id))
                .join(users).on(users.id.eq(discountUseHistory.userId))
                .where(
                        periodTypeEq(filter.getStartDate(), filter.getEndDate()),
                        policyActiveYn(filter.getActiveYn()),
                        publisherTypeEq(filter.getPublisherType()),
                        issueTypeEq(filter.getIssueType())

                )
                .distinct();
    }

    @Override
    public JPAQuery<Long> fetchDiscountUsePolicyCountQuery(long discountPolicyId, DiscountFilter filter) {
        return getQueryFactory()
                .select(discountUseHistory.count())
                .from(discountUseHistory)
                .innerJoin(order).on(order.id.eq(discountUseHistory.orderId))
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup).fetchJoin()
                .where(
                        discountUseHistoryHasPolicyId(discountPolicyId),
                        periodTypeEq(filter.getStartDate(), filter.getEndDate()),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord())
                )
                .distinct();
    }


    private BooleanExpression discountUseHistoryHasPolicyId(Long discountPolicyId){
        if(discountPolicyId != null) return discountUseHistory.discountPolicyId.eq(discountPolicyId);
        return null;
    }


    private BooleanExpression policyActiveYn(Yn yn){
        if(yn !=null) return discountPolicy.discountDetails.activeYn.eq(yn);
        return null;
    }

    private BooleanExpression publisherTypeEq(PublisherType publisherType){
        if(publisherType != null) return discountPolicy.discountDetails.publisherType.eq(publisherType);
        return null;
    }

    private BooleanExpression issueTypeEq(IssueType issueType){
        if(issueType != null) return discountPolicy.discountDetails.issueType.eq(issueType);
        return null;
    }

    private BooleanExpression periodTypeEq(LocalDateTime startDate, LocalDateTime endDate){
        return discountUseHistory.insertDate.between(startDate, endDate);
    }

    private BooleanExpression userIdEq(Long userId){
        if(userId != null) return discountUseHistory.userId.eq(userId);
        return null;
    }

    @Override
    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable, Path<?> path) {
        List<OrderSpecifier<?>> allOrderSpecifiers = super.getAllOrderSpecifiers(pageable, path);
        if (allOrderSpecifiers.isEmpty()) {
            allOrderSpecifiers.add(discountPolicy.id.desc());
        }
        return allOrderSpecifiers;
    }

}
