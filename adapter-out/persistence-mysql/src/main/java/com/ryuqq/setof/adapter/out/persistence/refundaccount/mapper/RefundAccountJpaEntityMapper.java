package com.ryuqq.setof.adapter.out.persistence.refundaccount.mapper;

import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.AccountHolderName;
import com.ryuqq.setof.domain.refundaccount.vo.AccountNumber;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.VerificationInfo;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * RefundAccountJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 RefundAccount 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>RefundAccount -> RefundAccountJpaEntity (저장용)
 *   <li>RefundAccountJpaEntity -> RefundAccount (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RefundAccountJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * <p>계좌번호는 원본 값을 저장합니다. 마스킹은 조회 시 Application Layer에서 처리합니다.
     *
     * @param domain RefundAccount 도메인
     * @return RefundAccountJpaEntity
     */
    public RefundAccountJpaEntity toEntity(RefundAccount domain) {
        return RefundAccountJpaEntity.of(
                domain.getIdValue(),
                domain.getMemberId().toString(),
                domain.getBankId(),
                domain.getAccountNumberValue(),
                domain.getAccountHolderNameValue(),
                domain.isVerified(),
                extractVerifiedAt(domain),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity RefundAccountJpaEntity
     * @return RefundAccount 도메인
     */
    public RefundAccount toDomain(RefundAccountJpaEntity entity) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(entity.getId()),
                UUID.fromString(entity.getMemberId()),
                entity.getBankId(),
                AccountNumber.of(entity.getAccountNumber()),
                AccountHolderName.of(entity.getAccountHolderName()),
                buildVerificationInfo(entity),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }

    // ========== Private Helper Methods ==========

    private java.time.Instant extractVerifiedAt(RefundAccount domain) {
        if (domain.getVerificationInfo() == null) {
            return null;
        }
        return domain.getVerificationInfo().verifiedAt();
    }

    private VerificationInfo buildVerificationInfo(RefundAccountJpaEntity entity) {
        return VerificationInfo.reconstitute(entity.isVerified(), entity.getVerifiedAt());
    }
}
