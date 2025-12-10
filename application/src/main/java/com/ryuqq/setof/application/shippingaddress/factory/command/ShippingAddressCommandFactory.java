package com.ryuqq.setof.application.shippingaddress.factory.command;

import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.AddressName;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverInfo;
import org.springframework.stereotype.Component;

/**
 * ShippingAddress Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command DTO를 Domain 객체로 변환
 *   <li>도메인 생성 로직 캡슐화
 *   <li>Value Object 생성 책임
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingAddressCommandFactory {

    private final ClockHolder clockHolder;

    public ShippingAddressCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 신규 배송지 생성
     *
     * @param command 배송지 등록 커맨드
     * @return 생성된 ShippingAddress (저장 전, ID 없음)
     */
    public ShippingAddress create(RegisterShippingAddressCommand command) {
        return ShippingAddress.forNew(
                command.memberId(),
                AddressName.of(command.addressName()),
                ReceiverInfo.of(command.receiverName(), command.receiverPhone()),
                DeliveryAddress.of(
                        command.roadAddress(),
                        command.jibunAddress(),
                        command.detailAddress(),
                        command.zipCode()),
                toDeliveryRequest(command.deliveryRequest()),
                command.isDefault(),
                clockHolder.getClock());
    }

    /**
     * 기존 배송지 수정 적용
     *
     * <p>Domain 객체의 update 메서드를 호출하여 상태 변경
     *
     * @param shippingAddress 수정할 배송지 도메인 객체
     * @param command 수정 커맨드
     */
    public void applyUpdate(ShippingAddress shippingAddress, UpdateShippingAddressCommand command) {
        shippingAddress.update(
                AddressName.of(command.addressName()),
                ReceiverInfo.of(command.receiverName(), command.receiverPhone()),
                DeliveryAddress.of(
                        command.roadAddress(),
                        command.jibunAddress(),
                        command.detailAddress(),
                        command.zipCode()),
                toDeliveryRequest(command.deliveryRequest()),
                clockHolder.getClock());
    }

    private DeliveryRequest toDeliveryRequest(String deliveryRequest) {
        return (deliveryRequest != null && !deliveryRequest.isBlank())
                ? DeliveryRequest.of(deliveryRequest)
                : null;
    }
}
