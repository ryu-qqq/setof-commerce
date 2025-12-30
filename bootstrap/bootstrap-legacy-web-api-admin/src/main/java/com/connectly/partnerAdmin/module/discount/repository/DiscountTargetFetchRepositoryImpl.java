package com.connectly.partnerAdmin.module.discount.repository;


import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.dto.ProductDiscountTarget;
import com.connectly.partnerAdmin.module.discount.dto.QProductDiscountTarget;
import com.connectly.partnerAdmin.module.discount.dto.QSellerDiscountTarget;
import com.connectly.partnerAdmin.module.discount.dto.SellerDiscountTarget;
import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.connectly.partnerAdmin.module.discount.entity.QDiscountTarget.discountTarget;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;


@Repository
@RequiredArgsConstructor
public class DiscountTargetFetchRepositoryImpl implements DiscountTargetFetchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<DiscountTarget> fetchDiscountTargetEntities(List<Long> discountPolicyIds){
        return queryFactory.selectFrom(discountTarget)
                .where(discountPolicyIdIn(discountPolicyIds), activeYn())
                .fetch();
    }

    @Override
    public List<ProductDiscountTarget> fetchProductTargets(long discountPolicyId, Pageable pageable) {
        return queryFactory
                .select(
                        new QProductDiscountTarget(
                                discountTarget.discountPolicyId,
                                discountTarget.id,
                                discountTarget.targetId,
                                discountTarget.insertOperator,
                                discountTarget.insertDate
                        )
                )
                .from(discountTarget)
                .innerJoin(productGroup).on(discountTarget.targetId.eq(productGroup.id))
                .where(discountTargetHasPolicyEq(discountPolicyId), deleteYn(), activeYn())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    @Override
    public List<SellerDiscountTarget> fetchSellerTargets(long discountPolicyId, Pageable pageable) {
        return queryFactory
                .select(
                        new QSellerDiscountTarget(
                                discountTarget.discountPolicyId,
                                discountTarget.targetId,
                                seller.sellerName,
                                discountTarget.insertOperator,
                                discountTarget.insertDate
                        )
                )
                .from(discountTarget)
                .join(seller).on(seller.id.eq(discountTarget.targetId))
                .where(discountTargetHasPolicyEq(discountPolicyId), deleteYn(), activeYn())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    @Override
    public JPAQuery<Long> fetchProductTargetCountQuery(long discountPolicyId) {
        return queryFactory
                .select(discountTarget.count())
                .from(discountTarget)
                .join(productGroup).on(productGroup.id.eq(discountTarget.targetId))
                .where(discountTargetHasPolicyEq(discountPolicyId), deleteYn(), activeYn())
                .distinct();
    }

    @Override
    public JPAQuery<Long> fetchSellerTargetCountQuery(long discountPolicyId) {
        return queryFactory
                .select(discountTarget.count())
                .from(discountTarget)
                .join(seller).on(seller.id.eq(discountTarget.targetId))
                .where(discountTargetHasPolicyEq(discountPolicyId), deleteYn(), activeYn())
                .distinct();
    }





    private BooleanExpression discountPolicyIdIn(List<Long> discountPolicyIds){
        if(!discountPolicyIds.isEmpty()) return discountTarget.discountPolicyId.in(discountPolicyIds);
        return null;
    }


    private BooleanExpression discountTargetHasPolicyEq(long discountPolicyId){
        return discountTarget.discountPolicyId.eq(discountPolicyId);
    }

    private BooleanExpression activeYn(){
        return discountTarget.activeYn.eq(Yn.Y);
    }


    private BooleanExpression deleteYn(){
        return discountTarget.deleteYn.eq(Yn.N);
    }




}
