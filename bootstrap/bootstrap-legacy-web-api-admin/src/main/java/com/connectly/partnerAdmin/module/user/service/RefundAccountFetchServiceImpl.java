package com.connectly.partnerAdmin.module.user.service;


import com.connectly.partnerAdmin.module.user.exception.RefundAccountNotFoundException;
import com.connectly.partnerAdmin.module.user.dto.RefundAccountInfo;
import com.connectly.partnerAdmin.module.user.repository.RefundAccountFetchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RefundAccountFetchServiceImpl implements RefundAccountFetchService {

    private final RefundAccountFetchRepository refundAccountFetchRepository;

    @Override
    public RefundAccountInfo fetchRefundAccountInfo(long userId){
        return refundAccountFetchRepository.fetchRefundAccount(userId)
                .orElseThrow(RefundAccountNotFoundException::new);
    }

}
