package com.ryuqq.setof.application.bank.manager.query;

import com.ryuqq.setof.application.bank.port.out.query.BankQueryPort;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.bank.exception.BankNotFoundException;
import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Bank Read Manager
 *
 * <p>단일 Query Port 조회를 담당하는 Read Manager
 *
 * <p>읽기 전용 트랜잭션 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BankReadManager {

    private final BankQueryPort bankQueryPort;

    public BankReadManager(BankQueryPort bankQueryPort) {
        this.bankQueryPort = bankQueryPort;
    }

    /**
     * Bank ID로 조회 (필수)
     *
     * @param bankId Bank ID (Long)
     * @return 조회된 Bank
     * @throws BankNotFoundException Bank를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Bank findById(Long bankId) {
        BankId id = BankId.of(bankId);
        return bankQueryPort.findById(id).orElseThrow(() -> new BankNotFoundException(bankId));
    }

    /**
     * Bank ID로 조회 (선택)
     *
     * @param bankId Bank ID (Long)
     * @return 조회된 Bank (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Bank> findByIdOptional(Long bankId) {
        BankId id = BankId.of(bankId);
        return bankQueryPort.findById(id);
    }

    /**
     * 은행 코드로 조회 (필수)
     *
     * @param bankCode 은행 코드
     * @return 조회된 Bank
     * @throws BankNotFoundException Bank를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Bank findByBankCode(String bankCode) {
        BankCode code = BankCode.of(bankCode);
        return bankQueryPort.findByBankCode(code).orElseThrow(() -> new BankNotFoundException(bankCode));
    }

    /**
     * 활성화된 모든 은행 목록 조회
     *
     * @return 활성 Bank 목록 (displayOrder 순)
     */
    @Transactional(readOnly = true)
    public List<Bank> findAllActive() {
        return bankQueryPort.findAllActive();
    }

    /**
     * Bank ID 존재 여부 확인
     *
     * @param bankId Bank ID (Long)
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long bankId) {
        BankId id = BankId.of(bankId);
        return bankQueryPort.existsById(id);
    }

    /**
     * 활성 Bank 존재 여부 확인
     *
     * @param bankId Bank ID (Long)
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsActiveById(Long bankId) {
        return bankQueryPort.existsActiveById(bankId);
    }
}
