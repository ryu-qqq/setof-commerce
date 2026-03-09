package com.ryuqq.setof.application.payment.port.out.client;

import java.util.Optional;

/**
 * PaymentGatewayClient - 외부 PG사 결제 상태 확인 클라이언트 Port.
 *
 * <p>PortOne(Iamport) 등 외부 PG 연동 어댑터에서 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface PaymentGatewayClient {

    /**
     * PG사 거래 ID로 결제 상태를 조회합니다.
     *
     * <p>PG사 API를 호출하여 실제 결제 상태를 확인합니다.
     *
     * @param pgAgencyId PG사 거래 ID (PortOne의 imp_uid)
     * @return 결제 완료(paid) 여부. PG 조회 실패 시 empty.
     */
    Optional<Boolean> isPaid(String pgAgencyId);
}
