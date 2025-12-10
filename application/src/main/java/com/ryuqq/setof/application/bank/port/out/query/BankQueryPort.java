package com.ryuqq.setof.application.bank.port.out.query;

import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import java.util.List;
import java.util.Optional;

/**
 * Bank Query Port (Query)
 *
 * <p>Bank Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BankQueryPort {

    /**
     * ID로 Bank 단건 조회
     *
     * @param id Bank ID (Value Object)
     * @return Bank Domain (Optional)
     */
    Optional<Bank> findById(BankId id);

    /**
     * 은행 코드로 Bank 단건 조회
     *
     * @param bankCode 은행 코드 (Value Object)
     * @return Bank Domain (Optional)
     */
    Optional<Bank> findByBankCode(BankCode bankCode);

    /**
     * 활성화된 모든 은행 목록 조회
     *
     * <p>displayOrder 순서로 정렬하여 반환합니다.
     *
     * @return 활성 Bank 목록
     */
    List<Bank> findAllActive();

    /**
     * Bank ID 존재 여부 확인
     *
     * @param id Bank ID
     * @return 존재 여부
     */
    boolean existsById(BankId id);

    /**
     * Bank ID로 활성 은행 존재 여부 확인
     *
     * @param bankId Bank ID (Long)
     * @return 존재 여부
     */
    boolean existsActiveById(Long bankId);
}
