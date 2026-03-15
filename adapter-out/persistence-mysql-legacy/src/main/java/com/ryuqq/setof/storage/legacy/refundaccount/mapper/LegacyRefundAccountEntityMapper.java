package com.ryuqq.setof.storage.legacy.refundaccount.mapper;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.id.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;
import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.refundaccount.entity.LegacyRefundAccountEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyRefundAccountEntityMapper - 레거시 환불 계좌 Entity &lt;-&gt; Domain 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyRefundAccountEntityMapper {

    /**
     * 엔티티를 도메인 Aggregate로 변환합니다.
     *
     * @param entity 환불 계좌 엔티티
     * @return RefundAccount 도메인 객체
     */
    public RefundAccount toDomain(LegacyRefundAccountEntity entity) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(entity.getId()),
                MemberId.of(entity.getUserId()),
                RefundBankInfo.of(
                        entity.getBankName(),
                        entity.getAccountNumber(),
                        entity.getAccountHolderName()),
                entity.getDeleteYn() == Yn.Y
                        ? DeletionStatus.deletedAt(Instant.now())
                        : DeletionStatus.active(),
                toInstant(entity.getInsertDate()),
                toInstant(entity.getUpdateDate()));
    }

    /**
     * 도메인 Aggregate를 엔티티로 변환합니다.
     *
     * <p>신규 계좌(isNew = true) 이면 create 팩토리를 사용하고, 기존 계좌이면 reconstitute 팩토리를 사용합니다.
     *
     * @param domain RefundAccount 도메인 객체
     * @return LegacyRefundAccountEntity
     */
    public LegacyRefundAccountEntity toEntity(RefundAccount domain) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime insertDate =
                domain.createdAt() != null
                        ? LocalDateTime.ofInstant(domain.createdAt(), ZoneId.systemDefault())
                        : now;
        LocalDateTime updateDate =
                domain.updatedAt() != null
                        ? LocalDateTime.ofInstant(domain.updatedAt(), ZoneId.systemDefault())
                        : now;

        long userId = domain.memberIdValue();

        if (domain.isNew()) {
            return LegacyRefundAccountEntity.create(
                    userId,
                    domain.bankName(),
                    domain.accountNumber(),
                    domain.accountHolderName(),
                    insertDate,
                    updateDate);
        }

        return LegacyRefundAccountEntity.reconstitute(
                domain.idValue(),
                userId,
                domain.bankName(),
                domain.accountNumber(),
                domain.accountHolderName(),
                domain.isDeleted() ? Yn.Y : Yn.N,
                insertDate,
                updateDate);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
