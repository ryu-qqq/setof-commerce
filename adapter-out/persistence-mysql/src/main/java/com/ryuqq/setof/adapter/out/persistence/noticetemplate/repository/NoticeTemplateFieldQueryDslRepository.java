package com.ryuqq.setof.adapter.out.persistence.noticetemplate.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateFieldJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.QNoticeTemplateFieldJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * NoticeTemplateFieldQueryDslRepository - NoticeTemplateField QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 단건 조회
 *   <li>findByTemplateId(Long templateId): 템플릿 ID로 필드 목록 조회
 *   <li>findRequiredByTemplateId(Long templateId): 필수 필드만 조회
 *   <li>findOptionalByTemplateId(Long templateId): 선택 필드만 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지 (fetch join, left join, inner join)
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class NoticeTemplateFieldQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QNoticeTemplateFieldJpaEntity qField =
            QNoticeTemplateFieldJpaEntity.noticeTemplateFieldJpaEntity;

    public NoticeTemplateFieldQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 NoticeTemplateField 단건 조회
     *
     * @param id 필드 ID
     * @return NoticeTemplateFieldJpaEntity (Optional)
     */
    public Optional<NoticeTemplateFieldJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qField).where(qField.id.eq(id)).fetchOne());
    }

    /**
     * 템플릿 ID로 필드 목록 조회 (표시 순서 정렬)
     *
     * @param templateId 템플릿 ID
     * @return NoticeTemplateFieldJpaEntity 목록
     */
    public List<NoticeTemplateFieldJpaEntity> findByTemplateId(Long templateId) {
        return queryFactory
                .selectFrom(qField)
                .where(qField.templateId.eq(templateId))
                .orderBy(qField.displayOrder.asc())
                .fetch();
    }

    /**
     * 템플릿 ID로 필수 필드만 조회 (표시 순서 정렬)
     *
     * @param templateId 템플릿 ID
     * @return 필수 필드 NoticeTemplateFieldJpaEntity 목록
     */
    public List<NoticeTemplateFieldJpaEntity> findRequiredByTemplateId(Long templateId) {
        return queryFactory
                .selectFrom(qField)
                .where(qField.templateId.eq(templateId), qField.required.isTrue())
                .orderBy(qField.displayOrder.asc())
                .fetch();
    }

    /**
     * 템플릿 ID로 선택 필드만 조회 (표시 순서 정렬)
     *
     * @param templateId 템플릿 ID
     * @return 선택 필드 NoticeTemplateFieldJpaEntity 목록
     */
    public List<NoticeTemplateFieldJpaEntity> findOptionalByTemplateId(Long templateId) {
        return queryFactory
                .selectFrom(qField)
                .where(qField.templateId.eq(templateId), qField.required.isFalse())
                .orderBy(qField.displayOrder.asc())
                .fetch();
    }
}
