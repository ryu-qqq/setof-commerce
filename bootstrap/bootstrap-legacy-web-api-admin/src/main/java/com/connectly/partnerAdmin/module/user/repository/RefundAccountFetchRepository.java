package com.connectly.partnerAdmin.module.user.repository;


import com.connectly.partnerAdmin.module.user.dto.RefundAccountInfo;

import java.util.Optional;

public interface RefundAccountFetchRepository {
    Optional<RefundAccountInfo> fetchRefundAccount(long userId);
}
