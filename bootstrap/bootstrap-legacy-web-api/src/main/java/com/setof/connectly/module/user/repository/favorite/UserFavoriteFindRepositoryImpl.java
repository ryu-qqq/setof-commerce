package com.setof.connectly.module.user.repository.favorite;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.setof.connectly.module.review.entity.QProductRatingStats.productRatingStats;
import static com.setof.connectly.module.search.entity.QProductScore.productScore;
import static com.setof.connectly.module.user.entity.QUserFavorite.userFavorite;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.product.dto.QProductGroupThumbnail;
import com.setof.connectly.module.product.dto.brand.QBrandDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import com.setof.connectly.module.user.dto.favorite.*;
import com.setof.connectly.module.user.entity.UserFavorite;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserFavoriteFindRepositoryImpl implements UserFavoriteFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserFavoriteThumbnail> fetchMyFavorites(
            long userId, MyFavoriteFilter filter, Pageable pageable) {
        return queryFactory
                .from(userFavorite)
                .innerJoin(productGroup)
                .on(productGroup.id.eq(userFavorite.productGroupId))
                .innerJoin(productGroupImage)
                .on(productGroupImage.productGroup.id.eq(productGroup.id))
                .on(
                        productGroupImage.imageDetail.productGroupImageType.eq(
                                ProductGroupImageType.MAIN))
                .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(brand)
                .on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .where(userIdEq(userId), onStock(), userFavoriteIdLt(filter.getLastDomainId()))
                .orderBy(userFavorite.id.desc())
                .limit(pageable.getPageSize() + 1)
                .transform(
                        GroupBy.groupBy(productGroup.id)
                                .list(
                                        new QUserFavoriteThumbnail(
                                                productGroup.id,
                                                productGroup.productGroupDetails.sellerId,
                                                productGroup.productGroupDetails.productGroupName,
                                                new QBrandDto(brand.id, brand.brandName),
                                                productGroupImage.imageDetail.imageUrl,
                                                productGroup.productGroupDetails.price,
                                                productGroup.insertDate,
                                                productGroup.productGroupDetails.productStatus,
                                                userFavorite.id)));
    }

    @Override
    public List<UserFavorite> fetchUserFavorites(long userId, List<Long> productGroupIds) {
        return queryFactory
                .selectFrom(userFavorite)
                .where(userIdEq(userId), productGroupIdIn(productGroupIds))
                .fetch();
    }

    @Override
    public Optional<UserFavoriteResponse> fetchUserFavorite(long userId, long productGroupId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QUserFavoriteResponse(
                                        userFavorite.id.coalesce(0L),
                                        new QProductGroupThumbnail(
                                                productGroup.id,
                                                productGroup.productGroupDetails.sellerId,
                                                productGroup.productGroupDetails.productGroupName,
                                                new QBrandDto(brand.id, brand.brandName),
                                                productGroupImage.imageDetail.imageUrl,
                                                productGroup.productGroupDetails.price,
                                                productGroup.insertDate,
                                                productRatingStats.averageRating.coalesce(0.0),
                                                productRatingStats.reviewCount.coalesce(0L),
                                                productScore.score.coalesce(0.0),
                                                productGroup.productGroupDetails.productStatus)))
                        .from(productGroup)
                        .innerJoin(productGroupImage)
                        .on(productGroupImage.productGroup.id.eq(productGroup.id))
                        .on(
                                productGroupImage.imageDetail.productGroupImageType.eq(
                                        ProductGroupImageType.MAIN))
                        .on(productGroupImage.deleteYn.eq(Yn.N))
                        .innerJoin(brand)
                        .on(brand.id.eq(productGroup.productGroupDetails.brandId))
                        .leftJoin(productRatingStats)
                        .on(productRatingStats.id.eq(productGroup.id))
                        .leftJoin(productScore)
                        .on(productScore.id.eq(productGroup.id))
                        .leftJoin(userFavorite)
                        .on(userFavorite.productGroupId.eq(productGroup.id))
                        .on(userFavorite.userId.eq(userId))
                        .where(productGroupIdEq(productGroupId))
                        .fetchOne());
    }

    @Override
    public List<Long> fetchUserFavoriteIds(long userId) {
        return queryFactory
                .select(userFavorite.id)
                .from(userFavorite)
                .where(userIdEq(userId))
                .fetch();
    }

    @Override
    public JPAQuery<Long> fetchUserFavoriteCountQuery(long userId) {
        return queryFactory
                .select(userFavorite.count())
                .from(userFavorite)
                .innerJoin(productGroup)
                .on(productGroup.id.eq(userFavorite.productGroupId))
                .where(userIdEq(userId), onStock())
                .distinct();
    }

    @Override
    public boolean hasUserFavoriteProduct(long userId, long productGroupId) {
        Long aLong =
                queryFactory
                        .select(userFavorite.id)
                        .from(userFavorite)
                        .where(userIdEq(userId), favoriteProductGroupIdEq(productGroupId))
                        .fetchFirst();

        return aLong != null;
    }

    private BooleanExpression userIdEq(long userId) {
        return userFavorite.userId.eq(userId);
    }

    private BooleanExpression productGroupIdEq(long productGroupId) {
        return productGroup.id.eq(productGroupId);
    }

    private BooleanExpression favoriteProductGroupIdEq(long productGroupId) {
        return userFavorite.productGroupId.eq(productGroupId);
    }

    private BooleanExpression productGroupIdIn(List<Long> productGroupIds) {
        return userFavorite.productGroupId.in(productGroupIds);
    }

    private BooleanExpression onStock() {
        return productGroup.productGroupDetails.productStatus.soldOutYn.eq(Yn.N);
    }

    private BooleanExpression userFavoriteIdLt(Long lastDomainId) {
        if (lastDomainId != null) return userFavorite.id.lt(lastDomainId);
        else return null;
    }
}
