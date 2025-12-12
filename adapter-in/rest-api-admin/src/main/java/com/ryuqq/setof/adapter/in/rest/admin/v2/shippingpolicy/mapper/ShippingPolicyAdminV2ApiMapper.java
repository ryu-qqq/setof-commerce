package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.UpdateShippingPolicyV2ApiRequest;
import com.ryuqq.setof.application.shippingpolicy.dto.command.DeleteShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.SetDefaultShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.query.ShippingPolicySearchQuery;
import org.springframework.stereotype.Component;

/**
 * ShippingPolicy Admin V2 API Mapper
 *
 * <p>배송 정책 관리 API DTO ↔ Application Command 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ShippingPolicyAdminV2ApiMapper {

    /** 배송 정책 등록 요청 → 등록 커맨드 변환 */
    public RegisterShippingPolicyCommand toRegisterCommand(
            RegisterShippingPolicyV2ApiRequest request) {
        return new RegisterShippingPolicyCommand(
                request.sellerId(),
                request.policyName(),
                request.defaultDeliveryCost(),
                request.freeShippingThreshold(),
                request.deliveryGuide(),
                request.isDefault(),
                request.displayOrder());
    }

    /** 배송 정책 수정 요청 → 수정 커맨드 변환 */
    public UpdateShippingPolicyCommand toUpdateCommand(
            Long shippingPolicyId, UpdateShippingPolicyV2ApiRequest request) {
        return new UpdateShippingPolicyCommand(
                shippingPolicyId,
                request.policyName(),
                request.defaultDeliveryCost(),
                request.freeShippingThreshold(),
                request.deliveryGuide(),
                request.displayOrder());
    }

    /** 기본 정책 설정 커맨드 생성 */
    public SetDefaultShippingPolicyCommand toSetDefaultCommand(
            Long shippingPolicyId, Long sellerId) {
        return new SetDefaultShippingPolicyCommand(shippingPolicyId, sellerId);
    }

    /** 배송 정책 삭제 커맨드 생성 */
    public DeleteShippingPolicyCommand toDeleteCommand(Long shippingPolicyId, Long sellerId) {
        return new DeleteShippingPolicyCommand(shippingPolicyId, sellerId);
    }

    /** 검색 쿼리 생성 */
    public ShippingPolicySearchQuery toSearchQuery(Long sellerId, boolean includeDeleted) {
        return new ShippingPolicySearchQuery(sellerId, includeDeleted);
    }
}
