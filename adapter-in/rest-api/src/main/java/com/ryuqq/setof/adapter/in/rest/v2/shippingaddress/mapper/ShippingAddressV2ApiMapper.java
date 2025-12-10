package com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command.RegisterShippingAddressV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command.UpdateShippingAddressV2ApiRequest;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.SetDefaultShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * ShippingAddress V2 API Mapper
 *
 * <p>배송지 관련 API DTO ↔ Application Command 변환
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>@Component로 DI (Static 금지)
 *   <li>비즈니스 로직 금지 - 순수 변환만
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ShippingAddressV2ApiMapper {

    /**
     * 배송지 등록 요청 → 등록 커맨드 변환
     *
     * @param memberId 회원 ID
     * @param request API 요청
     * @return RegisterShippingAddressCommand
     */
    public RegisterShippingAddressCommand toRegisterCommand(
            UUID memberId, RegisterShippingAddressV2ApiRequest request) {
        return RegisterShippingAddressCommand.of(
                memberId,
                request.addressName(),
                request.receiverName(),
                request.receiverPhone(),
                request.roadAddress(),
                request.jibunAddress(),
                request.detailAddress(),
                request.zipCode(),
                request.deliveryRequest(),
                request.isDefault());
    }

    /**
     * 배송지 수정 요청 → 수정 커맨드 변환
     *
     * @param memberId 회원 ID
     * @param shippingAddressId 배송지 ID
     * @param request API 요청
     * @return UpdateShippingAddressCommand
     */
    public UpdateShippingAddressCommand toUpdateCommand(
            UUID memberId, Long shippingAddressId, UpdateShippingAddressV2ApiRequest request) {
        return UpdateShippingAddressCommand.of(
                memberId,
                shippingAddressId,
                request.addressName(),
                request.receiverName(),
                request.receiverPhone(),
                request.roadAddress(),
                request.jibunAddress(),
                request.detailAddress(),
                request.zipCode(),
                request.deliveryRequest());
    }

    /**
     * 배송지 삭제 커맨드 생성
     *
     * @param memberId 회원 ID
     * @param shippingAddressId 배송지 ID
     * @return DeleteShippingAddressCommand
     */
    public DeleteShippingAddressCommand toDeleteCommand(UUID memberId, Long shippingAddressId) {
        return DeleteShippingAddressCommand.of(memberId, shippingAddressId);
    }

    /**
     * 기본 배송지 설정 커맨드 생성
     *
     * @param memberId 회원 ID
     * @param shippingAddressId 배송지 ID
     * @return SetDefaultShippingAddressCommand
     */
    public SetDefaultShippingAddressCommand toSetDefaultCommand(
            UUID memberId, Long shippingAddressId) {
        return SetDefaultShippingAddressCommand.of(memberId, shippingAddressId);
    }
}
