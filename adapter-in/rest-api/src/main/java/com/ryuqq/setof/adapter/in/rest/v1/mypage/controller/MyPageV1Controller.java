package com.ryuqq.setof.adapter.in.rest.v1.mypage.controller;

import java.util.List;
import jakarta.validation.Valid;
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
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.common.dto.SliceApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.command.AddressBookV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.command.CreateFavoriteV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.command.RefundAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.query.FavoriteFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.response.AddressBookV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.response.FavoriteV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.response.MyPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.response.RefundAccountV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * V1 MyPage Controller (Legacy)
 *
 * <p>
 * 레거시 API 호환을 위한 V1 마이페이지 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "MyPage (Legacy V1)", description = "레거시 마이페이지 API - V2로 마이그레이션 권장")
@RestController
@RequestMapping
@Validated
@Deprecated
public class MyPageV1Controller {

    // =====================================================
    // 배송지 관련 엔드포인트 (Address Book)
    // =====================================================

    @Deprecated
    @Operation(summary = "[Legacy] 배송지 목록 조회", description = "사용자의 배송지 목록을 조회합니다.")
    @GetMapping(ApiPaths.User.AddressBook.LIST)
    public ResponseEntity<ApiResponse<List<AddressBookV1ApiResponse>>> fetchAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal) {

        throw new UnsupportedOperationException("배송지 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배송지 단건 조회", description = "특정 배송지를 조회합니다.")
    @GetMapping(ApiPaths.User.AddressBook.DETAIL)
    public ResponseEntity<ApiResponse<AddressBookV1ApiResponse>> fetchAddressBookDetail(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("shippingAddressId") long shippingAddressId) {

        throw new UnsupportedOperationException("배송지 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배송지 등록", description = "새로운 배송지를 등록합니다.")
    @PostMapping(ApiPaths.User.AddressBook.CREATE)
    public ResponseEntity<ApiResponse<AddressBookV1ApiResponse>> createAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody AddressBookV1ApiRequest request) {

        throw new UnsupportedOperationException("배송지 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배송지 수정", description = "기존 배송지를 수정합니다.")
    @PutMapping(ApiPaths.User.AddressBook.UPDATE)
    public ResponseEntity<ApiResponse<AddressBookV1ApiResponse>> updateAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("shippingAddressId") long shippingAddressId,
            @Valid @RequestBody AddressBookV1ApiRequest request) {

        throw new UnsupportedOperationException("배송지 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배송지 삭제", description = "배송지를 삭제합니다.")
    @DeleteMapping(ApiPaths.User.AddressBook.DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("shippingAddressId") long shippingAddressId) {

        throw new UnsupportedOperationException("배송지 기능은 아직 지원되지 않습니다.");
    }

    // =====================================================
    // 환불계좌 관련 엔드포인트 (Refund Account)
    // =====================================================

    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 조회", description = "사용자의 환불계좌를 조회합니다.")
    @GetMapping(ApiPaths.User.RefundAccount.GET)
    public ResponseEntity<ApiResponse<RefundAccountV1ApiResponse>> fetchRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal) {

        throw new UnsupportedOperationException("환불계좌 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 등록", description = "새로운 환불계좌를 등록합니다.")
    @PostMapping(ApiPaths.User.RefundAccount.CREATE)
    public ResponseEntity<ApiResponse<RefundAccountV1ApiResponse>> createRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody RefundAccountV1ApiRequest request) {

        throw new UnsupportedOperationException("환불계좌 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 수정", description = "기존 환불계좌를 수정합니다.")
    @PutMapping(ApiPaths.User.RefundAccount.UPDATE)
    public ResponseEntity<ApiResponse<RefundAccountV1ApiResponse>> updateRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("refundAccountId") long refundAccountId,
            @Valid @RequestBody RefundAccountV1ApiRequest request) {

        throw new UnsupportedOperationException("환불계좌 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 환불계좌 삭제", description = "환불계좌를 삭제합니다.")
    @DeleteMapping(ApiPaths.User.RefundAccount.DELETE)
    public ResponseEntity<ApiResponse<Void>> deleteRefundAccount(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("refundAccountId") long refundAccountId) {

        throw new UnsupportedOperationException("환불계좌 기능은 아직 지원되지 않습니다.");
    }

    // =====================================================
    // 마이페이지 관련 엔드포인트 (My Page)
    // =====================================================

    @Deprecated
    @Operation(summary = "[Legacy] 마이페이지 조회", description = "마이페이지 정보를 조회합니다.")
    @GetMapping(ApiPaths.User.MyPage.MAIN)
    public ResponseEntity<ApiResponse<MyPageV1ApiResponse>> fetchMyPage(
            @AuthenticationPrincipal MemberPrincipal principal) {

        throw new UnsupportedOperationException("마이페이지 기능은 아직 지원되지 않습니다.");
    }

    // =====================================================
    // 찜(즐겨찾기) 관련 엔드포인트 (Favorites)
    // =====================================================

    @Deprecated
    @Operation(summary = "[Legacy] 찜 목록 조회", description = "사용자의 찜 목록을 조회합니다.")
    @GetMapping(ApiPaths.User.Favorite.LIST)
    public ResponseEntity<ApiResponse<SliceApiResponse<FavoriteV1ApiResponse>>> fetchMyFavorites(
            @AuthenticationPrincipal MemberPrincipal principal,
            @ModelAttribute @Validated FavoriteFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("찜 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 찜 추가", description = "상품을 찜 목록에 추가합니다.")
    @PostMapping(ApiPaths.User.Favorite.CREATE)
    public ResponseEntity<ApiResponse<FavoriteV1ApiResponse>> createUserFavorite(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody CreateFavoriteV1ApiRequest request) {

        throw new UnsupportedOperationException("찜 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 찜 삭제", description = "상품을 찜 목록에서 삭제합니다.")
    @DeleteMapping(ApiPaths.User.Favorite.DELETE)
    public ResponseEntity<ApiResponse<Long>> deleteUserFavorite(
            @AuthenticationPrincipal MemberPrincipal principal, @PathVariable long productGroupId) {

        throw new UnsupportedOperationException("찜 기능은 아직 지원되지 않습니다.");
    }
}
