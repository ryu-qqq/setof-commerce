package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;

/**
 * 할인 정책 생성 UseCase.
 *
 * <p>신규 할인 정책과 적용 대상을 함께 등록합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CreateDiscountPolicyUseCase {

    /**
     * 할인 정책을 생성합니다.
     *
     * @param command 할인 정책 생성 Command
     * @return 생성된 할인 정책 ID
     */
    long execute(CreateDiscountPolicyCommand command);
}
