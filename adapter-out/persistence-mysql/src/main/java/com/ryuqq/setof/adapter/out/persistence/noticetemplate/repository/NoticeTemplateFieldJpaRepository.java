package com.ryuqq.setof.adapter.out.persistence.noticetemplate.repository;

import com.ryuqq.setof.adapter.out.persistence.noticetemplate.entity.NoticeTemplateFieldJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * NoticeTemplateFieldJpaRepository - NoticeTemplateField JPA Repository
 *
 * <p>Spring Data JPA Repository로, 기본 CRUD 메서드를 제공합니다.
 *
 * <p><strong>표준 JPA 메서드만 사용:</strong>
 *
 * <ul>
 *   <li>save, saveAll, findById, deleteById, deleteAll - 표준 메서드
 *   <li>커스텀 쿼리는 NoticeTemplateFieldQueryDslRepository에서 처리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface NoticeTemplateFieldJpaRepository
        extends JpaRepository<NoticeTemplateFieldJpaEntity, Long> {
    // 표준 JPA 메서드만 사용 (save, saveAll, findById, deleteById, deleteAll)
    // 커스텀 쿼리는 NoticeTemplateFieldQueryDslRepository에서 처리
}
