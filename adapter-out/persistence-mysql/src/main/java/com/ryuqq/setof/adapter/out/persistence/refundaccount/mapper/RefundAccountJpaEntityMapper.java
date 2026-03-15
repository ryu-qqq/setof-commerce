package com.ryuqq.setof.adapter.out.persistence.refundaccount.mapper;

import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.id.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;
import org.springframework.stereotype.Component;

/**
 * RefundAccountJpaEntityMapper - 환불 계좌 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class RefundAccountJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain RefundAccount 도메인 객체
     * @return RefundAccountJpaEntity
     */
    public RefundAccountJpaEntity toEntity(RefundAccount domain) {
        return RefundAccountJpaEntity.create(
                domain.idValue(),
                domain.memberIdValue(),
                domain.bankName(),
                domain.accountNumber(),
                domain.accountHolderName(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity RefundAccountJpaEntity
     * @return RefundAccount 도메인 객체
     */
    public RefundAccount toDomain(RefundAccountJpaEntity entity) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(entity.getId()),
                MemberId.of(entity.getMemberId()),
                RefundBankInfo.of(
                        entity.getBankName(),
                        entity.getAccountNumber(),
                        entity.getAccountHolderName()),
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
