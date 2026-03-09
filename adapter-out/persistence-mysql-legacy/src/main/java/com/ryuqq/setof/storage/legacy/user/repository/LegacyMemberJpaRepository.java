package com.ryuqq.setof.storage.legacy.user.repository;

import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyMemberJpaRepository - 레거시 회원 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 CUD 작업만 담당.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface LegacyMemberJpaRepository extends JpaRepository<LegacyUserEntity, Long> {}
