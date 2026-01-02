package com.connectly.partnerAdmin.module.qna.repository;

import com.connectly.partnerAdmin.module.brand.core.QBaseBrandContext;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.filter.BaseRoleFilter;
import com.connectly.partnerAdmin.module.common.repository.AbstractCommonRepository;
import com.connectly.partnerAdmin.module.common.strategy.search.SearchConditionStrategy;
import com.connectly.partnerAdmin.module.common.strategy.sort.SortConditionStrategy;
import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;
import com.connectly.partnerAdmin.module.qna.dto.fetch.*;
import com.connectly.partnerAdmin.module.qna.dto.filter.QnaFilter;
import com.connectly.partnerAdmin.module.qna.entity.QQnaImage;
import com.connectly.partnerAdmin.module.qna.entity.Qna;
import com.connectly.partnerAdmin.module.qna.enums.QnaStatus;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.connectly.partnerAdmin.module.brand.entity.QBrand.brand;
import static com.connectly.partnerAdmin.module.order.entity.QOrder.order;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.image.QOrderSnapShotProductGroupImage.orderSnapShotProductGroupImage;
import static com.connectly.partnerAdmin.module.order.entity.snapshot.option.QOrderSnapShotOptionDetail.orderSnapShotOptionDetail;
import static com.connectly.partnerAdmin.module.product.entity.group.QProductGroup.productGroup;
import static com.connectly.partnerAdmin.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.connectly.partnerAdmin.module.qna.entity.QQna.qna;
import static com.connectly.partnerAdmin.module.qna.entity.QQnaAnswer.qnaAnswer;
import static com.connectly.partnerAdmin.module.qna.entity.QQnaImage.qnaImage;
import static com.connectly.partnerAdmin.module.qna.entity.QQnaOrder.qnaOrder;
import static com.connectly.partnerAdmin.module.qna.entity.QQnaProduct.qnaProduct;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;
import static com.connectly.partnerAdmin.module.user.entity.QUsers.users;

@Repository
public class QnaFetchRepositoryImpl extends AbstractCommonRepository implements QnaFetchRepository {


    protected QnaFetchRepositoryImpl(JPAQueryFactory queryFactory, SearchConditionStrategy searchConditionStrategy, SortConditionStrategy sortConditionStrategy) {
        super(queryFactory, searchConditionStrategy, sortConditionStrategy);
    }

    @Override
    public Optional<QnaType> fetchQnaType(long qnaId, Optional<Long> sellerIdOpt) {
        return Optional.ofNullable(
                getQueryFactory()
                        .select(qna.qnaType)
                        .from(qna)
                        .where(qnaIdEq(qnaId), sellerIdEq(sellerIdOpt))
                        .fetchOne());
    }

    @Override
    public Optional<Qna> fetchQnaEntity(long qnaId, Optional<Long> sellerIdOpt) {
        return Optional.ofNullable(
                getQueryFactory()
                        .selectFrom(qna)
                        .leftJoin(qna.qnaProduct, qnaProduct).fetchJoin()
                        .leftJoin(qna.qnaOrder, qnaOrder).fetchJoin()
                        .leftJoin(qna.qnaImages, qnaImage).fetchJoin()
                        .where(qna.id.eq(qnaId), sellerIdEq(sellerIdOpt))
                        .fetchOne()
        );
    }

    @Override
    public Optional<DetailQnaResponse> fetchProductQna(long qnaId, Optional<Long> sellerIdOpt) {
        return Optional.ofNullable(
                getQueryFactory()
                        .from(qna)
                        .leftJoin(qnaAnswer).on(qnaAnswer.qna.id.eq(qna.id))
                        .innerJoin(qna.qnaProduct, qnaProduct)
                        .innerJoin(productGroup).on(productGroup.id.eq(qnaProduct.productGroupId))
                        .innerJoin(brand)
                            .on(brand.id.eq(productGroup.productGroupDetails.brandId))
                        .innerJoin(productGroup.images, productGroupImage)
                            .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                            .on(productGroupImage.deleteYn.eq(Yn.N))
                        .innerJoin(seller)
                            .on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                        .innerJoin(users).on(users.id.eq(qna.userId))
                        .where(
                                qnaIdEq(qnaId), sellerIdEq(sellerIdOpt)
                        )
                        .transform(
                                GroupBy.groupBy(qna.id).as(
                                        new QDetailQnaResponse(
                                                new QFetchQnaResponse(
                                                        qna.id,
                                                        qna.qnaContents,
                                                        qna.privateYn,
                                                        qna.qnaStatus,
                                                        qna.qnaType,
                                                        qna.qnaDetailType,
                                                        seller.sellerName,
                                                        new QUserInfoQnaDto(
                                                                users.id.coalesce(1L),
                                                                users.name.coalesce(qna.insertOperator),
                                                                users.phoneNumber.coalesce(""),
                                                                users.email.coalesce(""),
                                                                users.gender
                                                        ),
                                                        new QProductQnaTarget(
                                                                productGroup.id,
                                                                productGroup.productGroupDetails.productGroupName,
                                                                productGroupImage.imageDetail.imageUrl,
                                                                new QBaseBrandContext(
                                                                        brand.id,
                                                                        brand.brandName
                                                                )
                                                        ),
                                                        qna.insertDate,
                                                        qna.updateDate
                                                ),
                                                GroupBy.set(
                                                        new QAnswerQnaResponse(
                                                                qnaAnswer.id,
                                                                qnaAnswer.qnaParentId,
                                                                qnaAnswer.qnaWriterType,
                                                                qnaAnswer.qnaContents,
                                                                qnaAnswer.insertOperator,
                                                                qnaAnswer.updateOperator,
                                                                qnaAnswer.insertDate,
                                                                qnaAnswer.updateDate
                                                        )
                                                )
                                        )
                                )
                        ).get(qnaId));

    }

    @Override
    public Optional<DetailQnaResponse> fetchOrderQna(long qnaId, Optional<Long> sellerIdOpt) {

        QQnaImage qnaImageAnswer = new QQnaImage("qnaImageAnswer");


        return Optional.ofNullable(
                getQueryFactory()
                        .from(qna)
                        .leftJoin(qnaAnswer).on(qnaAnswer.qna.id.eq(qna.id))
                        .innerJoin(qna.qnaOrder, qnaOrder)
                        .leftJoin(qna.qnaImages, qnaImage)
                            .on(qnaImage.deleteYn.eq(Yn.N))
                        .leftJoin(qnaAnswer.qnaImages, qnaImageAnswer)
                            .on(qnaImageAnswer.deleteYn.eq(Yn.N))

                        .innerJoin(order).on(order.id.eq(qnaOrder.orderId))

                        .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                        .innerJoin(brand)
                            .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                        .innerJoin(seller).on(seller.id.eq(qna.sellerId))
                        .innerJoin(users).on(users.id.eq(qna.userId))

                        .join(order.orderSnapShotProductGroupImages, orderSnapShotProductGroupImage)
                            .on(orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                        .leftJoin(order.orderSnapShotOptionDetails, orderSnapShotOptionDetail)
                        .where(
                                qnaIdEq(qnaId), sellerIdEq(sellerIdOpt)
                        )
                        .transform(
                                GroupBy.groupBy(qna.id).as(
                                        new QDetailQnaResponse(
                                                new QFetchQnaResponse(
                                                        qna.id,
                                                        qna.qnaContents,
                                                        qna.privateYn,
                                                        qna.qnaStatus,
                                                        qna.qnaType,
                                                        qna.qnaDetailType,
                                                        seller.sellerName,
                                                        new QUserInfoQnaDto(
                                                                users.id.coalesce(0L),
                                                                users.name.coalesce(qna.insertOperator),
                                                                users.phoneNumber.coalesce(""),
                                                                users.email.coalesce(""),
                                                                users.gender
                                                        ),
                                                        new QOrderQnaTarget(
                                                                orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                                                                orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                                                                orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.imageUrl,
                                                                new QBaseBrandContext(
                                                                        brand.id,
                                                                        brand.brandName
                                                                ),
                                                                order.payment.id,
                                                                order.id,
                                                                GroupBy.list(orderSnapShotOptionDetail.snapShotOptionDetail.optionValue)
                                                        ),
                                                        qna.insertDate,
                                                        qna.updateDate
                                                ),
                                                GroupBy.set(
                                                        new QAnswerQnaResponse(
                                                                qnaAnswer.id,
                                                                qnaAnswer.qnaParentId,
                                                                qnaAnswer.qnaWriterType,
                                                                qnaAnswer.qnaContents,
                                                                qnaAnswer.insertOperator,
                                                                qnaAnswer.updateOperator,
                                                                qnaAnswer.insertDate,
                                                                qnaAnswer.updateDate
                                                        )
                                                ),
                                                GroupBy.list(
                                                        new QQnaImageDto(
                                                                qnaImage.qnaIssueType,
                                                                qnaImage.id,
                                                                qnaImage.qna.id,
                                                                qnaImage.qnaAnswer.id,
                                                                qnaImage.imageUrl,
                                                                qnaImage.displayOrder
                                                        )
                                                ),
                                                GroupBy.list(
                                                        new QQnaImageDto(
                                                                qnaImageAnswer.qnaIssueType,
                                                                qnaImageAnswer.id,
                                                                qnaImageAnswer.qna.id,
                                                                qnaImageAnswer.qnaAnswer.id,
                                                                qnaImageAnswer.imageUrl,
                                                                qnaImageAnswer.displayOrder
                                                        )
                                                )
                                        )
                                )
                        ).get(qnaId));
    }

    @Override
    public List<FetchQnaResponse> fetchQnas(QnaFilter filter, Pageable pageable){
        if(filter.isProductQna()) return fetchProductQnas(filter, pageable);
        return fetchOrderQnas(filter, pageable);
    }

    @Override
    public Optional<QnaCountResponse> fetchTodayQnaCountQuery(BaseRoleFilter filter) {
        LocalDate today = LocalDate.now();

        return Optional.ofNullable(getQueryFactory()
                .select(
                        new QQnaCountResponse(
                                getQnaTypeCaseWhenQuery(QnaType.ORDER),
                                getQnaTypeCaseWhenQuery(QnaType.PRODUCT)
                        )
                )
                .from(qna)
                .where(
                        qna.qnaStatus.eq(QnaStatus.OPEN),
                        sellerIdEq(Optional.ofNullable(filter.getSellerId()))
                )
                .groupBy(qna.qnaType)
                .fetchOne());
    }

    private NumberExpression<Long> getQnaTypeCaseWhenQuery(QnaType qnaType) {
        return Expressions.numberTemplate(
                Long.class,
                "sum(case when {0} = {1} then 1 else 0 end)",
                qna.qnaType,
                qnaType
        );
    }


    @Override
    public JPAQuery<Long> fetchQnaCountQuery(QnaFilter filter) {
        return getQueryFactory()
                .select(qna.count())
                .from(qna)
                .where(
                        qnaStatusEq(filter), qnaTypeEq(filter),
                        qnaDetailTypeEq(filter), betweenTime(filter),
                        sellerIdEq(Optional.ofNullable(filter.getSellerId()))
                )
                .distinct();
    }

    private List<Long> fetchQnaIds(QnaFilter filter, Pageable pageable){
        return getQueryFactory()
                .select(qna.id)
                .from(qna)
                .join(seller).on(seller.id.eq(qna.sellerId))
                .leftJoin(users).on(users.id.eq(qna.userId))
                .where(
                        qnaStatusEq(filter), qnaTypeEq(filter),
                        qnaDetailTypeEq(filter), betweenTime(filter), isCursorRead(filter),
                        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
                        sellerIdEq(Optional.ofNullable(filter.getSellerId()))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qna.id.desc())
                .fetch();
    }

    private List<FetchQnaResponse> fetchProductQnas(QnaFilter filter, Pageable pageable){
        List<Long> qnaIds = fetchQnaIds(filter, pageable);
        if(qnaIds.isEmpty()) return Collections.emptyList();

        return getQueryFactory()
                .from(qna)
                .leftJoin(qnaAnswer).on(qnaAnswer.qna.id.eq(qna.id))
                .innerJoin(qna.qnaProduct, qnaProduct)
                .innerJoin(productGroup).on(productGroup.id.eq(qnaProduct.productGroupId))
                .innerJoin(brand)
                .on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .innerJoin(productGroup.images, productGroupImage)
                .on(productGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(seller)
                .on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                .innerJoin(users).on(users.id.eq(qna.userId))
                .where(
                        qnaIdIn(qnaIds)
                )
                .transform(
                        GroupBy.groupBy(qna.id).list(
                                new QFetchQnaResponse(
                                        qna.id,
                                        qna.qnaContents,
                                        qna.privateYn,
                                        qna.qnaStatus,
                                        qna.qnaType,
                                        qna.qnaDetailType,
                                        seller.sellerName,
                                        new QUserInfoQnaDto(
                                                users.id.coalesce(0L),
                                                users.name.coalesce(qna.insertOperator),
                                                users.phoneNumber.coalesce(""),
                                                users.email.coalesce(""),
                                                users.gender
                                        ),
                                        new QProductQnaTarget(
                                                productGroup.id,
                                                productGroup.productGroupDetails.productGroupName,
                                                productGroupImage.imageDetail.imageUrl,
                                                new QBaseBrandContext(
                                                        brand.id,
                                                        brand.brandName
                                                )
                                        ),
                                        qna.insertDate,
                                        qna.updateDate
                                )
                        )
                );
    }



    private List<FetchQnaResponse> fetchOrderQnas(QnaFilter filter, Pageable pageable){
        List<Long> qnaIds = fetchQnaIds(filter, pageable);
        if(qnaIds.isEmpty()) return Collections.emptyList();

        QQnaImage qnaImageAnswer = new QQnaImage("qnaImageAnswer");

        return getQueryFactory()
                .from(qna)
                .leftJoin(qnaAnswer).on(qnaAnswer.qna.id.eq(qna.id))
                .innerJoin(qna.qnaOrder, qnaOrder)
                .leftJoin(qna.qnaImages, qnaImage)
                .on(qnaImage.deleteYn.eq(Yn.N))
                .leftJoin(qnaAnswer.qnaImages, qnaImageAnswer)
                    .on(qnaImageAnswer.deleteYn.eq(Yn.N))
                .innerJoin(order).on(order.id.eq(qnaOrder.orderId))
                .innerJoin(order.orderSnapShotProductGroup, orderSnapShotProductGroup)
                .innerJoin(brand)
                .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .innerJoin(seller).on(seller.id.eq(qna.sellerId))
                .innerJoin(users).on(users.id.eq(qna.userId))
                .join(order.orderSnapShotProductGroupImages, orderSnapShotProductGroupImage)
                .on(orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.productGroupImageType.eq(ProductGroupImageType.MAIN))
                .leftJoin(order.orderSnapShotOptionDetails, orderSnapShotOptionDetail)
                .where(
                        qnaIdIn(qnaIds)
                )
                .transform(
                        GroupBy.groupBy(qna.id).list(
                                new QFetchQnaResponse(
                                        qna.id,
                                        qna.qnaContents,
                                        qna.privateYn,
                                        qna.qnaStatus,
                                        qna.qnaType,
                                        qna.qnaDetailType,
                                        seller.sellerName,
                                        new QUserInfoQnaDto(
                                                users.id.coalesce(0L),
                                                users.name.coalesce(qna.insertOperator),
                                                users.phoneNumber.coalesce(""),
                                                users.email.coalesce(""),
                                                users.gender
                                        ),
                                        new QOrderQnaTarget(
                                                orderSnapShotProductGroup.snapShotProductGroup.productGroupId,
                                                orderSnapShotProductGroup.snapShotProductGroup.productGroupName,
                                                orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail.imageUrl,
                                                new QBaseBrandContext(
                                                        brand.id,
                                                        brand.brandName
                                                ),
                                                order.payment.id,
                                                order.id,
                                                GroupBy.list(orderSnapShotOptionDetail.snapShotOptionDetail.optionValue)
                                        ),
                                        qna.insertDate,
                                        qna.updateDate
                                )
                        )
                );
    }

    private BooleanExpression qnaStatusEq(QnaFilter filter){
        if(filter.getQnaStatus() != null) return qna.qnaStatus.eq(filter.getQnaStatus());
        return null;
    }


    private BooleanExpression qnaTypeEq(QnaFilter filter){
        if(filter.getQnaType() != null) return qna.qnaType.eq(filter.getQnaType());
        return null;
    }

    private BooleanExpression qnaDetailTypeEq(QnaFilter filter){
        if(filter.getQnaDetailType() != null) return qna.qnaDetailType.eq(filter.getQnaDetailType());
        return null;
    }

    private BooleanExpression betweenTime(QnaFilter filter){
        return qna.insertDate.between(filter.getStartDate(), filter.getEndDate());
    }


    private BooleanExpression sellerIdEq(Optional<Long> sellerIdOpt){
        return sellerIdOpt.map(qna.sellerId::eq).orElse(null);
    }



    private BooleanExpression qnaIdEq(long qnaId){
        return qna.id.eq(qnaId);
    }

    private BooleanExpression isCursorRead(QnaFilter dto) {
        if (dto.getLastDomainId() != null && dto.getLastDomainId() >0) {
            return QnaIdLt(dto.getLastDomainId());
        }
        return null;
    }

    private BooleanExpression QnaIdLt(long qnaId) {
        return qnaId > 0 ? qna.id.lt(qnaId) : null;
    }

    private BooleanExpression qnaIdIn(List<Long> qnaIds){
        return qna.id.in(qnaIds);
    }

}
