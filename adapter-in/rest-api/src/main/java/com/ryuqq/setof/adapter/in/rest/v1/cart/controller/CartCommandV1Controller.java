package com.ryuqq.setof.adapter.in.rest.v1.cart.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.cart.CartV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.AddCartItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.DeleteCartItemsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.request.ModifyCartItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.mapper.CartV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.DeleteCartItemsCommand;
import com.ryuqq.setof.application.cart.dto.command.ModifyCartItemCommand;
import com.ryuqq.setof.application.cart.port.in.command.AddCartItemUseCase;
import com.ryuqq.setof.application.cart.port.in.command.DeleteCartItemsUseCase;
import com.ryuqq.setof.application.cart.port.in.command.ModifyCartItemUseCase;
import com.ryuqq.setof.domain.cart.aggregate.CartItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * CartCommandV1Controller - 장바구니 명령 V1 Public API.
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
 * <p>레거시 호환:
 *
 * <ul>
 *   <li>POST /api/v1/cart
 *   <li>PUT /api/v1/cart/{cartId}
 *   <li>DELETE /api/v1/carts
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "장바구니 명령 V1", description = "장바구니 추가/수정/삭제 V1 Public API (인증 필요)")
@RestController
public class CartCommandV1Controller {

    private final AddCartItemUseCase addCartItemUseCase;
    private final ModifyCartItemUseCase modifyCartItemUseCase;
    private final DeleteCartItemsUseCase deleteCartItemsUseCase;
    private final CartV1ApiMapper mapper;

    public CartCommandV1Controller(
            AddCartItemUseCase addCartItemUseCase,
            ModifyCartItemUseCase modifyCartItemUseCase,
            DeleteCartItemsUseCase deleteCartItemsUseCase,
            CartV1ApiMapper mapper) {
        this.addCartItemUseCase = addCartItemUseCase;
        this.modifyCartItemUseCase = modifyCartItemUseCase;
        this.deleteCartItemsUseCase = deleteCartItemsUseCase;
        this.mapper = mapper;
    }

    @Operation(
            summary = "장바구니 항목 추가",
            description = "인증된 사용자의 장바구니에 항목을 추가합니다. 동일 상품이 이미 존재하면 수량을 덮어씁니다(Upsert).")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "추가 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping(CartV1Endpoints.CART)
    public ResponseEntity<V1ApiResponse<List<CartV1ApiResponse>>> addCartItems(
            @AuthenticatedUserId Long userId,
            @Valid @RequestBody List<AddCartItemV1ApiRequest> requests) {

        AddCartItemCommand command = mapper.toAddCommand(userId, requests);
        List<CartItem> cartItems = addCartItemUseCase.execute(command);
        List<CartV1ApiResponse> response = mapper.toCartResponseList(cartItems);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }

    @Operation(summary = "장바구니 항목 수량 수정", description = "인증된 사용자의 특정 장바구니 항목 수량을 수정합니다.")
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
                description = "장바구니 항목을 찾을 수 없음")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping(CartV1Endpoints.CART_BY_ID)
    public ResponseEntity<V1ApiResponse<CartV1ApiResponse>> modifyCartItem(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "수정할 장바구니 항목 ID", required = true)
                    @PathVariable(CartV1Endpoints.PATH_CART_ID)
                    Long cartId,
            @Valid @RequestBody ModifyCartItemV1ApiRequest request) {

        ModifyCartItemCommand command = mapper.toModifyCommand(cartId, userId, request);
        modifyCartItemUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(null));
    }

    @Operation(summary = "장바구니 항목 삭제", description = "인증된 사용자의 특정 장바구니 항목을 소프트 딜리트합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping(CartV1Endpoints.CARTS)
    public ResponseEntity<V1ApiResponse<Integer>> deleteCartItems(
            @AuthenticatedUserId Long userId, @ModelAttribute DeleteCartItemsV1ApiRequest request) {

        DeleteCartItemsCommand command = mapper.toDeleteCommand(userId, request);
        int deletedCount = deleteCartItemsUseCase.execute(command);
        return ResponseEntity.ok(V1ApiResponse.success(deletedCount));
    }
}
