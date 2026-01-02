package com.setof.connectly.module.search.repository;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.setof.connectly.module.review.entity.QProductRatingStats.productRatingStats;
import static com.setof.connectly.module.search.entity.QProductScore.productScore;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.QProductGroupThumbnail;
import com.setof.connectly.module.product.dto.brand.QBrandDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import com.setof.connectly.module.search.dto.SearchFilter;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TempSearchRepositoryImpl implements SearchRepository {

    protected final JPAQueryFactory queryFactory;

    public TempSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<ProductGroupThumbnail> fetchResults(SearchFilter searchFilter, int size) {
        return queryFactory
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
                .where(fullTextSearch(searchFilter.getSearchWord()))
                .orderBy(productScore.score.coalesce(0.0).desc())
                .limit(size)
                .transform(
                        GroupBy.groupBy(productGroup.id)
                                .list(
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
                                                productGroup.productGroupDetails.productStatus)));
    }

    @Override
    public long fetchSearchCountQuery(SearchFilter filter) {
        Long l =
                queryFactory
                        .select(productGroup.id.count())
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
                        .where(fullTextSearch(filter.getSearchWord()))
                        .orderBy(productScore.score.coalesce(0.0).desc())
                        .fetchOne();

        return l != null ? l : 0;
    }

    /**
     * MySQL ngram FULLTEXT 인덱스를 사용한 검색 인덱스: ALTER TABLE product_group ADD FULLTEXT INDEX
     * ft_product_group_name (product_group_name) WITH PARSER ngram;
     */
    private BooleanExpression fullTextSearch(String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        // MySQL MATCH ... AGAINST with ngram parser (IN BOOLEAN MODE for better control)
        // Hibernate 6 커스텀 함수 사용 (MatchAgainstFunctionContributor에서 등록)
        return Expressions.numberTemplate(
                        Double.class,
                        "match_against({0}, {1})",
                        productGroup.productGroupDetails.productGroupName,
                        "+" + searchWord + "*")
                .gt(0);
    }
}
