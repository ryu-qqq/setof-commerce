package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;

/**
 * 할인 정책 수정 UseCase.
 *
 * <p>기존 할인 정책의 수정 가능한 필드를 업데이트합니다. applicationType, publisherType, sellerId, stackingGroup은 불변 필드로
 * 변경 불가합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UpdateDiscountPolicyUseCase {

    /**
     * 할인 정책을 수정합니다.
     *
     * @param command 할인 정책 수정 Command
     */
    void execute(UpdateDiscountPolicyCommand command);
}
