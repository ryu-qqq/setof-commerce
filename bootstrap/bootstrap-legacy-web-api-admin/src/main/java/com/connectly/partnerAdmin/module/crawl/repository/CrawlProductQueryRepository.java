package com.connectly.partnerAdmin.module.crawl.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.connectly.partnerAdmin.module.crawl.entity.CrawlProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.connectly.partnerAdmin.module.crawl.entity.QCrawlProduct.crawlProduct;


@Repository
public class CrawlProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CrawlProductQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public boolean existById(long crawlProductId) {

        Long l = queryFactory
            .select(crawlProduct.crawlProductSku)
            .from(crawlProduct)
            .where(crawlProduct.crawlProductSku.eq(crawlProductId))
            .fetchFirst();

        return l != null;
    }

    public Optional<Long> findProductGroupIdById(long crawlProductId) {
        return Optional.ofNullable(
            queryFactory
            .select(crawlProduct.productGroupId)
            .from(crawlProduct)
            .where(crawlProduct.crawlProductSku.eq(crawlProductId))
            .fetchFirst()
        );
    }

    public void updateCrawlProduct(CrawlProduct cp){
        queryFactory
            .update(crawlProduct)
            .set(crawlProduct.productGroupId, cp.getProductGroupId())
            .set(crawlProduct.updateStatus, cp.getUpdateStatus())
            .where(crawlProduct.crawlProductSku.eq(cp.getCrawlProductSku()))
            .execute();
    }

}
