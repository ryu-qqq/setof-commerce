package com.ryuqq.setof.application.bank.service.query;

import com.ryuqq.setof.application.bank.assembler.BankAssembler;
import com.ryuqq.setof.application.bank.dto.response.BankResponse;
import com.ryuqq.setof.application.bank.manager.query.BankReadManager;
import com.ryuqq.setof.application.bank.port.in.query.GetBanksUseCase;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 은행 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>BankReadManager로 활성 은행 목록 조회
 *   <li>BankAssembler로 BankResponse 목록 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class BankQueryService implements GetBanksUseCase {

    private final BankReadManager bankReadManager;
    private final BankAssembler bankAssembler;

    public BankQueryService(BankReadManager bankReadManager, BankAssembler bankAssembler) {
        this.bankReadManager = bankReadManager;
        this.bankAssembler = bankAssembler;
    }

    @Override
    public List<BankResponse> execute() {
        List<Bank> activeBanks = bankReadManager.findAllActive();
        return bankAssembler.toBankResponses(activeBanks);
    }
}
