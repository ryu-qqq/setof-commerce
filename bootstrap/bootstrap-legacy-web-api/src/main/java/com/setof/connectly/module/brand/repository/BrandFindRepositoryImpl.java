package com.setof.connectly.module.brand.repository;

import static com.setof.connectly.module.brand.entity.QBrand.brand;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.brand.dto.BrandDisplayDto;
import com.setof.connectly.module.brand.dto.BrandFilter;
import com.setof.connectly.module.brand.dto.QBrandDisplayDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class BrandFindRepositoryImpl implements BrandFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<BrandDisplayDto> fetchBrand(long brandId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QBrandDisplayDto(
                                        brand.id,
                                        brand.brandName,
                                        brand.displayKoreanName,
                                        brand.brandIconImageUrl))
                        .from(brand)
                        .where(brandIdEq(brandId))
                        .fetchFirst());
    }

    @Override
    public List<BrandDisplayDto> fetchBrands() {
        return queryFactory
                .select(
                        new QBrandDisplayDto(
                                brand.id,
                                brand.brandName,
                                brand.displayKoreanName,
                                brand.brandIconImageUrl))
                .from(brand)
                .fetch();
    }

    @Override
    public List<BrandDisplayDto> fetchBrands(BrandFilter filter) {
        return queryFactory
                .select(
                        new QBrandDisplayDto(
                                brand.id,
                                brand.brandName,
                                brand.displayKoreanName,
                                brand.brandIconImageUrl))
                .from(brand)
                .where(brandNameLike(filter))
                .fetch();
    }

    private BooleanExpression brandIdEq(long brandId) {
        return brand.id.eq(brandId);
    }

    private BooleanExpression brandNameLike(BrandFilter filter) {
        if (StringUtils.hasText(filter.getSearchWord())) {
            return brand.displayKoreanName
                    .like("%" + filter.getSearchWord() + "%")
                    .or(brand.displayEnglishName.like("%" + filter.getSearchWord() + "%"));
        }
        return null;
    }
}
