package com.setof.connectly.module.discount.repository;

import static com.setof.connectly.module.discount.entity.QDiscountPolicy.discountPolicy;
import static com.setof.connectly.module.discount.entity.QDiscountTarget.discountTarget;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.dto.QDiscountCacheDto;
import com.setof.connectly.module.discount.enums.IssueType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiscountPolicyFindRepositoryImpl implements DiscountPolicyFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<DiscountCacheDto> fetchDiscountInfo(long targetId, IssueType issueType) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QDiscountCacheDto(
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
                                        discountPolicy.discountDetails.shareRatio))
                        .from(discountTarget)
                        .innerJoin(discountPolicy)
                        .on(discountPolicy.id.eq(discountTarget.discountPolicyId))
                        .where(
                                targetIdEq(targetId),
                                issueTypeEq(issueType),
                                policyActive(),
                                targetActive(),
                                isBetweenPolicyDate())
                        .orderBy(discountPolicy.discountDetails.policyStartDate.desc())
                        .fetchFirst());
    }

    @Override
    public List<DiscountCacheDto> fetchDiscountInfos(Set<Long> targetIds, IssueType issueType) {
        return queryFactory
                .from(discountTarget)
                .innerJoin(discountPolicy)
                .on(discountPolicy.id.eq(discountTarget.discountPolicyId))
                .where(
                        targetIdIn(targetIds),
                        issueTypeEq(issueType),
                        policyActive(),
                        targetActive(),
                        isBetweenPolicyDate())
                .orderBy(discountPolicy.discountDetails.policyStartDate.desc())
                .transform(
                        GroupBy.groupBy(discountTarget.targetId)
                                .list(
                                        new QDiscountCacheDto(
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
                                                discountPolicy.discountDetails.shareRatio)));
    }

    private BooleanExpression targetIdEq(long targetId) {
        return discountTarget.targetId.eq(targetId);
    }

    private BooleanExpression targetIdIn(Set<Long> targetIds) {
        return discountTarget.targetId.in(targetIds);
    }

    private BooleanExpression issueTypeEq(IssueType issueType) {
        if (issueType != null) return discountPolicy.discountDetails.issueType.eq(issueType);
        return null;
    }

    private BooleanExpression policyActive() {
        return discountPolicy.discountDetails.activeYn.eq(Yn.Y);
    }

    private BooleanExpression targetActive() {
        return discountTarget.activeYn.eq(Yn.Y);
    }

    private BooleanExpression isBetweenPolicyDate() {
        LocalDateTime now = LocalDateTime.now();
        return discountPolicy
                .discountDetails
                .policyEndDate
                .goe(now)
                .and(discountPolicy.discountDetails.policyStartDate.loe(now));
    }
}
