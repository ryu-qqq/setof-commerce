package com.setof.connectly.module.payment.service.vbank.fetch;

import com.setof.connectly.module.payment.dto.refund.BankResponse;
import java.util.List;

public interface VBankFindService {
    List<BankResponse> fetchVBanks();

    List<BankResponse> fetchRefundBanks();
}
