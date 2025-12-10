package com.ryuqq.setof.adapter.in.rest.v1.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.SliceApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.command.CreateCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.query.CartV1SearchApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.command.DeleteCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartCreateV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartUpdateV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.DeleteCartV1ApiResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart (Legacy V1)", description = "레거시 Cart API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping
public class CartV1Controller {


    @Deprecated
    @Operation(summary = "[Legacy] 내 장바구니 카운트", description = "내가 장바구니를 삭제합니다.")
    @GetMapping(ApiPaths.Cart.COUNT)
    public ResponseEntity<ApiResponse<CartCountV1ApiResponse>> getCartCount(
        @AuthenticationPrincipal MemberPrincipal principal
    ){
        throw new UnsupportedOperationException("카트 카운트 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 내 장바구니 조회", description = "내가 장바구니 목록을 조회합니다.")
    @GetMapping(ApiPaths.Cart.LIST)
    public ResponseEntity<ApiResponse<SliceApiResponse<CartSearchV1ApiResponse>>> getCarts(
        @AuthenticationPrincipal MemberPrincipal principal,
        @ModelAttribute CartV1SearchApiRequest request){
        throw new UnsupportedOperationException("카트 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 내 장바구니 등록", description = "내가 장바구니를 등록합니다.")
    @PostMapping(ApiPaths.Cart.CREATE)
    public ResponseEntity<ApiResponse<CartCreateV1ApiResponse>> addToCart(
        @AuthenticationPrincipal MemberPrincipal principal,
        @RequestBody @Validated List<CreateCartV1ApiRequest> requests
    ){
        throw new UnsupportedOperationException("카트 등록 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 내 장바구니 수정", description = "내가 장바구니를 수정합니다.")
    @PutMapping(ApiPaths.Cart.UPDATE)
    public ResponseEntity<ApiResponse<CartUpdateV1ApiResponse>> modifyCart(
        @AuthenticationPrincipal MemberPrincipal principal,
        @PathVariable("cartId") long cartId,
        @RequestBody @Validated CreateCartV1ApiRequest request){
        throw new UnsupportedOperationException("카트 업데이트 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 내 장바구니 삭제", description = "내가 장바구니를 삭제합니다.")
    @DeleteMapping(ApiPaths.Cart.DELETE)
    public ResponseEntity<ApiResponse<DeleteCartV1ApiResponse>> deleteCarts(@ModelAttribute DeleteCartV1ApiRequest request){
        throw new UnsupportedOperationException("카트 삭제 기능은 아직 지원되지 않습니다.");
    }

}
