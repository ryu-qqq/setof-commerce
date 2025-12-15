package com.ryuqq.setof.adapter.out.persistence.bank.adapter;

import com.ryuqq.setof.adapter.out.persistence.bank.mapper.BankJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.bank.repository.BankQueryDslRepository;
import com.ryuqq.setof.application.bank.port.out.query.BankQueryPort;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import com.ryuqq.setof.domain.bank.vo.BankName;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * BankQueryAdapter - Bank Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Bank 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>은행 코드로 조회 (findByBankCode)
 *   <li>활성 은행 목록 조회 (findAllActive)
 *   <li>존재 여부 확인 (existsById, existsActiveById)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (CommandAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BankQueryAdapter implements BankQueryPort {

    private final BankQueryDslRepository queryDslRepository;
    private final BankJpaEntityMapper bankJpaEntityMapper;

    public BankQueryAdapter(
            BankQueryDslRepository queryDslRepository, BankJpaEntityMapper bankJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.bankJpaEntityMapper = bankJpaEntityMapper;
    }

    /**
     * ID로 Bank 단건 조회
     *
     * @param id Bank ID (Value Object)
     * @return Bank Domain (Optional)
     */
    @Override
    public Optional<Bank> findById(BankId id) {
        return queryDslRepository.findById(id.value()).map(bankJpaEntityMapper::toDomain);
    }

    /**
     * 은행 코드로 Bank 조회
     *
     * @param bankCode 은행 코드 (Value Object)
     * @return Bank Domain (Optional)
     */
    @Override
    public Optional<Bank> findByBankCode(BankCode bankCode) {
        return queryDslRepository
                .findByBankCode(bankCode.value())
                .map(bankJpaEntityMapper::toDomain);
    }

    /**
     * 은행 이름으로 Bank 조회
     *
     * <p>V1 레거시 API 지원을 위한 메서드입니다.
     *
     * @param bankName 은행 이름 (Value Object)
     * @return Bank Domain (Optional)
     */
    @Override
    public Optional<Bank> findByBankName(BankName bankName) {
        return queryDslRepository
                .findByBankName(bankName.value())
                .map(bankJpaEntityMapper::toDomain);
    }

    /**
     * 활성화된 모든 Bank 목록 조회
     *
     * @return Bank 목록 (displayOrder 순)
     */
    @Override
    public List<Bank> findAllActive() {
        return queryDslRepository.findAllActive().stream()
                .map(bankJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * ID로 Bank 존재 여부 확인
     *
     * @param id Bank ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(BankId id) {
        return queryDslRepository.existsById(id.value());
    }

    /**
     * 활성 Bank 존재 여부 확인
     *
     * @param bankId Bank ID (Long)
     * @return 활성 존재 여부
     */
    @Override
    public boolean existsActiveById(Long bankId) {
        return queryDslRepository.existsActiveById(bankId);
    }
}
