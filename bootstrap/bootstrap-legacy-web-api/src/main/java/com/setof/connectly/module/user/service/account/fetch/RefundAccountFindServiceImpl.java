package com.setof.connectly.module.user.service.account.fetch;

import com.setof.connectly.module.exception.user.RefundAccountNotFoundException;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.entity.RefundAccount;
import com.setof.connectly.module.user.repository.refund.RefundAccountFindRepository;
import com.setof.connectly.module.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RefundAccountFindServiceImpl implements RefundAccountFindService {

    private final RefundAccountFindRepository refundAccountFindRepository;

    @Override
    public RefundAccountInfo fetchRefundAccountInfo() {
        return refundAccountFindRepository
                .fetchRefundAccount(SecurityUtils.currentUserId())
                .orElseThrow(RefundAccountNotFoundException::new);
    }

    @Override
    public RefundAccount fetchRefundAccountEntity(long refundAccountId) {
        return refundAccountFindRepository
                .fetchRefundAccountEntity(refundAccountId, SecurityUtils.currentUserId())
                .orElseThrow(RefundAccountNotFoundException::new);
    }

    @Override
    public RefundAccountInfo fetchRefundAccount(long paymentId) {
        return refundAccountFindRepository
                .fetchRefundAccountByPaymentId(paymentId)
                .orElseThrow(RefundAccountNotFoundException::new);
    }
}
