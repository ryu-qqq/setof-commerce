package com.ryuqq.setof.storage.legacy.qna.repository;

import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyQnaAnswerJpaRepository - 레거시 Q&A 답변 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyQnaAnswerJpaRepository extends JpaRepository<LegacyQnaAnswerEntity, Long> {}
