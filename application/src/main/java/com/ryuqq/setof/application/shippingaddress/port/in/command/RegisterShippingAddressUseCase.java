package com.ryuqq.setof.application.shippingaddress.port.in.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;

/**
 * Register ShippingAddress UseCase (Command)
 *
 * <p>배송지 등록을 담당하는 Inbound Port
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>회원당 최대 5개까지 등록 가능
 *   <li>기본 배송지로 등록 시 기존 기본 배송지 해제
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterShippingAddressUseCase {

    /**
     * 배송지 등록 실행
     *
     * @param command 배송지 등록 커맨드
     * @return 등록된 배송지 정보
     */
    ShippingAddressResponse execute(RegisterShippingAddressCommand command);
}
