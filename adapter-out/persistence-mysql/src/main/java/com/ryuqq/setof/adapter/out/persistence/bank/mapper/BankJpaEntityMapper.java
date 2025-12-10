package com.ryuqq.setof.adapter.out.persistence.bank.mapper;

import com.ryuqq.setof.adapter.out.persistence.bank.entity.BankJpaEntity;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import com.ryuqq.setof.domain.bank.vo.BankName;
import org.springframework.stereotype.Component;

/**
 * BankJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Bank 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Bank -> BankJpaEntity (저장용)
 *   <li>BankJpaEntity -> Bank (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BankJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * @param domain Bank 도메인
     * @return BankJpaEntity
     */
    public BankJpaEntity toEntity(Bank domain) {
        return BankJpaEntity.of(
                domain.getIdValue(),
                domain.getBankCodeValue(),
                domain.getBankNameValue(),
                domain.isActive(),
                domain.getDisplayOrder(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity BankJpaEntity
     * @return Bank 도메인
     */
    public Bank toDomain(BankJpaEntity entity) {
        return Bank.reconstitute(
                BankId.of(entity.getId()),
                BankCode.of(entity.getBankCode()),
                BankName.of(entity.getBankName()),
                entity.getDisplayOrder(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
