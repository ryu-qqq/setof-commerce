package com.ryuqq.setof.adapter.out.persistence.refreshtoken.repository;

import com.ryuqq.setof.adapter.out.persistence.refreshtoken.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RefreshTokenJpaRepository - Refresh Token JPA Repository
 *
 * <p>Spring Data JPA 기반 Refresh Token 저장소 인터페이스
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>save(): 토큰 저장
 * </ul>
 *
 * <p><strong>삭제 쿼리:</strong>
 *
 * <ul>
 *   <li>deleteByMemberId, deleteByToken은 RefreshTokenQueryDslRepository로 이동
 *   <li>@Query, @Modifying 어노테이션 대신 QueryDSL 사용
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {
    // 삭제 쿼리는 RefreshTokenQueryDslRepository에서 처리
}
