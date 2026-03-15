package com.ryuqq.setof.adapter.out.persistence.displaycomponent.repository;

import static com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.QComponentFixedProductJpaEntity.componentFixedProductJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.ComponentFixedProductJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ComponentFixedProductQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ComponentFixedProductQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<ComponentFixedProductJpaEntity> fetchByComponentIds(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(componentFixedProductJpaEntity)
                .where(
                        componentFixedProductJpaEntity.componentId.in(componentIds),
                        componentFixedProductJpaEntity.deletedAt.isNull())
                .orderBy(componentFixedProductJpaEntity.displayOrder.asc())
                .fetch();
    }
}
