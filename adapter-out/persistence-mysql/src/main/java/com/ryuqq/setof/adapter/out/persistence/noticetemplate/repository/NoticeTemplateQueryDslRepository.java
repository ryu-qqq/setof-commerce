package com.ryuqq.setof.adapter.out.persistence.noticetemplate.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.QNoticeTemplateJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * NoticeTemplateQueryDslRepository - NoticeTemplate QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class NoticeTemplateQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QNoticeTemplateJpaEntity qTemplate =
            QNoticeTemplateJpaEntity.noticeTemplateJpaEntity;

    public NoticeTemplateQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 NoticeTemplate 단건 조회 */
    public Optional<NoticeTemplateJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qTemplate).where(qTemplate.id.eq(id)).fetchOne());
    }

    /** 카테고리 ID로 NoticeTemplate 단건 조회 */
    public Optional<NoticeTemplateJpaEntity> findByCategoryId(Long categoryId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qTemplate)
                        .where(qTemplate.categoryId.eq(categoryId))
                        .fetchOne());
    }

    /** 전체 NoticeTemplate 조회 */
    public List<NoticeTemplateJpaEntity> findAll() {
        return queryFactory.selectFrom(qTemplate).orderBy(qTemplate.id.asc()).fetch();
    }

    /** 카테고리 ID로 존재 여부 확인 */
    public boolean existsByCategoryId(Long categoryId) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qTemplate)
                        .where(qTemplate.categoryId.eq(categoryId))
                        .fetchFirst();
        return count != null;
    }
}
