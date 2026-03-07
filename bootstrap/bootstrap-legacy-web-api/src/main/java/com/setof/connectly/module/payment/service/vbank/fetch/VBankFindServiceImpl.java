package com.setof.connectly.module.payment.service.vbank.fetch;

import com.setof.connectly.module.payment.dto.refund.BankResponse;
import com.setof.connectly.module.payment.repository.vbank.VBankFindRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class VBankFindServiceImpl implements VBankFindService {

    private final VBankFindRepository vBankFindRepository;

    @Override
    public List<BankResponse> fetchVBanks() {
        return vBankFindRepository.fetchVBankAccounts();
    }

    @Override
    public List<BankResponse> fetchRefundBanks() {
        return vBankFindRepository.fetchRefundBanks();
    }
}
