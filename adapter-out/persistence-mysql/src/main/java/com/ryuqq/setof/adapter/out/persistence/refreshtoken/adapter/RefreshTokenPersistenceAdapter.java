package com.ryuqq.setof.adapter.out.persistence.refreshtoken.adapter;

import com.ryuqq.setof.adapter.out.persistence.refreshtoken.entity.RefreshTokenJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.mapper.RefreshTokenJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.repository.RefreshTokenJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.repository.RefreshTokenQueryDslRepository;
import com.ryuqq.setof.application.auth.port.out.command.RefreshTokenPersistencePort;
import com.ryuqq.setof.domain.auth.aggregate.RefreshToken;
import org.springframework.stereotype.Component;

/**
 * RefreshTokenPersistenceAdapter - Refresh Token 영속성 Adapter
 *
 * <p>RefreshTokenPersistencePort를 구현하여 RDS에 Refresh Token을 저장합니다.
 *
 * <p><strong>역할:</strong>
 *
 * <ul>
 *   <li>Redis 캐시의 백업 저장소
 *   <li>Cache-Aside 패턴에서 원본 데이터 제공
 *   <li>회원 탈퇴/로그아웃 시 일괄 삭제
 * </ul>
 *
 * <p><strong>의존성 구조:</strong>
 *
 * <ul>
 *   <li>RefreshTokenJpaRepository: save() 전용
 *   <li>RefreshTokenQueryDslRepository: delete 쿼리 전용
 *   <li>RefreshTokenJpaEntityMapper: Domain ↔ Entity 변환
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 포함 금지
 *   <li>@Transactional 금지 (Facade/Manager 책임)
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Component
public class RefreshTokenPersistenceAdapter implements RefreshTokenPersistencePort {

    private final RefreshTokenJpaRepository jpaRepository;
    private final RefreshTokenQueryDslRepository queryDslRepository;
    private final RefreshTokenJpaEntityMapper mapper;

    public RefreshTokenPersistenceAdapter(
            RefreshTokenJpaRepository jpaRepository,
            RefreshTokenQueryDslRepository queryDslRepository,
            RefreshTokenJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Refresh Token을 RDS에 저장합니다. Mapper를 통해 Domain → Entity 변환 후 저장합니다.
     */
    @Override
    public void persist(RefreshToken refreshToken) {
        RefreshTokenJpaEntity entity = mapper.toEntity(refreshToken);
        jpaRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     *
     * <p>회원 ID 기준으로 모든 Refresh Token을 삭제합니다. QueryDslRepository를 통해 벌크 삭제 수행합니다.
     */
    @Override
    public void deleteByMemberId(String memberId) {
        queryDslRepository.deleteByMemberId(memberId);
    }

    /**
     * {@inheritDoc}
     *
     * <p>특정 Refresh Token을 삭제합니다. QueryDslRepository를 통해 삭제 수행합니다.
     */
    @Override
    public void deleteByToken(String tokenValue) {
        queryDslRepository.deleteByToken(tokenValue);
    }
}
