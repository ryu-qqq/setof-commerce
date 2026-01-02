package com.ryuqq.setof.adapter.out.persistence.gnb.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.gnb.entity.GnbJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.gnb.entity.QGnbJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * GnbQueryDslRepository - GNB QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class GnbQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QGnbJpaEntity qGnb = QGnbJpaEntity.gnbJpaEntity;

    public GnbQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 GNB 단건 조회 */
    public Optional<GnbJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qGnb)
                        .where(qGnb.id.eq(id), qGnb.deletedAt.isNull())
                        .fetchOne());
    }

    /** 활성 GNB 전체 조회 (displayOrder 순 정렬) */
    public List<GnbJpaEntity> findAllActive() {
        return queryFactory
                .selectFrom(qGnb)
                .where(qGnb.status.eq("ACTIVE"), qGnb.deletedAt.isNull())
                .orderBy(qGnb.displayOrder.asc())
                .fetch();
    }

    /** ID로 존재 여부 확인 */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qGnb)
                        .where(qGnb.id.eq(id), qGnb.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }
}
