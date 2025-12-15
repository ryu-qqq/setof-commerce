package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.auth.security.MemberPrincipal;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.commnad.AddressBookV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response.AddressBookV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.mapper.ShippingAddressV1ApiMapper;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.port.in.command.DeleteShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.RegisterShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.command.UpdateShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressUseCase;
import com.ryuqq.setof.application.shippingaddress.port.in.query.GetShippingAddressesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 ShippingAddress (AddressBook) Controller
 *
 * <p>레거시 API 호환을 위한 V1 배송지 엔드포인트 V2 UseCase를 재사용하며, V1 DTO로 변환하여 응답
 *
 * <p>경로: /api/v1/user/address-book (레거시 스펙 유지)
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "AddressBook (Legacy V1)", description = "레거시 배송지 API - V2로 마이그레이션 권장")
@RestController
@RequestMapping(ApiPaths.User.AddressBook.BASE)
@Validated
@Deprecated
public class ShippingAddressV1Controller {

    private final RegisterShippingAddressUseCase registerShippingAddressUseCase;
    private final GetShippingAddressesUseCase getShippingAddressesUseCase;
    private final GetShippingAddressUseCase getShippingAddressUseCase;
    private final UpdateShippingAddressUseCase updateShippingAddressUseCase;
    private final DeleteShippingAddressUseCase deleteShippingAddressUseCase;
    private final ShippingAddressV1ApiMapper shippingAddressV1ApiMapper;

    public ShippingAddressV1Controller(
            RegisterShippingAddressUseCase registerShippingAddressUseCase,
            GetShippingAddressesUseCase getShippingAddressesUseCase,
            GetShippingAddressUseCase getShippingAddressUseCase,
            UpdateShippingAddressUseCase updateShippingAddressUseCase,
            DeleteShippingAddressUseCase deleteShippingAddressUseCase,
            ShippingAddressV1ApiMapper shippingAddressV1ApiMapper) {
        this.registerShippingAddressUseCase = registerShippingAddressUseCase;
        this.getShippingAddressesUseCase = getShippingAddressesUseCase;
        this.getShippingAddressUseCase = getShippingAddressUseCase;
        this.updateShippingAddressUseCase = updateShippingAddressUseCase;
        this.deleteShippingAddressUseCase = deleteShippingAddressUseCase;
        this.shippingAddressV1ApiMapper = shippingAddressV1ApiMapper;
    }

    /**
     * 배송지 목록 조회
     *
     * @param principal 인증된 사용자 정보
     * @return 배송지 목록
     */
    @Deprecated
    @Operation(summary = "[Legacy] 배송지 목록 조회", description = "현재 로그인한 회원의 배송지 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressBookV1ApiResponse>>> getAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        List<ShippingAddressResponse> responses = getShippingAddressesUseCase.execute(memberId);

        List<AddressBookV1ApiResponse> v1Responses =
                shippingAddressV1ApiMapper.toV1Responses(responses);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Responses));
    }

    /**
     * 배송지 단건 조회
     *
     * @param principal 인증된 사용자 정보
     * @param shippingAddressId 배송지 ID
     * @return 배송지 정보
     */
    @Deprecated
    @Operation(summary = "[Legacy] 배송지 단건 조회", description = "배송지 ID로 배송지 정보를 조회합니다.")
    @GetMapping("/{shippingAddressId}")
    public ResponseEntity<ApiResponse<AddressBookV1ApiResponse>> getAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("shippingAddressId") long shippingAddressId) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        ShippingAddressResponse response =
                getShippingAddressUseCase.execute(memberId, shippingAddressId);

        AddressBookV1ApiResponse v1Response = shippingAddressV1ApiMapper.toV1Response(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    /**
     * 배송지 등록
     *
     * @param principal 인증된 사용자 정보
     * @param request 배송지 등록 요청
     * @return 등록된 배송지 정보
     */
    @Deprecated
    @Operation(summary = "[Legacy] 배송지 등록", description = "새로운 배송지를 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<AddressBookV1ApiResponse>> createAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal,
            @Valid @RequestBody AddressBookV1ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        RegisterShippingAddressCommand command =
                shippingAddressV1ApiMapper.toRegisterCommand(memberId, request);

        ShippingAddressResponse response = registerShippingAddressUseCase.execute(command);
        AddressBookV1ApiResponse v1Response = shippingAddressV1ApiMapper.toV1Response(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    /**
     * 배송지 수정
     *
     * @param principal 인증된 사용자 정보
     * @param shippingAddressId 배송지 ID
     * @param request 배송지 수정 요청
     * @return 수정된 배송지 정보
     */
    @Deprecated
    @Operation(summary = "[Legacy] 배송지 수정", description = "배송지 정보를 수정합니다.")
    @PutMapping("/{shippingAddressId}")
    public ResponseEntity<ApiResponse<AddressBookV1ApiResponse>> updateAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("shippingAddressId") long shippingAddressId,
            @Valid @RequestBody AddressBookV1ApiRequest request) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        UpdateShippingAddressCommand command =
                shippingAddressV1ApiMapper.toUpdateCommand(memberId, shippingAddressId, request);

        ShippingAddressResponse response = updateShippingAddressUseCase.execute(command);
        AddressBookV1ApiResponse v1Response = shippingAddressV1ApiMapper.toV1Response(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }

    /**
     * 배송지 삭제
     *
     * <p>레거시 스펙 유지를 위해 DELETE 메서드 사용 (V2에서는 PATCH 사용)
     *
     * @param principal 인증된 사용자 정보
     * @param shippingAddressId 배송지 ID
     * @return 삭제된 배송지 정보
     */
    @Deprecated
    @Operation(summary = "[Legacy] 배송지 삭제", description = "배송지를 삭제합니다.")
    @DeleteMapping("/{shippingAddressId}")
    public ResponseEntity<ApiResponse<AddressBookV1ApiResponse>> deleteAddressBook(
            @AuthenticationPrincipal MemberPrincipal principal,
            @PathVariable("shippingAddressId") long shippingAddressId) {

        UUID memberId = UUID.fromString(principal.getMemberId());
        DeleteShippingAddressCommand command =
                shippingAddressV1ApiMapper.toDeleteCommand(memberId, shippingAddressId);

        deleteShippingAddressUseCase.execute(command);

        // 레거시 스펙: 삭제된 배송지 정보 반환하지 않고 null 반환
        return ResponseEntity.ok(ApiResponse.ofSuccess(null));
    }
}
