package com.ryuqq.setof.application.bank.port.in.query;

import com.ryuqq.setof.application.bank.dto.response.BankResponse;
import java.util.List;

/**
 * Get Banks UseCase (Query)
 *
 * <p>활성화된 은행 목록을 조회하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetBanksUseCase {

    /**
     * 활성 은행 목록 조회 실행
     *
     * @return 활성 은행 목록
     */
    List<BankResponse> execute();
}
