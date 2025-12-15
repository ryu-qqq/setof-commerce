package com.setof.connectly.module.user.controller;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.user.dto.TokenDto;
import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.dto.account.CreateRefundAccount;
import com.setof.connectly.module.user.dto.account.RefundAccountInfo;
import com.setof.connectly.module.user.dto.account.WithdrawalDto;
import com.setof.connectly.module.user.dto.favorite.*;
import com.setof.connectly.module.user.dto.join.CreateUser;
import com.setof.connectly.module.user.dto.join.IsJoinedUser;
import com.setof.connectly.module.user.dto.join.JoinedUser;
import com.setof.connectly.module.user.dto.join.LoginUser;
import com.setof.connectly.module.user.dto.mypage.MyPageResponse;
import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.service.account.RefundAccountQueryService;
import com.setof.connectly.module.user.service.account.fetch.RefundAccountFindService;
import com.setof.connectly.module.user.service.favorite.fetch.UserFavoriteFindService;
import com.setof.connectly.module.user.service.favorite.query.UserFavoriteQueryService;
import com.setof.connectly.module.user.service.fetch.UserFindService;
import com.setof.connectly.module.user.service.manage.UserManageService;
import com.setof.connectly.module.user.service.mypage.MyPageFindService;
import com.setof.connectly.module.user.service.shipping.fetch.ShippingAddressFindService;
import com.setof.connectly.module.user.service.shipping.query.ShippingAddressQueryService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final ShippingAddressFindService shippingAddressFindService;
    private final ShippingAddressQueryService shippingAddressQueryService;
    private final RefundAccountFindService refundAccountFindService;
    private final RefundAccountQueryService refundAccountQueryService;
    private final MyPageFindService myPageFindService;
    private final UserManageService userManageService;
    private final UserFindService userFindService;
    private final UserFavoriteFindService userFavoriteFindService;
    private final UserFavoriteQueryService userFavoriteQueryService;

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("")
    public ResponseEntity<ApiResponse<JoinedUser>> fetchUser() {
        return ResponseEntity.ok(ApiResponse.success(userFindService.fetchJoinedUser()));
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<JoinedUser>> isExistUser(
            @ModelAttribute @Validated IsJoinedUser isJoinedUser) {
        return ResponseEntity.ok(
                ApiResponse.success(userFindService.fetchJoinedUser(isJoinedUser)));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<TokenDto>> join(
            @RequestBody @Validated CreateUser createUser, HttpServletResponse response) {
        return ResponseEntity.ok(
                ApiResponse.success(userManageService.joinUser(createUser, response)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(
            @RequestBody @Validated LoginUser loginUser, HttpServletResponse response)
            throws IOException {
        userManageService.loginUser(loginUser, response);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping("/withdrawl")
    public ResponseEntity<ApiResponse<Users>> withdrawal(
            @RequestBody @Validated WithdrawalDto withdrawal, HttpServletResponse response) {
        return ResponseEntity.ok(
                ApiResponse.success(userManageService.withdrawal(withdrawal, response)));
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody @Validated LoginUser loginUse) {
        return ResponseEntity.ok(ApiResponse.success(userManageService.resetPassword(loginUse)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/address-book")
    public ResponseEntity<ApiResponse<List<UserShippingInfo>>> fetchAddressBook() {
        return ResponseEntity.ok(
                ApiResponse.success(shippingAddressFindService.fetchShippingInfo()));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/address-book/{shippingAddressId}")
    public ResponseEntity<ApiResponse<UserShippingInfo>> fetchAddressBook(
            @PathVariable("shippingAddressId") long shippingAddressId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        shippingAddressFindService.fetchShippingInfo(shippingAddressId)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping("/address-book")
    public ResponseEntity<ApiResponse<UserShippingInfo>> createAddressBook(
            @RequestBody UserShippingInfo userShippingInfo) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        shippingAddressQueryService.toCreateUserShippingInfo(userShippingInfo)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping("/address-book/{shippingAddressId}")
    public ResponseEntity<ApiResponse<UserShippingInfo>> updateAddressBook(
            @PathVariable("shippingAddressId") long shippingAddressId,
            @RequestBody UserShippingInfo userShippingInfo) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        shippingAddressQueryService.updateUserShippingInfo(
                                shippingAddressId, userShippingInfo)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping("/address-book/{shippingAddressId}")
    public ResponseEntity<ApiResponse<ShippingAddress>> deleteAddressBook(
            @PathVariable("shippingAddressId") long shippingAddressId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        shippingAddressQueryService.deleteUserShippingInfo(shippingAddressId)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/refund-account")
    public ResponseEntity<ApiResponse<RefundAccountInfo>> fetchRefundAccount() {
        return ResponseEntity.ok(
                ApiResponse.success(refundAccountFindService.fetchRefundAccountInfo()));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping("/refund-account")
    public ResponseEntity<ApiResponse<RefundAccountInfo>> createRefundAccount(
            @RequestBody @Validated CreateRefundAccount createRefundAccount) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        refundAccountQueryService.saveRefundAccount(createRefundAccount)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping("/refund-account/{refundAccountId}")
    public ResponseEntity<ApiResponse<RefundAccountInfo>> updateRefundAccount(
            @PathVariable("refundAccountId") long refundAccountId,
            @RequestBody @Validated CreateRefundAccount createRefundAccount) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        refundAccountQueryService.updateRefundAccount(
                                createRefundAccount, refundAccountId)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping("/refund-account/{refundAccountId}")
    public ResponseEntity<ApiResponse<RefundAccountInfo>> deleteRefundAccount(
            @PathVariable("refundAccountId") long refundAccountId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        refundAccountQueryService.deleteRefundAccount(refundAccountId)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/my-page")
    public ResponseEntity<ApiResponse<MyPageResponse>> fetchMyPage() {
        return ResponseEntity.ok(ApiResponse.success(myPageFindService.fetchMyPage()));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/my-favorites")
    public ResponseEntity<ApiResponse<CustomSlice<UserFavoriteThumbnail>>> fetchMyFavorites(
            @ModelAttribute @Validated MyFavoriteFilter filter, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(userFavoriteFindService.fetchUserFavorites(filter, pageable)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping("/my-favorite")
    public ResponseEntity<ApiResponse<UserFavoriteResponse>> createUserFavorite(
            @RequestBody @Validated CreateUserFavorite createUserFavorite) {
        return ResponseEntity.ok(
                ApiResponse.success(userFavoriteQueryService.createFavorite(createUserFavorite)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @DeleteMapping("/my-favorite/{productGroupId}")
    public ResponseEntity<ApiResponse<Long>> deleteUserFavorite(@PathVariable long productGroupId) {
        return ResponseEntity.ok(
                ApiResponse.success(userFavoriteQueryService.deleteFavorite(productGroupId)));
    }
}
