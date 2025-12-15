package com.setof.connectly.module.payment.repository.vbank;

import com.setof.connectly.module.payment.dto.refund.BankResponse;
import java.util.List;

public interface VBankFindRepository {

    List<BankResponse> fetchVBankAccounts();

    List<BankResponse> fetchRefundBanks();
}
