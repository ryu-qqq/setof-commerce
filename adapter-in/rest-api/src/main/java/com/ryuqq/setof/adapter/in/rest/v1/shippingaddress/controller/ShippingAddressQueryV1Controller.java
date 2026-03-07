package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.ShippingAddressV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response.ShippingAddressV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.mapper.ShippingAddressV1ApiMapper;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ShippingAddressQueryV1Controller - 배송지 조회 V1 Public API.
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
@Tag(name = "배송지 조회 V1", description = "배송지 조회 V1 Public API (인증 필요)")
@RestController
@RequestMapping(ShippingAddressV1Endpoints.ADDRESS_BOOK)
public class ShippingAddressQueryV1Controller {

    private final GetShippingAddressesUseCase getShippingAddressesUseCase;
    private final GetShippingAddressUseCase getShippingAddressUseCase;
    private final ShippingAddressV1ApiMapper mapper;

    public ShippingAddressQueryV1Controller(
            GetShippingAddressesUseCase getShippingAddressesUseCase,
            GetShippingAddressUseCase getShippingAddressUseCase,
            ShippingAddressV1ApiMapper mapper) {
        this.getShippingAddressesUseCase = getShippingAddressesUseCase;
        this.getShippingAddressUseCase = getShippingAddressUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "배송지 목록 조회", description = "인증된 사용자의 배송지 목록을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping
    public ResponseEntity<V1ApiResponse<List<ShippingAddressV1ApiResponse>>> getShippingAddresses(
            @AuthenticatedUserId Long userId) {
        List<ShippingAddressV1ApiResponse> response =
                mapper.toResponseList(getShippingAddressesUseCase.execute(userId));
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(summary = "배송지 단건 조회", description = "인증된 사용자의 특정 배송지를 ID로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "배송지를 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(ShippingAddressV1Endpoints.ADDRESS_BOOK_ID)
    public ResponseEntity<V1ApiResponse<ShippingAddressV1ApiResponse>> getShippingAddress(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "배송지 ID", required = true)
                    @PathVariable(ShippingAddressV1Endpoints.PATH_SHIPPING_ADDRESS_ID)
                    Long shippingAddressId) {
        ShippingAddressV1ApiResponse response =
                mapper.toResponse(getShippingAddressUseCase.execute(userId, shippingAddressId));
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
