package com.ryuqq.setof.adapter.in.rest.admin.v1.refundpolicy.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.refundpolicy.dto.command.CreateRefundNoticeV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.refundpolicy.mapper.RefundPolicyAdminV1ApiMapper;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetDefaultRefundPolicyUseCase;
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
 * V1 Refund Policy Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Product 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Refund Policy (Legacy V1)", description = "레거시 Refund Policy API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
@PreAuthorize("@access.orgAdminOrHigher()")
public class RefundPolicyAdminV1Controller {

    private final GetDefaultRefundPolicyUseCase getDefaultRefundPolicyUseCase;
    private final RefundPolicyAdminV1ApiMapper refundPolicyAdminV1ApiMapper;
    private final UpdateRefundPolicyUseCase updateRefundPolicyUseCase;

    public RefundPolicyAdminV1Controller(
            GetDefaultRefundPolicyUseCase getDefaultRefundPolicyUseCase,
            RefundPolicyAdminV1ApiMapper refundPolicyAdminV1ApiMapper,
            UpdateRefundPolicyUseCase updateRefundPolicyUseCase) {
        this.getDefaultRefundPolicyUseCase = getDefaultRefundPolicyUseCase;
        this.refundPolicyAdminV1ApiMapper = refundPolicyAdminV1ApiMapper;
        this.updateRefundPolicyUseCase = updateRefundPolicyUseCase;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 환불 고지 수정", description = "환불 고지를 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/notice/refund")
    public ResponseEntity<V1ApiResponse<Long>> updateProductRefundNotice(
            @PathVariable long productGroupId,
            @Valid @RequestBody CreateRefundNoticeV1ApiRequest refundNotice) {

        RefundPolicyResponse refundPolicyResponse = getDefaultRefundPolicyUseCase.execute(0L);
        UpdateRefundPolicyCommand updateRefundPolicyCommand =
                refundPolicyAdminV1ApiMapper.toUpdateRefundPolicyCommand(
                        refundPolicyResponse, refundNotice);

        updateRefundPolicyUseCase.execute(updateRefundPolicyCommand);

        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }
}
