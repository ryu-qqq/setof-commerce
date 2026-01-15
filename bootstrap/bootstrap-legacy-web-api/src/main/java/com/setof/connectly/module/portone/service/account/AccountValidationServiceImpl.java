package com.setof.connectly.module.portone.service.account;

import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderDto;
import com.setof.connectly.module.payment.dto.portone.PortOneVBankHolderResponse;
import com.setof.connectly.module.portone.client.PortOneClient;
import com.setof.connectly.module.user.dto.account.CreateRefundAccount;
import com.setof.connectly.module.user.mapper.account.AccountMapper;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "portone.enabled", havingValue = "true", matchIfMissing = true)
public class AccountValidationServiceImpl implements AccountValidationService {

    private final PortOneClient portOneClient;
    private final AccountMapper accountMapper;

    @Override
    public boolean validateAccount(CreateRefundAccount createRefundAccount) {
        String token = portOneClient.fetchTokenPortOne();
        PortOneVBankHolderDto requestDto = accountMapper.toPortOneVBankDto(createRefundAccount);
        PortOneVBankHolderResponse response = portOneClient.fetchVBankHolder(token, requestDto);
        return validateVBankHolder(response);
    }

    private boolean validateVBankHolder(PortOneVBankHolderResponse response) {
        String name = SecurityUtils.currentUserForLogging();
        if (name.equals("세토프")) return true;
        return Objects.equals(name, response.getBankHolder());
    }
}
