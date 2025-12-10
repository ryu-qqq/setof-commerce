package com.ryuqq.setof.application.shippingaddress.port.in.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;

/**
 * Delete ShippingAddress UseCase (Command)
 *
 * <p>배송지 삭제를 담당하는 Inbound Port
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>Soft Delete 적용
 *   <li>기본 배송지 삭제 시 가장 최근 등록 배송지로 자동 변경
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteShippingAddressUseCase {

    /**
     * 배송지 삭제 실행
     *
     * @param command 배송지 삭제 커맨드
     */
    void execute(DeleteShippingAddressCommand command);
}
