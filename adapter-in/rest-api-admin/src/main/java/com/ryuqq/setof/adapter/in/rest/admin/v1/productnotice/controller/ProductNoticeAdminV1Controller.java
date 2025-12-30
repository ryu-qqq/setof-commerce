package com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.dto.command.CreateProductNoticeV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.mapper.ProductNoticeAdminV1ApiMapper;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
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
 * V1 Product Notice Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Product Notice 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Product Notice (Legacy V1)", description = "레거시 Product Notice API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
@PreAuthorize("@access.orgAdminOrHigher()")
public class ProductNoticeAdminV1Controller {

    private final UpdateProductNoticeUseCase updateProductNoticeUseCase;
    private final ProductNoticeAdminV1ApiMapper productNoticeAdminV1ApiMapper;

    public ProductNoticeAdminV1Controller(
            UpdateProductNoticeUseCase updateProductNoticeUseCase,
            ProductNoticeAdminV1ApiMapper productNoticeAdminV1ApiMapper) {
        this.updateProductNoticeUseCase = updateProductNoticeUseCase;
        this.productNoticeAdminV1ApiMapper = productNoticeAdminV1ApiMapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 상품 고지 수정", description = "상품 고지를 수정합니다.")
    @PutMapping("/product/group/{productGroupId}/notice")
    public ResponseEntity<V1ApiResponse<Long>> updateProductNotice(
            @PathVariable long productGroupId,
            @Valid @RequestBody CreateProductNoticeV1ApiRequest createProductNotice) {

        UpdateProductNoticeCommand updateCommand =
                productNoticeAdminV1ApiMapper.toUpdateCommand(productGroupId, createProductNotice);

        updateProductNoticeUseCase.execute(updateCommand);

        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }
}
