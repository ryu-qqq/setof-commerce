package com.setof.connectly.module.product.repository.individual;

import static com.setof.connectly.module.product.entity.group.QProduct.product;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.option.QOptionDetail.optionDetail;
import static com.setof.connectly.module.product.entity.option.QOptionGroup.optionGroup;
import static com.setof.connectly.module.product.entity.option.QProductOption.productOption;
import static com.setof.connectly.module.product.entity.stock.QProductStock.productStock;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.product.dto.option.OptionDto;
import com.setof.connectly.module.product.dto.option.QOptionDto;
import com.setof.connectly.module.product.entity.group.Product;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductFindRepositoryImpl implements ProductFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<OptionDto> fetchOptions(long productId) {
        return queryFactory
                .from(product)
                .leftJoin(productOption)
                .on(productOption.product.id.eq(productId))
                .leftJoin(optionGroup)
                .on(optionGroup.id.eq(productOption.optionGroup.id))
                .leftJoin(optionDetail)
                .on(optionDetail.id.eq(productOption.optionDetail.id))
                .where(productIdEq(productId))
                .transform(
                        GroupBy.groupBy(product.id)
                                .list(
                                        new QOptionDto(
                                                optionGroup.id,
                                                optionDetail.id,
                                                optionGroup.optionName,
                                                optionDetail.optionValue)));
    }

    @Override
    public Optional<Product> fetchProductEntity(long productId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(product)
                        .join(product.productGroup, productGroup)
                        .fetchJoin()
                        .join(product.productStock, productStock)
                        .fetchJoin()
                        .where(productIdEq(productId), deleteYn())
                        .distinct()
                        .fetchOne());
    }

    @Override
    public List<Product> fetchProductEntities(List<Long> productIds) {
        return queryFactory
                .selectFrom(product)
                .join(product.productGroup, productGroup)
                .fetchJoin()
                .join(product.productStock, productStock)
                .fetchJoin()
                .where(productIdIn(productIds), deleteYn())
                .distinct()
                .fetch();
    }

    private List<Long> fetchProductGroupIds(List<Long> productIds) {
        return queryFactory
                .select(product.productGroup.id)
                .from(product)
                .where(productIdIn(productIds))
                .fetch();
    }

    private BooleanExpression productIdEq(long productId) {
        return product.id.eq(productId);
    }

    private BooleanExpression productIdIn(List<Long> productIds) {
        if (!productIds.isEmpty()) return product.id.in(productIds);
        return null;
    }

    private BooleanExpression deleteYn() {
        return product.deleteYn.eq(Yn.N);
    }
}
