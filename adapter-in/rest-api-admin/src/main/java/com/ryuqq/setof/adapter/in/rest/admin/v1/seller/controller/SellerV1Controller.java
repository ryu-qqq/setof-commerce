package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.CreateSellerSettlementAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerApprovalStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerInfoContextV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerUpdateDetailV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.query.BusinessValidationV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.query.SellerFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerContextV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Seller Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Seller 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Seller (Legacy V1)", description = "레거시 Seller API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
public class SellerV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 조회", description = "인증된 셀러를 조회합니다.")
    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<SellerContextV1ApiResponse>> fetchSeller() {

        throw new UnsupportedOperationException("셀러 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 상세 조회", description = "특정 셀러의 상세 정보를 조회합니다.")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<SellerDetailV1ApiResponse>> fetchSeller(
            @PathVariable("sellerId") long sellerId) {

        throw new UnsupportedOperationException("셀러 상세 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 목록 조회", description = "셀러 목록을 조회합니다.")
    @GetMapping("/sellers")
    public ResponseEntity<ApiResponse<PageApiResponse<SellerV1ApiResponse>>> fetchSellers(
            @ModelAttribute @Validated SellerFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("셀러 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 사업자 등록번호 검증", description = "사업자 등록번호를 검증합니다.")
    @GetMapping("/sellers/business-validation")
    public ResponseEntity<ApiResponse<Boolean>> fetchBusinessValidation(
            @ModelAttribute BusinessValidationV1ApiRequest businessValidation) {

        throw new UnsupportedOperationException("사업자 등록번호 검증 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 등록", description = "새로운 셀러를 등록합니다.")
    @PostMapping("/seller")
    public ResponseEntity<ApiResponse<Long>> insertSeller(
            @RequestBody SellerInfoContextV1ApiRequest sellerInfoContextRequestDto) {

        throw new UnsupportedOperationException("셀러 등록 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 정산 계좌 검증", description = "셀러 정산 계좌를 검증합니다.")
    @PostMapping("/seller-account")
    public ResponseEntity<ApiResponse<Long>> checkSellerSettlementAccount(
            @RequestBody CreateSellerSettlementAccountV1ApiRequest createSellerSettlementAccount) {

        throw new UnsupportedOperationException("셀러 정산 계좌 검증 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 수정", description = "셀러 정보를 수정합니다.")
    @PutMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse<Long>> updateSeller(
            @PathVariable long sellerId,
            @RequestBody SellerUpdateDetailV1ApiRequest sellerUpdateDetailRequestDto) {

        throw new UnsupportedOperationException("셀러 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 승인 상태 수정", description = "셀러의 승인 상태를 수정합니다.")
    @PutMapping("/seller/approval-status")
    public ResponseEntity<ApiResponse<List<Long>>> updateApprovalStatus(
            @RequestBody SellerApprovalStatusV1ApiRequest sellerApprovalStatusRequestDto) {

        throw new UnsupportedOperationException("셀러 승인 상태 수정 기능은 아직 지원되지 않습니다.");
    }
}
