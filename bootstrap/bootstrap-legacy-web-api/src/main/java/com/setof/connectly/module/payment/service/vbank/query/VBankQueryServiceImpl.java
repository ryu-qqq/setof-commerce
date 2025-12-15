package com.setof.connectly.module.payment.service.vbank.query;

import com.setof.connectly.module.payment.entity.VBankAccount;
import com.setof.connectly.module.payment.repository.vbank.VBankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class VBankQueryServiceImpl implements VBankQueryService {

    private final VBankAccountRepository vBankAccountRepository;

    @Override
    public void saveVBankAccount(VBankAccount vBankAccount) {
        vBankAccountRepository.save(vBankAccount);
    }
}
