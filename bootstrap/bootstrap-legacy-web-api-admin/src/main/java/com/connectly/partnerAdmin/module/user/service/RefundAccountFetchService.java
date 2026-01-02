package com.connectly.partnerAdmin.module.user.service;

import com.connectly.partnerAdmin.module.user.dto.RefundAccountInfo;

public interface RefundAccountFetchService {
    RefundAccountInfo fetchRefundAccountInfo(long userId);
}
