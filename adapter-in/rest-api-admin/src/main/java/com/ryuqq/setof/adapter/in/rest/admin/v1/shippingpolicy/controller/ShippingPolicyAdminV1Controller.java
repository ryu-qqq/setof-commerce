package com.ryuqq.setof.adapter.in.rest.admin.v1.shippingpolicy.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.shippingpolicy.dto.command.CreateDeliveryNoticeV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.shippingpolicy.mapper.ShippingPolicyAdminV1ApiMapper;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetDefaultShippingAddressUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Shipping Policy Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Product 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Shipping Policy (Legacy V1)", description = "레거시 Shipping Policy API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ShippingPolicyAdminV1Controller {

    private final GetDefaultShippingAddressUseCase getDefaultShippingAddressUseCase;
    private final UpdateShippingPolicyUseCase updateShippingPolicyUseCase;
    private final ShippingPolicyAdminV1ApiMapper shippingPolicyAdminV1ApiMapper;

    public ShippingPolicyAdminV1Controller(
            GetDefaultShippingAddressUseCase getDefaultShippingAddressUseCase,
            UpdateShippingPolicyUseCase updateShippingPolicyUseCase,
            ShippingPolicyAdminV1ApiMapper shippingPolicyAdminV1ApiMapper) {
        this.getDefaultShippingAddressUseCase = getDefaultShippingAddressUseCase;
        this.updateShippingPolicyUseCase = updateShippingPolicyUseCase;
        this.shippingPolicyAdminV1ApiMapper = shippingPolicyAdminV1ApiMapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배송 고지 수정", description = "배송 고지를 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/notice/delivery")
    public ResponseEntity<V1ApiResponse<Long>> updateProductDeliveryNotice(
            @PathVariable long productGroupId,
            @Valid @RequestBody CreateDeliveryNoticeV1ApiRequest deliveryNotice) {

        ShippingPolicyResponse shippingPolicyResponse =
                getDefaultShippingAddressUseCase.execute(0L);
        UpdateShippingPolicyCommand updateCommand =
                shippingPolicyAdminV1ApiMapper.toUpdateCommand(
                        shippingPolicyResponse, deliveryNotice);

        updateShippingPolicyUseCase.execute(updateCommand);

        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }
}
