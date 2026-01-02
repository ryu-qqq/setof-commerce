package com.ryuqq.setof.adapter.out.persistence.claim.adapter;

import com.ryuqq.setof.adapter.out.persistence.claim.condition.AdminClaimSearchCondition;
import com.ryuqq.setof.adapter.out.persistence.claim.entity.ClaimJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.claim.mapper.ClaimJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.claim.repository.ClaimJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.claim.repository.ClaimQueryDslRepository;
import com.ryuqq.setof.application.claim.dto.query.GetAdminClaimsQuery;
import com.ryuqq.setof.application.claim.port.out.command.ClaimPersistencePort;
import com.ryuqq.setof.application.claim.port.out.query.ClaimQueryPort;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.vo.ClaimId;
import com.ryuqq.setof.domain.claim.vo.ClaimNumber;
import com.ryuqq.setof.domain.claim.vo.ClaimStatus;
import com.ryuqq.setof.domain.order.vo.OrderId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ClaimPersistenceAdapter - Claim 영속성 어댑터
 *
 * <p>Application Layer의 Port를 구현하여 영속성 계층과 연결합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ClaimPersistenceAdapter implements ClaimPersistencePort, ClaimQueryPort {

    private final ClaimJpaRepository claimJpaRepository;
    private final ClaimQueryDslRepository claimQueryDslRepository;
    private final ClaimJpaEntityMapper claimJpaEntityMapper;

    public ClaimPersistenceAdapter(
            ClaimJpaRepository claimJpaRepository,
            ClaimQueryDslRepository claimQueryDslRepository,
            ClaimJpaEntityMapper claimJpaEntityMapper) {
        this.claimJpaRepository = claimJpaRepository;
        this.claimQueryDslRepository = claimQueryDslRepository;
        this.claimJpaEntityMapper = claimJpaEntityMapper;
    }

    /**
     * Claim 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 있으면 UPDATE, 없으면 INSERT
     */
    @Override
    public ClaimId persist(Claim claim) {
        if (claim.claimId() != null) {
            // UPDATE: 기존 Entity 조회 후 수정 (QueryDslRepository 사용)
            ClaimJpaEntity entity =
                    claimQueryDslRepository
                            .findByClaimId(claim.claimId().value())
                            .orElseThrow(
                                    () ->
                                            new IllegalStateException(
                                                    "Claim not found: " + claim.claimId().value()));
            claimJpaEntityMapper.updateEntity(entity, claim);
            return ClaimId.of(entity.getClaimId());
        } else {
            // INSERT: 새로운 Entity 저장
            ClaimJpaEntity entity = claimJpaEntityMapper.toEntity(claim);
            ClaimJpaEntity saved = claimJpaRepository.save(entity);
            return ClaimId.of(saved.getClaimId());
        }
    }

    @Override
    public Optional<Claim> findByClaimId(ClaimId claimId) {
        return claimQueryDslRepository
                .findByClaimId(claimId.value())
                .map(claimJpaEntityMapper::toDomain);
    }

    @Override
    public Optional<Claim> findByClaimNumber(ClaimNumber claimNumber) {
        return claimQueryDslRepository
                .findByClaimNumber(claimNumber.value())
                .map(claimJpaEntityMapper::toDomain);
    }

    @Override
    public List<Claim> findByOrderId(OrderId orderId) {
        return claimQueryDslRepository
                .findByOrderIdOrderByCreatedAtDesc(orderId.value().toString())
                .stream()
                .map(claimJpaEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Claim> findByStatus(ClaimStatus status) {
        return claimQueryDslRepository.findByStatusOrderByCreatedAtDesc(status.name()).stream()
                .map(claimJpaEntityMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsActiveClaimByOrderId(OrderId orderId) {
        List<String> activeStatuses =
                List.of(
                        ClaimStatus.REQUESTED.name(),
                        ClaimStatus.APPROVED.name(),
                        ClaimStatus.IN_PROGRESS.name());
        return claimQueryDslRepository.existsByOrderIdAndStatusIn(
                orderId.value().toString(), activeStatuses);
    }

    @Override
    public List<Claim> findByAdminQuery(GetAdminClaimsQuery query) {
        AdminClaimSearchCondition condition =
                AdminClaimSearchCondition.of(
                        query.sellerId(),
                        query.memberId(),
                        query.claimStatuses(),
                        query.claimTypes(),
                        query.searchKeyword(),
                        query.startDate(),
                        query.endDate(),
                        query.lastClaimId(),
                        query.pageSize());

        return claimQueryDslRepository.findByAdminCondition(condition).stream()
                .map(claimJpaEntityMapper::toDomain)
                .toList();
    }
}
