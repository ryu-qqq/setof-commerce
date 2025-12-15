package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.commnad.AddressBookV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response.AddressBookV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response.AddressBookV1ApiResponse.ShippingDetailsV1Response;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * ShippingAddress V1 API Mapper
 *
 * <p>배송지 관련 V1 API DTO ↔ Application Command/Response 변환
 *
 * <p>레거시 V1 스펙 유지하면서 새로운 UseCase 호출을 위한 변환
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Component
@Deprecated
public class ShippingAddressV1ApiMapper {

    /**
     * V1 배송지 등록 요청 → 등록 커맨드 변환
     *
     * @param memberId 회원 ID
     * @param request V1 API 요청
     * @return RegisterShippingAddressCommand
     */
    public RegisterShippingAddressCommand toRegisterCommand(
            UUID memberId, AddressBookV1ApiRequest request) {
        return RegisterShippingAddressCommand.of(
                memberId,
                request.addressName(),
                request.recipientName(),
                request.recipientPhone(),
                request.address(), // V1에서는 address를 roadAddress로 사용
                null, // jibunAddress - V1에서는 미지원
                request.addressDetail(),
                request.zipCode(),
                null, // deliveryRequest - V1에서는 미지원
                "Y".equals(request.defaultYn()));
    }

    /**
     * V1 배송지 수정 요청 → 수정 커맨드 변환
     *
     * @param memberId 회원 ID
     * @param shippingAddressId 배송지 ID
     * @param request V1 API 요청
     * @return UpdateShippingAddressCommand
     */
    public UpdateShippingAddressCommand toUpdateCommand(
            UUID memberId, Long shippingAddressId, AddressBookV1ApiRequest request) {
        return UpdateShippingAddressCommand.of(
                memberId,
                shippingAddressId,
                request.addressName(),
                request.recipientName(),
                request.recipientPhone(),
                request.address(), // V1에서는 address를 roadAddress로 사용
                null, // jibunAddress - V1에서는 미지원
                request.addressDetail(),
                request.zipCode(),
                null); // deliveryRequest - V1에서는 미지원
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
     * Application Response → V1 API Response 변환
     *
     * @param response Application 응답
     * @return AddressBookV1ApiResponse
     */
    public AddressBookV1ApiResponse toV1Response(ShippingAddressResponse response) {
        ShippingDetailsV1Response details =
                new ShippingDetailsV1Response(
                        response.receiverName(),
                        response.addressName(),
                        response.roadAddress(),
                        response.detailAddress(),
                        response.zipCode(),
                        "KR", // country - V1 기본값
                        response.deliveryRequest(),
                        response.receiverPhone());

        return new AddressBookV1ApiResponse(
                response.id(), details, response.isDefault() ? "Y" : "N");
    }

    /**
     * Application Response 목록 → V1 API Response 목록 변환
     *
     * @param responses Application 응답 목록
     * @return V1 API 응답 목록
     */
    public List<AddressBookV1ApiResponse> toV1Responses(List<ShippingAddressResponse> responses) {
        return responses.stream().map(this::toV1Response).toList();
    }
}
