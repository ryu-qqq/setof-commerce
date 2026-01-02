package com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command.ChangeInvoiceV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command.ChangeStatusV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command.RegisterShipmentV2ApiRequest;
import com.ryuqq.setof.application.shipment.dto.command.ChangeInvoiceCommand;
import com.ryuqq.setof.application.shipment.dto.command.ChangeShipmentStatusCommand;
import com.ryuqq.setof.application.shipment.dto.command.RegisterShipmentCommand;
import org.springframework.stereotype.Component;

/**
 * Shipment Admin V2 API Mapper
 *
 * <p>운송장 관리 API DTO ↔ Application Command 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ShipmentAdminV2ApiMapper {

    /** 운송장 등록 요청 → 등록 커맨드 변환 */
    public RegisterShipmentCommand toRegisterCommand(RegisterShipmentV2ApiRequest request) {
        return new RegisterShipmentCommand(
                request.sellerId(),
                request.checkoutId(),
                request.carrierId(),
                request.invoiceNumber(),
                request.senderName(),
                request.senderPhone(),
                request.senderAddress(),
                request.type());
    }

    /** 운송장 번호 변경 요청 → 변경 커맨드 변환 */
    public ChangeInvoiceCommand toChangeInvoiceCommand(ChangeInvoiceV2ApiRequest request) {
        return new ChangeInvoiceCommand(request.carrierId(), request.invoiceNumber());
    }

    /** 운송장 상태 변경 요청 → 상태 변경 커맨드 변환 */
    public ChangeShipmentStatusCommand toChangeStatusCommand(ChangeStatusV2ApiRequest request) {
        return new ChangeShipmentStatusCommand(request.status());
    }
}
