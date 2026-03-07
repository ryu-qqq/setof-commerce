package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.ShippingAddressV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.RegisterShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.UpdateShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.mapper.ShippingAddressV1ApiMapper;
import com.ryuqq.setof.application.shippingaddress.port.in.command.DeleteShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.RegisterShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.UpdateShippingAddressUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ShippingAddressCommandV1Controller - 배송지 명령 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * <p>인증 필수 엔드포인트: @AuthenticatedUserId로 userId 추출.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "배송지 명령 V1", description = "배송지 등록/수정/삭제 V1 Public API (인증 필요)")
@RestController
@RequestMapping(ShippingAddressV1Endpoints.ADDRESS_BOOK)
public class ShippingAddressCommandV1Controller {

    private final RegisterShippingAddressUseCase registerShippingAddressUseCase;
    private final UpdateShippingAddressUseCase updateShippingAddressUseCase;
    private final DeleteShippingAddressUseCase deleteShippingAddressUseCase;
    private final ShippingAddressV1ApiMapper mapper;

    public ShippingAddressCommandV1Controller(
            RegisterShippingAddressUseCase registerShippingAddressUseCase,
            UpdateShippingAddressUseCase updateShippingAddressUseCase,
            DeleteShippingAddressUseCase deleteShippingAddressUseCase,
            ShippingAddressV1ApiMapper mapper) {
        this.registerShippingAddressUseCase = registerShippingAddressUseCase;
        this.updateShippingAddressUseCase = updateShippingAddressUseCase;
        this.deleteShippingAddressUseCase = deleteShippingAddressUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "배송지 등록", description = "인증된 사용자의 배송지를 등록합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "등록 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패 또는 최대 개수 초과"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping
    public ResponseEntity<V1ApiResponse<Long>> registerShippingAddress(
            @AuthenticatedUserId Long userId,
            @Valid @RequestBody RegisterShippingAddressV1ApiRequest request) {
        Long id = registerShippingAddressUseCase.execute(mapper.toRegisterCommand(userId, request));
        return ResponseEntity.ok(V1ApiResponse.success(id));
    }

    @Operation(summary = "배송지 수정", description = "인증된 사용자의 특정 배송지를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "수정 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배송지를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping(ShippingAddressV1Endpoints.ADDRESS_BOOK_ID)
    public ResponseEntity<V1ApiResponse<Void>> updateShippingAddress(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "배송지 ID", required = true)
                    @PathVariable(ShippingAddressV1Endpoints.PATH_SHIPPING_ADDRESS_ID)
                    Long shippingAddressId,
            @Valid @RequestBody UpdateShippingAddressV1ApiRequest request) {
        updateShippingAddressUseCase.execute(
                mapper.toUpdateCommand(userId, shippingAddressId, request));
        return ResponseEntity.ok(V1ApiResponse.success(null));
    }

    @Operation(summary = "배송지 삭제", description = "인증된 사용자의 특정 배송지를 삭제합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배송지를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping(ShippingAddressV1Endpoints.ADDRESS_BOOK_ID)
    public ResponseEntity<V1ApiResponse<Void>> deleteShippingAddress(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "배송지 ID", required = true)
                    @PathVariable(ShippingAddressV1Endpoints.PATH_SHIPPING_ADDRESS_ID)
                    Long shippingAddressId) {
        deleteShippingAddressUseCase.execute(mapper.toDeleteCommand(userId, shippingAddressId));
        return ResponseEntity.ok(V1ApiResponse.success(null));
    }
}
