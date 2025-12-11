package com.ryuqq.setof.adapter.out.persistence.refreshtoken.mapper;

import com.ryuqq.setof.adapter.out.persistence.refreshtoken.entity.RefreshTokenJpaEntity;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import org.springframework.stereotype.Component;

/**
 * RefreshTokenJpaEntityMapper - Entity ↔ Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 RefreshToken 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>RefreshToken → RefreshTokenJpaEntity (저장용)
 *   <li>RefreshTokenJpaEntity → RefreshToken (조회용)
 * </ul>
 *
 * <p><strong>시간 처리:</strong>
 *
 * <ul>
 *   <li>Domain에서 이미 계산된 시간값을 그대로 사용
 *   <li>Mapper는 단순 변환만 담당 (비즈니스 로직 없음)
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Component
public class RefreshTokenJpaEntityMapper {

    /**
     * Domain → Entity 변환
     *
     * <p><strong>사용 시나리오:</strong>
     *
     * <ul>
     *   <li>신규 RefreshToken 저장
     * </ul>
     *
     * <p><strong>변환 규칙:</strong>
     *
     * <ul>
     *   <li>memberId: String UUID 그대로 전달
     *   <li>tokenValue: 토큰 값 그대로 전달
     *   <li>expiresAt: Domain에서 계산된 값 그대로 전달
     *   <li>createdAt: Domain에서 계산된 값 그대로 전달
     * </ul>
     *
     * @param domain RefreshToken 도메인
     * @return RefreshTokenJpaEntity
     */
    public RefreshTokenJpaEntity toEntity(RefreshToken domain) {
        return RefreshTokenJpaEntity.of(
                domain.getMemberId(),
                domain.getTokenValue(),
                domain.getExpiresAt(),
                domain.getCreatedAt());
    }

    /**
     * Entity → Domain 변환
     *
     * <p><strong>사용 시나리오:</strong>
     *
     * <ul>
     *   <li>데이터베이스에서 조회한 Entity를 Domain으로 변환
     * </ul>
     *
     * <p><strong>변환 규칙:</strong>
     *
     * <ul>
     *   <li>memberId: String UUID 그대로 전달
     *   <li>token: 토큰 값 그대로 전달
     *   <li>expiresAt, createdAt: Instant 그대로 전달
     * </ul>
     *
     * @param entity RefreshTokenJpaEntity
     * @return RefreshToken 도메인
     */
    public RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        return RefreshToken.reconstitute(
                entity.getMemberId(),
                entity.getToken(),
                entity.getExpiresAt(),
                entity.getCreatedAt());
    }
}
