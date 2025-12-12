package com.ryuqq.setof.application.shippingpolicy.port.in.command;

import com.ryuqq.setof.application.shippingpolicy.dto.command.SetDefaultShippingPolicyCommand;

/**
 * 기본 배송 정책 설정 UseCase (Port-In)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SetDefaultShippingPolicyUseCase {

    /**
     * 기본 배송 정책 설정
     *
     * @param command 기본 정책 설정 커맨드
     */
    void execute(SetDefaultShippingPolicyCommand command);
}
