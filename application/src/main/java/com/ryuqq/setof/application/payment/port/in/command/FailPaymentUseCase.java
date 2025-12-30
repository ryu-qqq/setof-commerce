package com.ryuqq.setof.application.payment.port.in.command;

import com.ryuqq.setof.application.payment.dto.command.FailPaymentCommand;
import com.ryuqq.setof.application.payment.dto.response.PaymentResponse;

/**
 * 결제 실패 UseCase
 *
 * <p>결제 실패 처리를 위한 Port-In 인터페이스입니다.
 *
 * <p>결제 실패 시:
 *
 * <ul>
 *   <li>Payment 상태를 FAILED로 변경
 *   <li>관련된 장바구니 아이템 복원 (소프트 딜리트 해제)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface FailPaymentUseCase {

    /**
     * 결제 실패 처리
     *
     * @param command 결제 실패 Command
     * @return 실패 처리된 결제 응답
     */
    PaymentResponse failPayment(FailPaymentCommand command);
}
