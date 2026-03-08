package com.ryuqq.setof.storage.legacy.qna.repository;

import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyQnaJpaRepository - 레거시 Q&A JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 CUD 작업만 담당.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyQnaJpaRepository extends JpaRepository<LegacyQnaEntity, Long> {}
