package com.ryuqq.setof.adapter.in.rest.v2.cart.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.AddCartItemV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.RemoveCartItemsV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateAllSelectedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateCartItemQuantityV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateCartItemSelectedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.response.CartItemV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.response.CartV2ApiResponse;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.ClearCartCommand;
import com.ryuqq.setof.application.cart.dto.command.RemoveCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemQuantityCommand;
import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemSelectedCommand;
import com.ryuqq.setof.application.cart.dto.response.CartItemResponse;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Cart V2 API Mapper
 *
 * <p>API Request/Response DTO와 Application Command/Response 간 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class CartV2ApiMapper {

    /**
     * 아이템 추가 요청을 Command로 변환
     *
     * @param memberId 회원 ID (UUID)
     * @param request API 요청
     * @return AddCartItemCommand
     */
    public AddCartItemCommand toAddItemCommand(UUID memberId, AddCartItemV2ApiRequest request) {
        return new AddCartItemCommand(
                memberId,
                request.productStockId(),
                request.productId(),
                request.productGroupId(),
                request.sellerId(),
                request.quantity(),
                request.unitPrice());
    }

    /**
     * 수량 변경 요청을 Command로 변환
     *
     * @param memberId 회원 ID (UUID)
     * @param cartItemId 장바구니 아이템 ID
     * @param request API 요청
     * @return UpdateCartItemQuantityCommand
     */
    public UpdateCartItemQuantityCommand toUpdateQuantityCommand(
            UUID memberId, Long cartItemId, UpdateCartItemQuantityV2ApiRequest request) {
        return new UpdateCartItemQuantityCommand(memberId, cartItemId, request.quantity());
    }

    /**
     * 선택 상태 변경 요청을 Command로 변환
     *
     * @param memberId 회원 ID (UUID)
     * @param request API 요청
     * @return UpdateCartItemSelectedCommand
     */
    public UpdateCartItemSelectedCommand toUpdateSelectedCommand(
            UUID memberId, UpdateCartItemSelectedV2ApiRequest request) {
        return new UpdateCartItemSelectedCommand(
                memberId, request.cartItemIds(), request.selected());
    }

    /**
     * 전체 선택/해제 요청을 Command로 변환
     *
     * @param memberId 회원 ID (UUID)
     * @param cartItemIds 전체 아이템 ID 목록
     * @param request API 요청
     * @return UpdateCartItemSelectedCommand
     */
    public UpdateCartItemSelectedCommand toUpdateAllSelectedCommand(
            UUID memberId, List<Long> cartItemIds, UpdateAllSelectedV2ApiRequest request) {
        return new UpdateCartItemSelectedCommand(memberId, cartItemIds, request.selected());
    }

    /**
     * 아이템 삭제 요청을 Command로 변환
     *
     * @param memberId 회원 ID (UUID)
     * @param request API 요청
     * @return RemoveCartItemCommand
     */
    public RemoveCartItemCommand toRemoveItemCommand(
            UUID memberId, RemoveCartItemsV2ApiRequest request) {
        return new RemoveCartItemCommand(memberId, request.cartItemIds());
    }

    /**
     * 장바구니 비우기 Command 생성
     *
     * @param memberId 회원 ID (UUID)
     * @return ClearCartCommand
     */
    public ClearCartCommand toClearCartCommand(UUID memberId) {
        return new ClearCartCommand(memberId);
    }

    /**
     * Application Response를 API Response로 변환
     *
     * @param response Application CartResponse
     * @return CartV2ApiResponse
     */
    public CartV2ApiResponse toApiResponse(CartResponse response) {
        List<CartItemV2ApiResponse> items =
                response.items().stream().map(this::toItemApiResponse).toList();

        return new CartV2ApiResponse(
                response.cartId(),
                response.memberId(),
                items,
                response.totalAmount(),
                response.selectedTotalAmount(),
                response.itemCount(),
                response.selectedItemCount(),
                response.totalQuantity(),
                response.createdAt(),
                response.updatedAt());
    }

    /**
     * Application CartItemResponse를 API Response로 변환
     *
     * @param response Application CartItemResponse
     * @return CartItemV2ApiResponse
     */
    private CartItemV2ApiResponse toItemApiResponse(CartItemResponse response) {
        return new CartItemV2ApiResponse(
                response.cartItemId(),
                response.productStockId(),
                response.productId(),
                response.productGroupId(),
                response.sellerId(),
                response.quantity(),
                response.unitPrice(),
                response.totalPrice(),
                response.selected(),
                response.addedAt());
    }
}
