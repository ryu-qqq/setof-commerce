package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository;

import static com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.QDescriptionImageJpaEntity.descriptionImageJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.DescriptionImageJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * DescriptionImageQueryDslRepository - 상세설명 이미지 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Repository
public class DescriptionImageQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public DescriptionImageQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 상세설명 ID로 이미지 목록 조회.
     *
     * @param descriptionId 상세설명 ID
     * @return 이미지 목록
     */
    public List<DescriptionImageJpaEntity> findByProductGroupDescriptionId(Long descriptionId) {
        return queryFactory
                .selectFrom(descriptionImageJpaEntity)
                .where(descriptionImageJpaEntity.productGroupDescriptionId.eq(descriptionId))
                .orderBy(descriptionImageJpaEntity.sortOrder.asc())
                .fetch();
    }

    /**
     * 상세설명 ID 목록으로 이미지 목록 조회.
     *
     * @param descriptionIds 상세설명 ID 목록
     * @return 이미지 목록
     */
    public List<DescriptionImageJpaEntity> findByProductGroupDescriptionIds(
            List<Long> descriptionIds) {
        if (descriptionIds == null || descriptionIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(descriptionImageJpaEntity)
                .where(descriptionImageJpaEntity.productGroupDescriptionId.in(descriptionIds))
                .orderBy(
                        descriptionImageJpaEntity.productGroupDescriptionId.asc(),
                        descriptionImageJpaEntity.sortOrder.asc())
                .fetch();
    }
}
