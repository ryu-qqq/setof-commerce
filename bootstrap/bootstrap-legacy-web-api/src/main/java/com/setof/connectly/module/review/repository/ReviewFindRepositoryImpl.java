package com.setof.connectly.module.review.repository;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.category.entity.QCategory.category;
import static com.setof.connectly.module.order.entity.order.QOrder.order;
import static com.setof.connectly.module.order.entity.snapshot.group.QOrderSnapShotProductGroup.orderSnapShotProductGroup;
import static com.setof.connectly.module.order.entity.snapshot.image.QOrderSnapShotProductGroupImage.orderSnapShotProductGroupImage;
import static com.setof.connectly.module.order.entity.snapshot.option.QOrderSnapShotOptionDetail.orderSnapShotOptionDetail;
import static com.setof.connectly.module.order.entity.snapshot.option.QOrderSnapShotOptionGroup.orderSnapShotOptionGroup;
import static com.setof.connectly.module.order.entity.snapshot.option.QOrderSnapShotProductOption.orderSnapShotProductOption;
import static com.setof.connectly.module.payment.entity.QPayment.payment;
import static com.setof.connectly.module.review.entity.QProductRatingStats.productRatingStats;
import static com.setof.connectly.module.review.entity.QReview.review;
import static com.setof.connectly.module.review.entity.QReviewImage.reviewImage;
import static com.setof.connectly.module.search.entity.QProductScore.productScore;
import static com.setof.connectly.module.user.entity.QUsers.users;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.common.repository.AbstractCommonRepository;
import com.setof.connectly.module.display.enums.component.OrderType;
import com.setof.connectly.module.product.dto.brand.QBrandDto;
import com.setof.connectly.module.product.dto.option.QOptionDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import com.setof.connectly.module.review.dto.QReviewDto;
import com.setof.connectly.module.review.dto.QReviewImageDto;
import com.setof.connectly.module.review.dto.ReviewDto;
import com.setof.connectly.module.review.dto.query.QReviewQueryDto;
import com.setof.connectly.module.review.dto.query.ReviewQueryDto;
import com.setof.connectly.module.review.entity.Review;
import com.setof.connectly.module.review.filter.ReviewFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewFindRepositoryImpl extends AbstractCommonRepository
        implements ReviewFindRepository {
    private final JPAQueryFactory queryFactory;

    private List<ReviewQueryDto> fetchReviewIds(ReviewFilter filter, Pageable pageable) {
        List<OrderSpecifier<?>> orders =
                createOrderSpecifiersFromPageable(review, filter.getOrderType());

        return queryFactory
                .from(review)
                .innerJoin(orderSnapShotProductGroup)
                .on(orderSnapShotProductGroup.orderId.eq(review.orderId))
                .leftJoin(productScore)
                .on(
                        productScore.id.eq(
                                orderSnapShotProductGroup.snapShotProductGroup.productGroupId))
                .where(productGroupIdEq(filter.getProductGroupId()), deleteYn())
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                        GroupBy.groupBy(review.id)
                                .list(
                                        new QReviewQueryDto(
                                                review.id,
                                                review.rating,
                                                productScore.score.coalesce(0.0))));
    }

    @Override
    public List<ReviewDto> fetchReviews(ReviewFilter filter, Pageable pageable) {
        List<ReviewQueryDto> reviewQueries = fetchReviewIds(filter, pageable);
        List<Long> reviewIds = extractReviewIds(reviewQueries);

        return queryFactory
                .from(review)
                .innerJoin(users)
                .on(users.id.eq(review.userId))
                .innerJoin(order)
                .on(order.id.eq(review.orderId))
                .innerJoin(payment)
                .on(payment.id.eq(order.paymentId))
                .innerJoin(orderSnapShotProductGroup)
                .on(review.orderId.eq(orderSnapShotProductGroup.orderId))
                .innerJoin(orderSnapShotProductGroupImage)
                .on(orderSnapShotProductGroupImage.orderId.eq(review.orderId))
                .on(
                        orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail
                                .productGroupImageType.eq(ProductGroupImageType.MAIN))
                .innerJoin(brand)
                .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .innerJoin(category)
                .on(category.id.eq(orderSnapShotProductGroup.snapShotProductGroup.categoryId))
                .leftJoin(reviewImage)
                .on(reviewImage.reviewId.eq(review.id))
                .leftJoin(orderSnapShotProductOption)
                .on(orderSnapShotProductOption.orderId.eq(review.orderId))
                .leftJoin(orderSnapShotOptionGroup)
                .on(orderSnapShotOptionGroup.orderId.eq(review.orderId))
                .on(
                        orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionGroupId))
                .leftJoin(orderSnapShotOptionDetail)
                .on(orderSnapShotOptionDetail.orderId.eq(review.orderId))
                .on(
                        orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionDetailId))
                .where(reviewIdIn(reviewIds), deleteYn())
                .transform(
                        GroupBy.groupBy(review.id)
                                .list(
                                        new QReviewDto(
                                                review.id,
                                                review.orderId,
                                                users.name,
                                                review.rating,
                                                review.content,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupId,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupName,
                                                orderSnapShotProductGroupImage
                                                        .snapShotProductGroupImage
                                                        .imageDetail
                                                        .imageUrl,
                                                new QBrandDto(brand.id, brand.brandName),
                                                category.id,
                                                category.displayName,
                                                GroupBy.set(
                                                        new QReviewImageDto(
                                                                reviewImage.id,
                                                                reviewImage.reviewImageType,
                                                                reviewImage.imageUrl)),
                                                review.insertDate,
                                                payment.paymentDetails.paymentDate,
                                                GroupBy.set(
                                                        new QOptionDto(
                                                                orderSnapShotOptionGroup
                                                                        .snapShotOptionGroup
                                                                        .optionGroupId,
                                                                orderSnapShotOptionDetail
                                                                        .snapShotOptionDetail
                                                                        .optionDetailId,
                                                                orderSnapShotOptionGroup
                                                                        .snapShotOptionGroup
                                                                        .optionName,
                                                                orderSnapShotOptionDetail
                                                                        .snapShotOptionDetail
                                                                        .optionValue)))));
    }

    @Override
    public JPAQuery<Long> fetchReviewCountQuery(Long productGroupId) {
        return queryFactory
                .select(review.count())
                .from(review)
                .leftJoin(orderSnapShotProductGroup)
                .on(orderSnapShotProductGroup.orderId.eq(review.orderId))
                .where(productGroupIdEq(productGroupId), deleteYn())
                .distinct();
    }

    @Override
    public Optional<Review> fetchReviewEntity(long reviewId, long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(review)
                        .where(reviewIdEq(reviewId), userIdEq(userId))
                        .fetchFirst());
    }

    @Override
    public Optional<Double> fetchAverageRating(long productGroupId) {
        return Optional.ofNullable(
                queryFactory
                        .select(productRatingStats.averageRating.coalesce(0.0))
                        .from(productRatingStats)
                        .where(productGroupIdEq(productGroupId))
                        .fetchFirst());
    }

    @Override
    public boolean isReviewAlreadyWritten(long orderId, long userId) {
        Long aLong =
                queryFactory
                        .select(review.id)
                        .from(review)
                        .where(orderIdEq(orderId), userIdEq(userId), deleteYn())
                        .fetchFirst();

        return aLong != null;
    }

    @Override
    public boolean isReWriteReview(long orderId, long userId) {
        List<Long> fetch =
                queryFactory
                        .select(review.id)
                        .from(review)
                        .where(orderIdEq(orderId), userIdEq(userId))
                        .fetch();

        return fetch.size() > 1;
    }

    @Override
    public List<ReviewDto> fetchMyReviews(long userId, Long lastDomainId, Pageable pageable) {
        List<Long> reviewIds = fetchMyReviewIds(userId, lastDomainId, pageable);

        return queryFactory
                .from(review)
                .innerJoin(users)
                .on(users.id.eq(review.userId))
                .innerJoin(order)
                .on(order.id.eq(review.orderId))
                .innerJoin(payment)
                .on(payment.id.eq(order.paymentId))
                .innerJoin(orderSnapShotProductGroup)
                .on(review.orderId.eq(orderSnapShotProductGroup.orderId))
                .innerJoin(orderSnapShotProductGroupImage)
                .on(orderSnapShotProductGroupImage.orderId.eq(review.orderId))
                .on(
                        orderSnapShotProductGroupImage.snapShotProductGroupImage.imageDetail
                                .productGroupImageType.eq(ProductGroupImageType.MAIN))
                .innerJoin(brand)
                .on(brand.id.eq(orderSnapShotProductGroup.snapShotProductGroup.brandId))
                .innerJoin(category)
                .on(category.id.eq(orderSnapShotProductGroup.snapShotProductGroup.categoryId))
                .leftJoin(reviewImage)
                .on(reviewImage.reviewId.eq(review.id))
                .leftJoin(orderSnapShotProductOption)
                .on(orderSnapShotProductOption.orderId.eq(review.orderId))
                .leftJoin(orderSnapShotOptionGroup)
                .on(orderSnapShotOptionGroup.orderId.eq(review.orderId))
                .on(
                        orderSnapShotOptionGroup.snapShotOptionGroup.optionGroupId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionGroupId))
                .leftJoin(orderSnapShotOptionDetail)
                .on(orderSnapShotOptionDetail.orderId.eq(review.orderId))
                .on(
                        orderSnapShotOptionDetail.snapShotOptionDetail.optionDetailId.eq(
                                orderSnapShotProductOption.snapShotProductOption.optionDetailId))
                .where(reviewIdIn(reviewIds), deleteYn())
                .orderBy(review.id.desc())
                .transform(
                        GroupBy.groupBy(review.id)
                                .list(
                                        new QReviewDto(
                                                review.id,
                                                review.orderId,
                                                users.name,
                                                review.rating,
                                                review.content,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupId,
                                                orderSnapShotProductGroup
                                                        .snapShotProductGroup
                                                        .productGroupName,
                                                orderSnapShotProductGroupImage
                                                        .snapShotProductGroupImage
                                                        .imageDetail
                                                        .imageUrl,
                                                new QBrandDto(brand.id, brand.brandName),
                                                category.id,
                                                category.displayName,
                                                GroupBy.set(
                                                        new QReviewImageDto(
                                                                reviewImage.id,
                                                                reviewImage.reviewImageType,
                                                                reviewImage.imageUrl)),
                                                review.insertDate,
                                                payment.paymentDetails.paymentDate,
                                                GroupBy.set(
                                                        new QOptionDto(
                                                                orderSnapShotOptionGroup
                                                                        .snapShotOptionGroup
                                                                        .optionGroupId,
                                                                orderSnapShotOptionDetail
                                                                        .snapShotOptionDetail
                                                                        .optionDetailId,
                                                                orderSnapShotOptionGroup
                                                                        .snapShotOptionGroup
                                                                        .optionName,
                                                                orderSnapShotOptionDetail
                                                                        .snapShotOptionDetail
                                                                        .optionValue)))));
    }

    @Override
    public JPAQuery<Long> fetchMyReviewCountQuery(long userId) {
        return queryFactory
                .select(review.count())
                .from(review)
                .leftJoin(orderSnapShotProductGroup)
                .on(orderSnapShotProductGroup.orderId.eq(review.orderId))
                .where(userIdEq(userId), deleteYn())
                .distinct();
    }

    private List<Long> fetchMyReviewIds(long userId, Long lastDomainId, Pageable pageable) {
        return queryFactory
                .select(review.id)
                .from(review)
                .where(userIdEq(userId), reviewIdLt(lastDomainId))
                .orderBy(review.id.desc())
                .limit(pageable.getPageSize() + 1)
                .distinct()
                .fetch();
    }

    private BooleanExpression productGroupIdEq(Long productGroupId) {
        if (productGroupId != null)
            return orderSnapShotProductGroup.snapShotProductGroup.productGroupId.eq(productGroupId);
        else return null;
    }

    private BooleanExpression productGroupIdEq(long productGroupId) {
        return productRatingStats.id.eq(productGroupId);
    }

    private BooleanExpression reviewIdIn(List<Long> reviewIds) {
        return review.id.in(reviewIds);
    }

    private BooleanExpression reviewIdEq(long reviewId) {
        return review.id.eq(reviewId);
    }

    private BooleanExpression reviewIdLt(Long lastDomainId) {
        if (lastDomainId != null) return review.id.lt(lastDomainId);
        return null;
    }

    private BooleanExpression userIdEq(long userId) {
        return review.userId.eq(userId);
    }

    private BooleanExpression deleteYn() {
        return review.deleteYn.eq(Yn.N);
    }

    private BooleanExpression orderIdEq(long orderId) {
        return review.orderId.eq(orderId);
    }

    private List<Long> extractReviewIds(List<ReviewQueryDto> reviewQueries) {
        return reviewQueries.stream().map(ReviewQueryDto::getReviewId).collect(Collectors.toList());
    }

    @Override
    public List<OrderSpecifier<?>> createOrderSpecifiersFromPageable(
            EntityPathBase<?> entityPath, OrderType orderType) {
        List<OrderSpecifier<?>> orderSpecifiers = new LinkedList<>();
        if (orderType != null) {
            Order direction = orderType.isAscending() ? Order.ASC : Order.DESC;
            String field = orderType.getField();

            EntityPathBase<?> tempEntityPath = entityPath;
            if (orderType.isRecommend()) {
                tempEntityPath = productScore;
                orderSpecifiers.add(getSortedColumn(direction, tempEntityPath, field));
            } else if (orderType.isHighRating()) {
                tempEntityPath = review;
                orderSpecifiers.add(getSortedColumn(direction, tempEntityPath, "rating"));
            }

            orderSpecifiers.add(getSortedColumn(Order.DESC, review, "id"));
            return orderSpecifiers;
        }
        orderSpecifiers.add(getSortedColumn(Order.DESC, entityPath, "id"));
        return orderSpecifiers;
    }
}
