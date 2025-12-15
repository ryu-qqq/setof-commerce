package com.setof.connectly.module.cart.controller;

import com.setof.connectly.module.cart.dto.CartCountDto;
import com.setof.connectly.module.cart.dto.CartDeleteRequestDto;
import com.setof.connectly.module.cart.dto.CartFilter;
import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.cart.entity.embedded.CartDetails;
import com.setof.connectly.module.cart.service.fetch.CartFindService;
import com.setof.connectly.module.cart.service.query.CartQueryService;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final CartFindService cartFindService;
    private final CartQueryService cartQueryService;

    @GetMapping("/cart-count")
    public ResponseEntity<ApiResponse<CartCountDto>> fetchCartCount() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        cartFindService.fetchCartCountQuery(SecurityUtils.currentUserId())));
    }

    @GetMapping("/carts")
    public ResponseEntity<ApiResponse<CustomSlice<CartResponse>>> getCarts(
            @ModelAttribute CartFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(cartFindService.fetchCartList(filter, pageable)));
    }

    @PostMapping("/cart")
    public ResponseEntity<ApiResponse<List<CartDetails>>> addToCart(
            @RequestBody @Validated List<CartDetails> cartDetails) {
        return ResponseEntity.ok(ApiResponse.success(cartQueryService.insertOrUpdate(cartDetails)));
    }

    @PutMapping("/cart/{cartId}")
    public ResponseEntity<ApiResponse<CartDetails>> modifyCart(
            @PathVariable("cartId") long cartId, @RequestBody @Validated CartDetails cartDetails) {
        return ResponseEntity.ok(
                ApiResponse.success(cartQueryService.updateCart(cartId, cartDetails)));
    }

    @DeleteMapping("/carts")
    public ResponseEntity<ApiResponse<Integer>> deleteCarts(
            @ModelAttribute CartDeleteRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(cartQueryService.delete(requestDto)));
    }
}
