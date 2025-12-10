package com.ryuqq.setof.application.shippingaddress.port.in.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.SetDefaultShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;

/**
 * Set Default ShippingAddress UseCase (Command)
 *
 * <p>기본 배송지 설정을 담당하는 Inbound Port
 *
 * <p>비즈니스 규칙:
 * <ul>
 *   <li>기존 기본 배송지가 있으면 해제 후 새로 설정
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SetDefaultShippingAddressUseCase {

    /**
     * 기본 배송지 설정 실행
     *
     * @param command 기본 배송지 설정 커맨드
     * @return 설정된 배송지 정보
     */
    ShippingAddressResponse execute(SetDefaultShippingAddressCommand command);
}
