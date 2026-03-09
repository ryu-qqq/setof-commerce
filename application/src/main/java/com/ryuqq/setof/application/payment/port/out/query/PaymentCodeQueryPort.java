package com.ryuqq.setof.application.payment.port.out.query;

import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResult;
import com.ryuqq.setof.domain.commoncode.aggregate.CommonCode;
import java.util.List;

/**
 * PaymentCodeQueryPort - 결제 관련 코드 조회 Port.
 *
 * <p>결제 수단, 가상계좌 은행, 환불 은행 목록을 조회합니다.
 *
 * <p>은행 코드는 {@link CommonCode} 도메인으로 반환하고, 결제 수단은 merchantKey 포함을 위해 {@link PaymentMethodResult}로
 * 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface PaymentCodeQueryPort {

    /**
     * 활성화된 결제 수단 목록 조회.
     *
     * @return 결제 수단 목록
     */
    List<PaymentMethodResult> findActivePaymentMethods();

    /**
     * 가상계좌 환불용 은행 코드 목록 조회.
     *
     * @return CommonCode 도메인 목록 (code_group_id=10)
     */
    List<CommonCode> findVBankCodes();

    /**
     * 환불 계좌용 은행 코드 목록 조회.
     *
     * @return CommonCode 도메인 목록 (code_group_id=12)
     */
    List<CommonCode> findRefundBankCodes();
}
