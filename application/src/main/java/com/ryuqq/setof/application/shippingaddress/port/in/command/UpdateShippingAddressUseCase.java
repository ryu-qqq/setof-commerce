package com.ryuqq.setof.application.shippingaddress.port.in.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;

/**
 * Update ShippingAddress UseCase (Command)
 *
 * <p>배송지 수정을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateShippingAddressUseCase {

    /**
     * 배송지 수정 실행
     *
     * @param command 배송지 수정 커맨드
     * @return 수정된 배송지 정보
     */
    ShippingAddressResponse execute(UpdateShippingAddressCommand command);
}
