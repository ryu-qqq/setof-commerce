package com.setof.connectly.module.product.repository.stock;

import static com.setof.connectly.module.crawl.QCrawlProduct.crawlProduct;
import static com.setof.connectly.module.product.entity.group.QProduct.product;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.stock.QProductStock.productStock;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.product.dto.stock.QStockDto;
import com.setof.connectly.module.product.dto.stock.StockDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStockFindRepositoryImpl implements ProductStockFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<StockDto> fetchStock(long productId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QStockDto(
                                        productStock.id,
                                        productStock.stockQuantity,
                                        productGroup.productGroupDetails.productGroupName,
                                        crawlProduct.crawlProductSku.coalesce(0L)))
                        .from(productStock)
                        .innerJoin(product)
                        .on(product.id.eq(productStock.id))
                        .innerJoin(productGroup)
                        .on(productGroup.id.eq(product.productGroup.id))
                        .leftJoin(crawlProduct)
                        .on(crawlProduct.productGroupId.eq(product.productGroup.id))
                        .where(productIdEq(productId))
                        .fetchOne());
    }

    @Override
    public List<StockDto> fetchStocks(List<Long> productIds) {
        return queryFactory
                .select(
                        new QStockDto(
                                productStock.id,
                                productStock.stockQuantity,
                                productGroup.productGroupDetails.productGroupName,
                                crawlProduct.crawlProductSku.coalesce(0L)))
                .from(productStock)
                .innerJoin(product)
                .on(product.id.eq(productStock.id))
                .innerJoin(productGroup)
                .on(productGroup.id.eq(product.productGroup.id))
                .leftJoin(crawlProduct)
                .on(crawlProduct.productGroupId.eq(product.productGroup.id))
                .where(productIdIn(productIds))
                .fetch();
    }

    private BooleanExpression productIdEq(long productId) {
        return productStock.product.id.eq(productId);
    }

    private BooleanExpression productIdIn(List<Long> productIds) {
        return productStock.product.id.in(productIds);
    }
}
