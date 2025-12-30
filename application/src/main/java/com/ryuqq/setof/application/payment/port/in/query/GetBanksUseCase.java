package com.ryuqq.setof.application.payment.port.in.query;

import com.ryuqq.setof.application.payment.dto.response.BankResponse;
import java.util.List;

/**
 * GetBanksUseCase - 은행 목록 조회 UseCase
 *
 * <p>가상계좌 및 환불 가능 은행 목록을 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetBanksUseCase {

    /**
     * 가상계좌 지원 은행 목록 조회
     *
     * @return 가상계좌 지원 은행 목록
     */
    List<BankResponse> getVirtualAccountBanks();

    /**
     * 환불 지원 은행 목록 조회
     *
     * @return 환불 지원 은행 목록
     */
    List<BankResponse> getRefundBanks();
}
