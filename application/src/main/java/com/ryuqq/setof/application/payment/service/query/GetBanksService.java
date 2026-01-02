package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.payment.dto.response.BankResponse;
import com.ryuqq.setof.application.payment.port.in.query.GetBanksUseCase;
import com.ryuqq.setof.domain.payment.vo.BankCode;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetBanksService - 은행 목록 조회 서비스
 *
 * <p>가상계좌 및 환불 가능 은행 목록을 조회합니다. 은행 목록은 Enum에서 관리됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetBanksService implements GetBanksUseCase {

    @Override
    public List<BankResponse> getVirtualAccountBanks() {
        return BankCode.getVirtualAccountBanks().stream().map(BankResponse::from).toList();
    }

    @Override
    public List<BankResponse> getRefundBanks() {
        return BankCode.getRefundBanks().stream().map(BankResponse::from).toList();
    }
}
