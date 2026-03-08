package com.ryuqq.setof.adapter.in.rest.v1.wishlist.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.WishlistV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response.WishlistItemSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.mapper.WishlistV1ApiMapper;
import com.ryuqq.setof.application.wishlist.dto.query.WishlistSearchParams;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import com.ryuqq.setof.application.wishlist.port.in.query.GetWishlistItemsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WishlistQueryV1Controller - 찜 목록 조회 V1 Public API.
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
 * <p>레거시 호환: GET /api/v1/user/my-favorites?lastDomainId={id}&size={size}
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(name = "찜 조회 V1", description = "찜 목록 조회 V1 Public API (인증 필요)")
@RestController
@RequestMapping(WishlistV1Endpoints.MY_FAVORITES)
public class WishlistQueryV1Controller {

    private final GetWishlistItemsUseCase getWishlistItemsUseCase;
    private final WishlistV1ApiMapper mapper;

    public WishlistQueryV1Controller(
            GetWishlistItemsUseCase getWishlistItemsUseCase, WishlistV1ApiMapper mapper) {
        this.getWishlistItemsUseCase = getWishlistItemsUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "찜 목록 조회", description = "인증된 사용자의 찜 목록을 커서 기반 페이지네이션으로 조회합니다.")
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
    public ResponseEntity<V1ApiResponse<WishlistItemSliceV1ApiResponse>> getMyFavorites(
            @AuthenticatedUserId Long userId,
            @Parameter(description = "마지막 조회된 찜 ID (커서)") @RequestParam(required = false)
                    Long lastDomainId,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20")
                    int size) {

        WishlistSearchParams params = mapper.toSearchParams(userId, lastDomainId, size);
        WishlistItemSliceResult sliceResult = getWishlistItemsUseCase.execute(params);

        WishlistItemSliceV1ApiResponse response = mapper.toSliceResponse(sliceResult, size);
        return ResponseEntity.ok(V1ApiResponse.success(response));
    }
}
