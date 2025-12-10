package com.ryuqq.setof.adapter.out.persistence.refreshtoken.mapper;

import com.ryuqq.setof.adapter.out.persistence.refreshtoken.entity.RefreshTokenJpaEntity;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
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
 *   <li>ClockHolder를 통한 현재 시간 획득
 *   <li>createdAt, expiresAt 계산
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Component
public class RefreshTokenJpaEntityMapper {

    private final ClockHolder clockHolder;

    public RefreshTokenJpaEntityMapper(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

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
     *   <li>expiresAt: 현재 시간 + expiresInSeconds 계산
     *   <li>createdAt: 현재 시간
     * </ul>
     *
     * @param domain RefreshToken 도메인
     * @return RefreshTokenJpaEntity
     */
    public RefreshTokenJpaEntity toEntity(RefreshToken domain) {
        Instant now = Instant.now(clockHolder.getClock());
        Instant expiresAt = now.plusSeconds(domain.getExpiresInSeconds());

        return RefreshTokenJpaEntity.of(
                domain.getMemberId(), domain.getTokenValue(), expiresAt, now);
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
