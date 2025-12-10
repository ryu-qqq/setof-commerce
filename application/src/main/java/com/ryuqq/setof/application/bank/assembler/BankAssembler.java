package com.ryuqq.setof.application.bank.assembler;

import com.ryuqq.setof.application.bank.dto.response.BankResponse;
import com.ryuqq.setof.domain.bank.aggregate.Bank;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Bank Assembler
 *
 * <p>Bank 도메인 객체를 Response DTO로 변환하는 Assembler
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BankAssembler {

    /**
     * Bank 도메인을 BankResponse로 변환
     *
     * @param bank Bank 도메인 객체
     * @return BankResponse
     */
    public BankResponse toBankResponse(Bank bank) {
        return BankResponse.of(
                bank.getIdValue(),
                bank.getBankCodeValue(),
                bank.getBankNameValue(),
                bank.getDisplayOrder());
    }

    /**
     * Bank 도메인 목록을 BankResponse 목록으로 변환
     *
     * @param banks Bank 도메인 목록
     * @return BankResponse 목록
     */
    public List<BankResponse> toBankResponses(List<Bank> banks) {
        return banks.stream().map(this::toBankResponse).toList();
    }
}
