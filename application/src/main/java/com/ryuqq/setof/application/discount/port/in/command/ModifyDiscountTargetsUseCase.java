package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;

/**
 * 할인 적용 대상 수정 UseCase.
 *
 * <p>특정 할인 정책의 적용 대상 목록을 교체(replace)합니다. 기존 대상은 비활성화되고 새로운 대상이 등록됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ModifyDiscountTargetsUseCase {

    /**
     * 할인 적용 대상을 수정합니다.
     *
     * @param command 대상 수정 Command (정책 ID, 대상 유형, 신규 대상 ID 목록)
     */
    void execute(ModifyDiscountTargetsCommand command);
}
