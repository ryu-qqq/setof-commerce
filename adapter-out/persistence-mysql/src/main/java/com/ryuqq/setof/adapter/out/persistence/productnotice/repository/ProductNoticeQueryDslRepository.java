package com.ryuqq.setof.adapter.out.persistence.productnotice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.QProductNoticeItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.QProductNoticeJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ProductNoticeQueryDslRepository - ProductNotice QueryDSL Repository
 *
 * <p>QueryDSL 기반 동적 쿼리 처리
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ProductNoticeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final QProductNoticeJpaEntity productNotice =
            QProductNoticeJpaEntity.productNoticeJpaEntity;
    private final QProductNoticeItemJpaEntity productNoticeItem =
            QProductNoticeItemJpaEntity.productNoticeItemJpaEntity;

    public ProductNoticeQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 상품고시 ID로 조회
     *
     * @param productNoticeId 상품고시 ID
     * @return 상품고시 Entity
     */
    public Optional<ProductNoticeJpaEntity> findById(Long productNoticeId) {
        ProductNoticeJpaEntity result =
                queryFactory
                        .selectFrom(productNotice)
                        .where(productNotice.id.eq(productNoticeId))
                        .fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * 상품그룹 ID로 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 Entity
     */
    public Optional<ProductNoticeJpaEntity> findByProductGroupId(Long productGroupId) {
        ProductNoticeJpaEntity result =
                queryFactory
                        .selectFrom(productNotice)
                        .where(productNotice.productGroupId.eq(productGroupId))
                        .fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * 상품고시 ID로 항목 목록 조회
     *
     * @param productNoticeId 상품고시 ID
     * @return 항목 Entity 목록
     */
    public List<ProductNoticeItemJpaEntity> findItemsByProductNoticeId(Long productNoticeId) {
        return queryFactory
                .selectFrom(productNoticeItem)
                .where(productNoticeItem.productNoticeId.eq(productNoticeId))
                .orderBy(productNoticeItem.displayOrder.asc())
                .fetch();
    }
}
