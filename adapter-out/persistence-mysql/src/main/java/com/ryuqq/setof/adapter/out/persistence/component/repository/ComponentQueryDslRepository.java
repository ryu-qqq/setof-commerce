package com.ryuqq.setof.adapter.out.persistence.component.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.component.entity.ComponentJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.component.entity.QComponentJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ComponentQueryDslRepository - Component QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ComponentQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QComponentJpaEntity qComponent = QComponentJpaEntity.componentJpaEntity;

    public ComponentQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 Component 단건 조회 */
    public Optional<ComponentJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qComponent)
                        .where(qComponent.id.eq(id), qComponent.deletedAt.isNull())
                        .fetchOne());
    }

    /** Content ID로 Component 목록 조회 (displayOrder 순 정렬) */
    public List<ComponentJpaEntity> findByContentId(Long contentId) {
        return queryFactory
                .selectFrom(qComponent)
                .where(qComponent.contentId.eq(contentId), qComponent.deletedAt.isNull())
                .orderBy(qComponent.displayOrder.asc())
                .fetch();
    }

    /** ID로 존재 여부 확인 */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qComponent)
                        .where(qComponent.id.eq(id), qComponent.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }
}
