package com.ryuqq.setof.adapter.in.rest.v1.wishlist.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.WishlistV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.request.AddWishlistItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.mapper.WishlistV1ApiMapper;
import com.ryuqq.setof.application.wishlist.port.in.command.AddWishlistItemUseCase;
import com.ryuqq.setof.application.wishlist.port.in.command.DeleteWishlistItemUseCase;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WishlistCommandV1Controller - 찜 생성/삭제 V1 Public API.
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
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "찜 명령 V1", description = "찜 생성/삭제 V1 Public API (인증 필요)")
@RestController
@RequestMapping(WishlistV1Endpoints.MY_FAVORITE)
public class WishlistCommandV1Controller {

    private final AddWishlistItemUseCase addWishlistItemUseCase;
    private final DeleteWishlistItemUseCase deleteWishlistItemUseCase;
    private final WishlistV1ApiMapper mapper;

    public WishlistCommandV1Controller(
            AddWishlistItemUseCase addWishlistItemUseCase,
            DeleteWishlistItemUseCase deleteWishlistItemUseCase,
            WishlistV1ApiMapper mapper) {
        this.addWishlistItemUseCase = addWishlistItemUseCase;
        this.deleteWishlistItemUseCase = deleteWishlistItemUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "찜 추가", description = "인증된 사용자가 상품 그룹을 찜합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "찜 추가 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping
    public ResponseEntity<V1ApiResponse<Long>> addFavorite(
            @AuthenticatedUserId Long userId,
            @Valid @RequestBody AddWishlistItemV1ApiRequest request) {
        Long wishlistItemId = addWishlistItemUseCase.execute(mapper.toAddCommand(userId, request));
        return ResponseEntity.ok(V1ApiResponse.success(wishlistItemId));
    }

    @Operation(summary = "찜 삭제", description = "인증된 사용자가 특정 상품 그룹의 찜을 삭제합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "찜 삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping(WishlistV1Endpoints.PRODUCT_GROUP_ID)
    public ResponseEntity<V1ApiResponse<Long>> deleteFavorite(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "삭제할 상품 그룹 ID", required = true)
                    @PathVariable(WishlistV1Endpoints.PATH_PRODUCT_GROUP_ID)
                    long productGroupId) {
        deleteWishlistItemUseCase.execute(mapper.toDeleteCommand(userId, productGroupId));
        return ResponseEntity.ok(V1ApiResponse.success(productGroupId));
    }
}
