package com.ryuqq.setof.adapter.in.rest.v2.cart.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.AddCartItemV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.RemoveCartItemsV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateAllSelectedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateCartItemQuantityV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.command.UpdateCartItemSelectedV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.cart.dto.response.CartV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.cart.mapper.CartV2ApiMapper;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.ClearCartCommand;
import com.ryuqq.setof.application.cart.dto.command.RemoveCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemQuantityCommand;
import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemSelectedCommand;
import com.ryuqq.setof.application.cart.dto.response.CartResponse;
import com.ryuqq.setof.application.cart.port.in.command.AddCartItemUseCase;
import com.ryuqq.setof.application.cart.port.in.command.ClearCartUseCase;
import com.ryuqq.setof.application.cart.port.in.command.RemoveCartItemUseCase;
import com.ryuqq.setof.application.cart.port.in.command.UpdateCartItemQuantityUseCase;
import com.ryuqq.setof.application.cart.port.in.command.UpdateCartItemSelectedUseCase;
import com.ryuqq.setof.application.cart.port.in.query.GetCartUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cart V2 Controller
 *
 * <p>장바구니 API 엔드포인트
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>장바구니는 회원당 1개
 *   <li>최대 100개 아이템
 *   <li>아이템당 최대 수량 99개
 *   <li>동일 상품 추가 시 수량 합산
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Cart", description = "장바구니 API")
@RestController
@RequestMapping(ApiV2Paths.Carts.BASE)
@Validated
public class CartV2Controller {

    private final GetCartUseCase getCartUseCase;
    private final AddCartItemUseCase addCartItemUseCase;
    private final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    private final UpdateCartItemSelectedUseCase updateCartItemSelectedUseCase;
    private final RemoveCartItemUseCase removeCartItemUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final CartV2ApiMapper mapper;

    public CartV2Controller(
            GetCartUseCase getCartUseCase,
            AddCartItemUseCase addCartItemUseCase,
            UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase,
            UpdateCartItemSelectedUseCase updateCartItemSelectedUseCase,
            RemoveCartItemUseCase removeCartItemUseCase,
            ClearCartUseCase clearCartUseCase,
            CartV2ApiMapper mapper) {
        this.getCartUseCase = getCartUseCase;
        this.addCartItemUseCase = addCartItemUseCase;
        this.updateCartItemQuantityUseCase = updateCartItemQuantityUseCase;
        this.updateCartItemSelectedUseCase = updateCartItemSelectedUseCase;
        this.removeCartItemUseCase = removeCartItemUseCase;
        this.clearCartUseCase = clearCartUseCase;
        this.mapper = mapper;
    }

    /**
     * 장바구니 조회
     *
     * <p>회원의 장바구니를 조회합니다.
     *
     * @param principal 인증된 사용자 정보
     * @return 장바구니 정보
     */
    @Operation(summary = "장바구니 조회", description = "회원의 장바구니를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<CartV2ApiResponse>> getCart(
            @AuthenticationPrincipal MemberPrincipal principal) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        CartResponse response = getCartUseCase.getCart(memberId);
        CartV2ApiResponse apiResponse = mapper.toApiResponse(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 장바구니 아이템 개수 조회
     *
     * <p>장바구니에 담긴 아이템 개수를 조회합니다. (헤더 뱃지용)
     *
     * @param principal 인증된 사용자 정보
     * @return 아이템 개수
     */
    @Operation(summary = "장바구니 아이템 개수 조회", description = "장바구니에 담긴 아이템 개수를 조회합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping(ApiV2Paths.Carts.COUNT_PATH)
    public ResponseEntity<ApiResponse<Integer>> getItemCount(
            @AuthenticationPrincipal MemberPrincipal principal) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        int count = getCartUseCase.getItemCount(memberId);

        return ResponseEntity.ok(ApiResponse.ofSuccess(count));
    }

    /**
     * 장바구니에 아이템 추가
     *
     * <p>장바구니에 상품을 추가합니다. 동일 상품이 있으면 수량이 합산됩니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 추가할 아이템 정보
     * @return 업데이트된 장바구니
     */
    @Operation(summary = "장바구니에 아이템 추가", description = "장바구니에 상품을 추가합니다. 동일 상품이 있으면 수량이 합산됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "추가 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청 (수량 초과 등)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PostMapping(ApiV2Paths.Carts.ITEMS_PATH)
    public ResponseEntity<ApiResponse<CartV2ApiResponse>> addItem(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody AddCartItemV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        AddCartItemCommand command = mapper.toAddItemCommand(memberId, request);
        CartResponse response = addCartItemUseCase.addItem(command);
        CartV2ApiResponse apiResponse = mapper.toApiResponse(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 아이템 수량 변경
     *
     * <p>장바구니 아이템의 수량을 변경합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param cartItemId 장바구니 아이템 ID
     * @param request 변경할 수량
     * @return 업데이트된 장바구니
     */
    @Operation(summary = "아이템 수량 변경", description = "장바구니 아이템의 수량을 변경합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "변경 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청 (수량 범위 초과)",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "아이템을 찾을 수 없음",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Carts.ITEM_QUANTITY_PATH)
    public ResponseEntity<ApiResponse<CartV2ApiResponse>> updateQuantity(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Parameter(description = "장바구니 아이템 ID", example = "1") @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemQuantityV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        UpdateCartItemQuantityCommand command =
                mapper.toUpdateQuantityCommand(memberId, cartItemId, request);
        CartResponse response = updateCartItemQuantityUseCase.updateQuantity(command);
        CartV2ApiResponse apiResponse = mapper.toApiResponse(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 아이템 선택 상태 변경
     *
     * <p>장바구니 아이템의 선택 상태를 변경합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 변경할 아이템 목록과 선택 상태
     * @return 업데이트된 장바구니
     */
    @Operation(summary = "아이템 선택 상태 변경", description = "장바구니 아이템의 선택 상태를 변경합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "변경 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Carts.ITEMS_SELECTED_PATH)
    public ResponseEntity<ApiResponse<CartV2ApiResponse>> updateSelected(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody UpdateCartItemSelectedV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        UpdateCartItemSelectedCommand command = mapper.toUpdateSelectedCommand(memberId, request);
        CartResponse response = updateCartItemSelectedUseCase.updateSelected(command);
        CartV2ApiResponse apiResponse = mapper.toApiResponse(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 전체 아이템 선택/해제
     *
     * <p>장바구니의 모든 아이템을 선택하거나 해제합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 선택 상태
     * @return 업데이트된 장바구니
     */
    @Operation(summary = "전체 아이템 선택/해제", description = "장바구니의 모든 아이템을 선택하거나 해제합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "변경 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PutMapping(ApiV2Paths.Carts.ALL_SELECTED_PATH)
    public ResponseEntity<ApiResponse<CartV2ApiResponse>> updateAllSelected(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody UpdateAllSelectedV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());

        // 먼저 현재 장바구니 조회하여 모든 아이템 ID 획득
        CartResponse currentCart = getCartUseCase.getCart(memberId);
        List<Long> allItemIds =
                currentCart.items().stream().map(item -> item.cartItemId()).toList();

        if (allItemIds.isEmpty()) {
            // 아이템이 없으면 현재 장바구니 그대로 반환
            return ResponseEntity.ok(ApiResponse.ofSuccess(mapper.toApiResponse(currentCart)));
        }

        UpdateCartItemSelectedCommand command =
                mapper.toUpdateAllSelectedCommand(memberId, allItemIds, request);
        CartResponse response = updateCartItemSelectedUseCase.updateSelected(command);
        CartV2ApiResponse apiResponse = mapper.toApiResponse(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 아이템 삭제
     *
     * <p>장바구니에서 아이템을 삭제합니다.
     *
     * @param principal 인증된 사용자 정보
     * @param request 삭제할 아이템 ID 목록
     * @return 업데이트된 장바구니
     */
    @Operation(summary = "아이템 삭제", description = "장바구니에서 아이템을 삭제합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "잘못된 요청",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Carts.ITEMS_PATH + "/remove")
    public ResponseEntity<ApiResponse<CartV2ApiResponse>> removeItems(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody RemoveCartItemsV2ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        RemoveCartItemCommand command = mapper.toRemoveItemCommand(memberId, request);
        CartResponse response = removeCartItemUseCase.removeItems(command);
        CartV2ApiResponse apiResponse = mapper.toApiResponse(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }

    /**
     * 장바구니 비우기
     *
     * <p>장바구니의 모든 아이템을 삭제합니다.
     *
     * @param principal 인증된 사용자 정보
     * @return 빈 장바구니
     */
    @Operation(summary = "장바구니 비우기", description = "장바구니의 모든 아이템을 삭제합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "비우기 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "인증 필요",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @PatchMapping(ApiV2Paths.Carts.CLEAR_PATH)
    public ResponseEntity<ApiResponse<CartV2ApiResponse>> clearCart(
            @AuthenticationPrincipal MemberPrincipal principal) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        ClearCartCommand command = mapper.toClearCartCommand(memberId);
        CartResponse response = clearCartUseCase.clear(command);
        CartV2ApiResponse apiResponse = mapper.toApiResponse(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(apiResponse));
    }
}
