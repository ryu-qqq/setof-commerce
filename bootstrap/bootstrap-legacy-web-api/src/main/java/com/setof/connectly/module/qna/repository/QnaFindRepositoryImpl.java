package com.setof.connectly.module.qna.repository;


import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.notification.dto.qna.QQnaSheet;
import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.order.dto.filter.OrderFilter;
import com.setof.connectly.module.order.entity.snapshot.group.QOrderSnapShotProductGroup;
import com.setof.connectly.module.product.dto.brand.QBrandDto;
import com.setof.connectly.module.product.entity.group.QProductGroup;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import com.setof.connectly.module.qna.dto.*;
import com.setof.connectly.module.qna.dto.filter.QnaFilter;
import com.setof.connectly.module.qna.entity.QQnaProduct;
import com.setof.connectly.module.qna.entity.Qna;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.enums.QnaType;
import com.setof.connectly.module.seller.entity.QSeller;
import com.setof.connectly.module.seller.entity.QSellerBusinessInfo;
import com.setof.connectly.module.user.entity.QUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.order.entity.order.QOrder.order;
import static com.setof.connectly.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;
import static com.setof.connectly.module.order.entity.snapshot.image.QOrderSnapShotProductGroupImage.orderSnapShotProductGroupImage;
import static com.setof.connectly.module.order.entity.snapshot.option.QOrderSnapShotOptionDetail.orderSnapShotOptionDetail;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.setof.connectly.module.qna.entity.QQna.qna;
import static com.setof.connectly.module.qna.entity.QQnaAnswer.qnaAnswer;
import static com.setof.connectly.module.qna.entity.QQnaImage.qnaImage;
import static com.setof.connectly.module.qna.entity.QQnaOrder.qnaOrder;
import static com.setof.connectly.module.qna.entity.QQnaProduct.qnaProduct;
import static com.setof.connectly.module.seller.entity.QSeller.seller;
import static com.setof.connectly.module.seller.entity.QSellerBusinessInfo.sellerBusinessInfo;
import static com.setof.connectly.module.user.entity.QUsers.users;

@Repository
@RequiredArgsConstructor
public class QnaFindRepositoryImpl implements QnaFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> fetchQnaProductIds(long productGroupId, Pageable pageable) {
        return queryFactory
                .select(
                        qna.id
                       )
                .from(qnaProduct)
                .leftJoin(qna)
                    .on(qna.id.eq(qnaProduct.qnaId))
                .leftJoin(users)
                    .on(users.id.eq(qna.userId))
                .where(productGroupIdEq(productGroupId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public JPAQuery<Long> fetchQnaCountQuery(long productGroupId) {
        return queryFactory
                .select(qnaProduct.count())
                .from(qnaProduct)
                .where(
                        productGroupIdEq(productGroupId)
                )
                .distinct();
    }

    @Override
    public List<QnaResponse> fetchQnas(List<Long> qnaIds) {
        return queryFactory.from(qna)
                .leftJoin(qnaAnswer)
                    .on(qnaAnswer.qnaId.eq(qna.id))
                .leftJoin(qnaImage)
                    .on(qnaImage.qnaId.eq(qna.id))
                    .on(qnaImage.qnaAnswerId.eq(qnaAnswer.id))
                    .on(qnaImage.deleteYn.eq(Yn.N))
                .leftJoin(users)
                    .on(users.id.eq(qna.userId))
                .where(
                        qnaIdIn(qnaIds)
                )
                .transform(
                        GroupBy.groupBy(qna.id).list(
                                new QQnaResponse(
                                        new QFetchQnaResponse(
                                                qna.id,
                                                qna.qnaContents,
                                                qna.privateYn,
                                                qna.qnaStatus,
                                                qna.qnaType,
                                                qna.qnaDetailType,
                                                qna.userType,
                                                qna.userId,
                                                users.name,
                                                qna.insertDate,
                                                qna.updateDate
                                        ),
                                        GroupBy.set(
                                                new QAnswerQnaResponse(
                                                        qnaAnswer.id,
                                                        qnaAnswer.qnaParentId,
                                                        qnaAnswer.qnaWriterType,
                                                        qnaAnswer.qnaContents,
                                                        qnaAnswer.insertDate,
                                                        qnaAnswer.updateDate
                                                )
                                        )

                                )
                        )
                );
    }

    @Override
    public Optional<Qna> fetchQnaEntity(long qnaId, long userId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qna)
                        .where(qnaIdEq(qnaId), userIdEq(userId))
                        .fetchFirst()
        );
    }

    @Override
    public Optional<QnaStatus> fetchQnaStatus(long qnaId) {
        return Optional.ofNullable(
                queryFactory.select(qna.qnaStatus)
                        .from(qna)
                        .where(qnaIdEq(qnaId))
                        .fetchFirst()
        );
    }

    @Override
    public List<QnaResponse> fetchMyQnas(long userId, QnaFilter filter, Pageable pageable){
        if(filter.isProductQna()) return fetchProductQnas(userId, filter, pageable);
        return fetchOrderQnas(userId, filter, pageable);
    }

    @Override
    public Optional<QnaSheet> fetchOrderQnaSheet(long qnaId) {
        return Optional.ofNullable(
                queryFactory.select(
                        new QQnaSheet(
                                qna.id,
                                qna.qnaType,
                                qna.qnaDetailType,
                                orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                                qnaOrder.orderId,
                                sellerBusinessInfo.csPhoneNumber
                        )
                        )
                        .from(qna)
                        .innerJoin(qnaOrder)
                            .on(qnaOrder.qnaId.eq(qnaId))
                        .innerJoin(orderSnapShotProductGroup)
                            .on(orderSnapShotProductGroup.orderId.eq(qnaOrder.orderId))
                        .innerJoin(sellerBusinessInfo)
                            .on(sellerBusinessInfo.id.eq(orderSnapShotProductGroup.snapShotProductGroup.sellerId))
                        .where(qnaIdEq(qnaId))
                        .fetchFirst()
        );
    }

    @Override
    public Optional<QnaSheet> fetchProductQnaSheet(long qnaId) {
        return Optional.ofNullable(
                queryFactory.select(
                                new QQnaSheet(
                                        qna.id,
                                        qna.qnaType,
                                        qna.qnaDetailType,
                                        productGroup.productGroupDetails.productGroupName,
                                        qnaProduct.productGroupId,
                                        sellerBusinessInfo.csPhoneNumber
                                )
                        )
                        .from(qna)
                        .innerJoin(qnaProduct)
                            .on(qnaProduct.qnaId.eq(qnaId))
                        .innerJoin(productGroup)
                            .on(productGroup.id.eq(qnaProduct.productGroupId))
                        .innerJoin(sellerBusinessInfo)
                            .on(sellerBusinessInfo.id.eq(productGroup.productGroupDetails.sellerId))
                        .where(qnaIdEq(qnaId))
                        .fetchFirst()
        );
    }


    private List<QnaResponse> fetchProductQnas(long userId, QnaFilter filter, Pageable pageable){
        List<Long> qnaIds = fetchQnaIds(userId, filter, pageable);
        return queryFactory.from(qna)
                .leftJoin(qnaAnswer)
                    .on(qnaAnswer.qnaId.eq(qna.id))
                .join(qnaProduct)
                    .on(qna.id.eq(qnaProduct.qnaId))
                .join(productGroup)
                    .on(qnaProduct.productGroupId.eq(productGroup.id))
                .join(brand)
                    .on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .join(productGroupImage)
                    .on(productGroupImage.productGroup.id.eq(productGroup.id))
                    .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                    .on(productGroupImage.deleteYn.eq(Yn.N))

                .join(seller)
                    .on(seller.id.eq(qna.sellerId))
                .leftJoin(users)
                    .on(users.id.eq(qna.userId))
                .where(
                        qnaIdIn(qnaIds)
                )
                .orderBy(qna.id.desc())
                .transform(
                        GroupBy.groupBy(qna.id).list(
                                new QQnaResponse(
                                        new QFetchQnaResponse(
                                                qna.id,
                                                qna.qnaContents,
                                                qna.privateYn,
                                                qna.qnaStatus,
                                                qna.qnaType,
                                                qna.qnaDetailType,
                                                qna.userType,
                                                new QProductQnaTarget(
                                                        productGroup.id,
                                                        productGroup.productGroupDetails.productGroupName,
                                                        productGroupImage.imageDetail.imageUrl,
                                                        new QBrandDto(
                                                                brand.id,
                                                                brand.brandName
                                                        )
                                                ),
                                                qna.userId,
                                                users.name,
                                                qna.insertDate,
                                                qna.updateDate
                                        ),
                                        GroupBy.set(
                                                new QAnswerQnaResponse(
                                                        qnaAnswer.id,
                                                        qnaAnswer.qnaParentId,
                                                        qnaAnswer.qnaWriterType,
                                                        qnaAnswer.qnaContents,
                                                        qnaAnswer.insertDate,
                                                        qnaAnswer.updateDate
                                                )
                                        )

                                )
                        )
                );
    }

    private List<QnaResponse> fetchOrderQnas(long userId, QnaFilter filterDto, Pageable pageable){
        List<Long> qnaIds = fetchQnaIds(userId, filterDto, pageable);
        return queryFactory.from(qna)
                .leftJoin(qnaAnswer)
                    .on(qnaAnswer.qnaId.eq(qna.id))
                .join(qnaOrder)
                    .on(qna.id.eq(qnaOrder.qnaId))
                .leftJoin(qnaImage)
                    .on(qnaImage.qnaId.eq(qna.id))
                    .on(qnaImage.qnaAnswerId.eq(qnaAnswer.id))
                    .on(qnaImage.deleteYn.eq(Yn.N))
                .join(order)
                    .on(order.id.eq(qnaOrder.orderId))
                .join(orderSnapShotProductGroup)
                    .on(qnaOrder.orderId.eq(orderSnapShotProductGroup.orderId))
                .join(brand)
                    .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .join(seller)
                    .on(seller.id.eq(qna.sellerId))
                .leftJoin(users)
                    .on(users.id.eq(qna.userId))
                .join(orderSnapShotProductGroupImage)
                    .on(orderSnapShotProductGroupImage.orderId.eq(qnaOrder.orderId))
                    .on(orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                .leftJoin(orderSnapShotOptionDetail)
                    .on(orderSnapShotOptionDetail.orderId.eq(qnaOrder.orderId))
                .where(
                        qnaIdIn(qnaIds)
                )
                .orderBy(qna.id.desc())
                .transform(
                        GroupBy.groupBy(qna.id).list(
                                new QQnaResponse(
                                        new QFetchQnaResponse(
                                                qna.id,
                                                qna.qnaContents,
                                                qna.privateYn,
                                                qna.qnaStatus,
                                                qna.qnaType,
                                                qna.qnaDetailType,
                                                qna.userType,
                                                new QOrderQnaTarget(
                                                        orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                                                        orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                                                        orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.imageUrl,
                                                        new QBrandDto(
                                                                brand.id,
                                                                brand.brandName
                                                        ),
                                                        order.paymentId,
                                                        order.id,
                                                        order.orderAmount,
                                                        order.quantity,
                                                        GroupBy.list(orderSnapShotOptionDetail.snapShotOptionDetail.optionValue)
                                                ),
                                                qna.userId,
                                                users.name,
                                                qna.insertDate,
                                                qna.updateDate
                                        ),
                                        GroupBy.set(
                                                new QAnswerQnaResponse(
                                                        qnaAnswer.id,
                                                        qnaAnswer.qnaParentId,
                                                        qnaAnswer.qnaWriterType,
                                                        qnaAnswer.qnaContents,
                                                        qnaAnswer.insertDate,
                                                        qnaAnswer.updateDate
                                                )
                                        )

                                )
                        )
                );
    }

    private List<Long> fetchQnaIds(long userId, QnaFilter filter, Pageable pageable){
        return queryFactory
                .select(qna.id)
                .from(qna)
                .join(seller).on(seller.id.eq(qna.sellerId))
                .leftJoin(users).on(users.id.eq(qna.userId))
                .where(
                        userIdEq(userId), qnaIdLt(filter.getLastDomainId()),
                        qnaTypeEq(filter.getQnaType()), betweenTime(filter)
                )
                .limit(pageable.getPageSize() +1)
                .orderBy(qna.id.desc())
                .fetch();
    }



    private BooleanExpression productGroupIdEq(long productGroupId){
        return qnaProduct.productGroupId.eq(productGroupId);
    }


    private BooleanExpression betweenTime(QnaFilter filter){
        return qna.insertDate.between(filter.getStartDate(), filter.getEndDate());
    }

    private BooleanExpression userIdEq(long userId){
        return qna.userId.eq(userId);
    }

    private BooleanExpression qnaIdEq(long qnaId){
        return qna.id.eq(qnaId);
    }
    private BooleanExpression qnaIdIn(List<Long> qnaIds){
        return qna.id.in(qnaIds);
    }

    private BooleanExpression qnaIdLt(Long lastDomainId){
         if(lastDomainId != null) return qna.id.lt(lastDomainId);
         return null;
    }

    private BooleanExpression qnaTypeEq(QnaType qnaType){
        return qna.qnaType.eq(qnaType);
    }

}
