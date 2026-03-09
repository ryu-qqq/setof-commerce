package com.ryuqq.setof.application.payment.port.in.query;

import com.ryuqq.setof.application.commoncode.dto.response.CommonCodeResult;
import java.util.List;

/**
 * GetRefundBankCodesUseCase - 환불 계좌용 은행 코드 목록 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetRefundBankCodesUseCase {

    /**
     * 환불 계좌용 은행 코드 목록 조회.
     *
     * @return 은행 코드 목록
     */
    List<CommonCodeResult> execute();
}
