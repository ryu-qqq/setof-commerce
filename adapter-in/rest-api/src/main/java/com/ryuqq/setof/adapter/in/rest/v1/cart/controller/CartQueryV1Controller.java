package com.ryuqq.setof.adapter.in.rest.v1.cart.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.cart.CartV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.SearchCartsCursorV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.mapper.CartV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartSliceResult;
import com.ryuqq.setof.application.cart.port.in.query.GetCartCountUseCase;
import com.ryuqq.setof.application.cart.port.in.query.GetCartsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 * CartQueryV1Controller - 장바구니 조회 V1 Public API.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "장바구니 조회 V1", description = "장바구니 조회 V1 Public API (인증 필요)")
@RestController
public class CartQueryV1Controller {

    private final GetCartCountUseCase getCartCountUseCase;
    private final GetCartsUseCase getCartsUseCase;
    private final CartV1ApiMapper mapper;

    public CartQueryV1Controller(
            GetCartCountUseCase getCartCountUseCase,
            GetCartsUseCase getCartsUseCase,
            CartV1ApiMapper mapper) {
        this.getCartCountUseCase = getCartCountUseCase;
        this.getCartsUseCase = getCartsUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "장바구니 아이템 개수 조회", description = "인증된 사용자의 장바구니 아이템 개수를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(CartV1Endpoints.CART_COUNT)
    public ResponseEntity<V1ApiResponse<CartCountV1ApiResponse>> getCartCount(
            @AuthenticatedUserId Long userId) {

        CartSearchParams params = mapper.toCountParams(userId);
        CartCountResult result = getCartCountUseCase.execute(params);
        CartCountV1ApiResponse response = mapper.toCartCountResponse(result);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(summary = "장바구니 목록 조회", description = "인증된 사용자의 장바구니 목록을 커서 기반 페이지네이션으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(CartV1Endpoints.CARTS)
    public ResponseEntity<V1ApiResponse<CartSliceV1ApiResponse>> getCarts(
            @AuthenticatedUserId Long userId,
            @ModelAttribute SearchCartsCursorV1ApiRequest request) {

        CartSearchParams params = mapper.toSearchParams(userId, request);
        CartSliceResult result = getCartsUseCase.execute(params);
        CartSliceV1ApiResponse response =
                mapper.toCartSliceResponse(result, request.lastDomainId());
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
