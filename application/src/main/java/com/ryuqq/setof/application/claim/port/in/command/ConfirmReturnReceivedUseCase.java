package com.ryuqq.setof.application.claim.port.in.command;

import com.ryuqq.setof.application.claim.dto.command.ConfirmReturnReceivedCommand;

/**
 * ConfirmReturnReceivedUseCase - 반품 수령 확인 UseCase
 *
 * <p>판매자가 반품 상품을 수령하고 검수 결과를 등록합니다. 검수 결과에 따라 클레임 상태가 변경됩니다: - PASS: 환불/교환 진행 (COMPLETED) - FAIL:
 * 반품 거절 (REJECTED) - PARTIAL: 부분 환불 처리
 *
 * @author development-team
 * @since 2.0.0
 */
public interface ConfirmReturnReceivedUseCase {

    /**
     * 반품 수령 확인 및 검수
     *
     * @param command 반품 수령 확인 Command
     */
    void confirmReceived(ConfirmReturnReceivedCommand command);
}
