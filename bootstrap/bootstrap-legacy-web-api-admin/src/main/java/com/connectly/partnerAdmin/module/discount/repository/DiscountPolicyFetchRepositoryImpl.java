package com.connectly.partnerAdmin.module.discount.repository;

import com.connectly.partnerAdmin.module.common.enums.PeriodType;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.discount.core.BaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.core.QBaseDiscountInfo;
import com.connectly.partnerAdmin.module.discount.dto.DiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.dto.QDiscountPolicyResponseDto;
import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.enums.PublisherType;
import com.connectly.partnerAdmin.module.discount.filter.DiscountFilter;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.connectly.partnerAdmin.module.discount.entity.QDiscountPolicy.discountPolicy;
import static com.connectly.partnerAdmin.module.discount.entity.QDiscountTarget.discountTarget;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;


@Repository
public class DiscountPolicyFetchRepositoryImpl extends AbstractCommonRepository implements DiscountPolicyFetchRepository {

    protected DiscountPolicyFetchRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public Optional<DiscountPolicyResponseDto> fetchDiscountPolicy(long discountPolicyId) {
        return Optional.ofNullable(
                getQueryFactory().from(discountPolicy)
                .where(discountPolicyIdEq(discountPolicyId))
                .transform(
                        GroupBy.groupBy(discountPolicy.id).as(
                                new QDiscountPolicyResponseDto(
                                        discountPolicy.id,
                                        discountPolicy.discountDetails,
                                        discountPolicy.insertDate,
                                        discountPolicy.updateDate,
                                        discountPolicy.insertOperator,
                                        discountPolicy.updateOperator
                                )
                        )
                ).get(discountPolicyId));
    }

    @Override
    public List<DiscountPolicyResponseDto> fetchDiscountPolicies(DiscountFilter filter, Pageable pageable) {
        return fetchDiscounts(filter, pageable);
    }

    @Override
    public Optional<DiscountPolicy> fetchDiscountEntity(long discountPolicyId) {
        return Optional.ofNullable(
                getQueryFactory().selectFrom(discountPolicy)
                .where(discountPolicyIdEq(discountPolicyId))
                .fetchOne()
        );
    }

    @Override
    public List<DiscountPolicy> fetchDiscountEntities(List<Long> discountPolicyIds) {
        return getQueryFactory().selectFrom(discountPolicy)
                .where(discountPolicyIdIn(discountPolicyIds))
                .fetch();
    }


    @Override
    public JPAQuery<Long> fetchDiscountPolicyCountQuery(DiscountFilter filter) {
        return getQueryFactory()
                .select(discountPolicy.count())
                .from(discountPolicy)
                .join(discountTarget)
                .on(discountTarget.discountPolicyId.eq(discountPolicy.id))

                .leftJoin(productGroup).on(productGroup.id.eq(discountTarget.targetId))
                .leftJoin(seller).on(seller.id.eq(discountTarget.targetId))
                .where(
                        periodTypeEq(filter.getPeriodType(), filter.getStartDate(), filter.getEndDate()),
                        policyActiveYn(filter.getActiveYn()),
                        publisherTypeEq(filter.getPublisherType()),
                        issueTypeEq(filter.getIssueType())

                )
                .distinct();
    }




    @Override
    public Optional<BaseDiscountInfo> fetchDiscountInfo(long targetId, IssueType issueType){
        return Optional.ofNullable(
                getQueryFactory()
                .select(
                        new QBaseDiscountInfo(
                                discountPolicy.id,
                                discountPolicy.discountDetails.discountType,
                                discountPolicy.discountDetails.issueType,
                                discountPolicy.discountDetails.discountLimitYn,
                                discountPolicy.discountDetails.maxDiscountPrice,
                                discountPolicy.discountDetails.discountRatio,
                                discountPolicy.discountDetails.policyStartDate,
                                discountPolicy.discountDetails.policyEndDate,
                                discountPolicy.discountDetails.priority,
                                discountTarget.targetId,
                                discountPolicy.discountDetails.shareRatio
                        )
                )
                .from(discountTarget)
                        .join(discountPolicy)
                        .on(discountTarget.discountPolicyId.eq(discountPolicy.id))
                .where(
                        targetIdEq(targetId), issueTypeEq(issueType), policyActive(), targetActive(), isBetweenPolicyDate()
                )
                .orderBy(discountPolicy.discountDetails.policyStartDate.desc())
                .fetchFirst());
    }

    @Override
    public List<BaseDiscountInfo> fetchDiscountInfos(Set<Long> targetIds, IssueType issueType) {
        return getQueryFactory()
                .select(
                        new QBaseDiscountInfo(
                                discountPolicy.id,
                                discountPolicy.discountDetails.discountType,
                                discountPolicy.discountDetails.issueType,
                                discountPolicy.discountDetails.discountLimitYn,
                                discountPolicy.discountDetails.maxDiscountPrice,
                                discountPolicy.discountDetails.discountRatio,
                                discountPolicy.discountDetails.policyStartDate,
                                discountPolicy.discountDetails.policyEndDate,
                                discountPolicy.discountDetails.priority,
                                discountTarget.targetId,
                                discountPolicy.discountDetails.shareRatio
                        )
                )
                .from(discountTarget)
                .join(discountPolicy)
                .on(discountTarget.discountPolicyId.eq(discountPolicy.id))
                .where(targetIdIn(targetIds), issueTypeEq(issueType), policyActive(), targetActive(),  isBetweenPolicyDate())
                .orderBy(discountPolicy.discountDetails.policyStartDate.desc())
                .fetch();
    }



    private List<DiscountPolicyResponseDto> fetchDiscounts(DiscountFilter filter, Pageable pageable) {
        List<OrderSpecifier<?>> ORDERS = getAllOrderSpecifiers(pageable, discountPolicy);

        return getQueryFactory()
                .select(
                        new QDiscountPolicyResponseDto(
                                discountPolicy.id,
                                discountPolicy.discountDetails,
                                discountPolicy.insertDate,
                                discountPolicy.updateDate,
                                discountPolicy.insertOperator,
                                discountPolicy.updateOperator
                        )
                )
                .from(discountPolicy)
                .join(discountPolicy)
                .on(discountTarget.discountPolicyId.eq(discountPolicy.id))
                .leftJoin(productGroup).on(productGroup.id.eq(discountTarget.targetId))
                .leftJoin(seller).on(seller.id.eq(discountTarget.targetId))
                .where(
                        periodTypeEq(filter.getPeriodType(), filter.getStartDate(), filter.getEndDate()),
                        policyActiveYn(filter.getActiveYn()),
                        publisherTypeEq(filter.getPublisherType()),
                        issueTypeEq(filter.getIssueType()),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord())
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();
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

    private BooleanExpression periodTypeEq(PeriodType periodType, LocalDateTime startDate, LocalDateTime endDate) {
        if (periodType != null && periodType.isPolicy()) {
            return discountPolicy.discountDetails.policyStartDate.before(endDate)
                    .and(discountPolicy.discountDetails.policyEndDate.after(startDate));

        }
        return discountPolicy.insertDate.between(startDate, endDate);
    }


    private BooleanExpression discountPolicyIdEq(Long discountPolicyId){
        if(discountPolicyId != null) return discountPolicy.id.eq(discountPolicyId);
        return null;
    }

    private BooleanExpression discountPolicyIdIn(List<Long> discountPolicyIds){
        return discountPolicy.id.in(discountPolicyIds);
    }

    private BooleanExpression policyActive(){
        return discountPolicy.discountDetails.activeYn.eq(Yn.Y);
    }

    private BooleanExpression targetActive(){
        return discountTarget.activeYn.eq(Yn.Y);
    }

    private BooleanExpression targetIdEq(long targetId){
        return discountTarget.targetId.eq(targetId);
    }

    private BooleanExpression targetIdIn(Set<Long> targetIds){
        return discountTarget.targetId.in(targetIds);
    }


    private BooleanExpression isBetweenPolicyDate() {
        LocalDateTime now = LocalDateTime.now();
        return discountPolicy.discountDetails.policyStartDate.loe(now)
                .and(discountPolicy.discountDetails.policyEndDate.goe(now));
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
