package com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.dto.command.RegisterCarrierV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.dto.command.UpdateCarrierV2ApiRequest;
import com.ryuqq.setof.application.carrier.dto.command.RegisterCarrierCommand;
import com.ryuqq.setof.application.carrier.dto.command.UpdateCarrierCommand;
import org.springframework.stereotype.Component;

/**
 * Carrier Admin V2 API Mapper
 *
 * <p>택배사 관리 API DTO ↔ Application Command 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class CarrierAdminV2ApiMapper {

    /** 택배사 등록 요청 → 등록 커맨드 변환 */
    public RegisterCarrierCommand toRegisterCommand(RegisterCarrierV2ApiRequest request) {
        return new RegisterCarrierCommand(
                request.code(),
                request.name(),
                request.trackingUrlTemplate(),
                request.displayOrder());
    }

    /** 택배사 수정 요청 → 수정 커맨드 변환 */
    public UpdateCarrierCommand toUpdateCommand(Long carrierId, UpdateCarrierV2ApiRequest request) {
        return new UpdateCarrierCommand(
                carrierId, request.name(), request.trackingUrlTemplate(), request.displayOrder());
    }
}
