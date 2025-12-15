package com.setof.connectly.module.user.service.account;

import com.setof.connectly.module.user.dto.account.CreateRefundAccount;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.entity.RefundAccount;
import com.setof.connectly.module.user.mapper.account.AccountMapper;
import com.setof.connectly.module.user.repository.refund.RefundAccountRepository;
import com.setof.connectly.module.user.service.account.fetch.RefundAccountFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class RefundAccountQueryServiceImpl implements RefundAccountQueryService {
    private final AccountMapper accountMapper;
    private final RefundAccountRepository refundAccountRepository;
    private final RefundAccountFindService refundAccountFindService;

    @Override
    public RefundAccountInfo saveRefundAccount(CreateRefundAccount createRefundAccount) {
        RefundAccount refundAccount = accountMapper.toEntity(createRefundAccount);
        RefundAccount saveRefundAccount = refundAccountRepository.save(refundAccount);
        return accountMapper.toResponse(saveRefundAccount);
    }

    @Override
    public RefundAccountInfo updateRefundAccount(
            CreateRefundAccount createRefundAccount, long refundAccountId) {
        RefundAccount refundAccount =
                refundAccountFindService.fetchRefundAccountEntity(refundAccountId);
        refundAccount.update(createRefundAccount);
        return accountMapper.toResponse(refundAccount);
    }

    @Override
    public RefundAccountInfo deleteRefundAccount(long refundAccountId) {
        RefundAccount refundAccount =
                refundAccountFindService.fetchRefundAccountEntity(refundAccountId);
        refundAccountRepository.delete(refundAccount);
        return accountMapper.toResponse(refundAccount);
    }
}
