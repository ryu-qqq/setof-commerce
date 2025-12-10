package com.ryuqq.setof.adapter.out.persistence.member.repository;

import com.ryuqq.setof.adapter.out.persistence.member.entity.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MemberJpaRepository - Member JPA Repository
 *
 * <p>Spring Data JPA Repository로서 Member Entity의 기본 CRUD를 담당합니다.
 *
 * <p><strong>UUID v7 기반 PK:</strong>
 *
 * <ul>
 *   <li>String 타입 PK (UUID v7)
 *   <li>AUTO_INCREMENT 대신 Domain에서 생성한 UUID 사용
 *   <li>보안성: 순차적 ID 예측 방지
 * </ul>
 *
 * <p><strong>제공 메서드 (Command 전용):</strong>
 *
 * <ul>
 *   <li>save(entity): 저장/수정 (INSERT/UPDATE)
 *   <li>delete(entity): 삭제 (DELETE)
 *   <li>deleteById(id): ID로 삭제
 * </ul>
 *
 * <p><strong>Query 작업:</strong>
 *
 * <ul>
 *   <li>모든 Query 작업은 MemberQueryDslRepository 사용
 *   <li>findById(), findAll() 등도 QueryDslRepository에서
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, String> {
    // ❌ Query Method 추가 금지
    // ❌ @Query 추가 금지
    // ❌ QuerydslPredicateExecutor 상속 금지
}
