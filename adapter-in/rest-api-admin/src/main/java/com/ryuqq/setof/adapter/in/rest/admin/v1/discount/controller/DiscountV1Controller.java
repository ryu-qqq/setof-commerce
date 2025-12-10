package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.controller;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountTargetV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateUseDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.query.DiscountFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountTargetV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountUseHistoryV1ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Discount Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Discount 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Discount (Legacy V1)", description = "레거시 Discount API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
public class DiscountV1Controller {

    @Deprecated
    @Operation(summary = "[Legacy] 할인 정책 조회", description = "특정 할인 정책을 조회합니다.")
    @GetMapping("/discount/{discountPolicyId}")
    public ResponseEntity<ApiResponse<DiscountPolicyV1ApiResponse>> fetchDiscountPolicy(
            @PathVariable("discountPolicyId") long discountPolicyId) {

        throw new UnsupportedOperationException("할인 정책 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 정책 목록 조회", description = "할인 정책 목록을 조회합니다.")
    @GetMapping("/discounts")
    public ResponseEntity<ApiResponse<PageApiResponse<DiscountPolicyV1ApiResponse>>>
            getDiscountPolies(@ModelAttribute @Validated DiscountFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("할인 정책 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 정책 생성", description = "새로운 할인 정책을 생성합니다.")
    @PostMapping("/discount")
    public ResponseEntity<ApiResponse<DiscountPolicyV1ApiResponse>> createDiscountPolicy(
            @Valid @RequestBody CreateDiscountV1ApiRequest createDiscount) {

        throw new UnsupportedOperationException("할인 정책 생성 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 정책 수정", description = "할인 정책을 수정합니다.")
    @PutMapping("/discount/{discountPolicyId}")
    public ResponseEntity<ApiResponse<DiscountPolicyV1ApiResponse>> updateDiscountPolicy(
            @PathVariable("discountPolicyId") long discountPolicyId,
            @Valid @RequestBody UpdateDiscountV1ApiRequest updateDiscount) {

        throw new UnsupportedOperationException("할인 정책 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 정책 사용 상태 수정", description = "할인 정책의 사용 상태를 수정합니다.")
    @PatchMapping("/discounts")
    public ResponseEntity<ApiResponse<List<DiscountPolicyV1ApiResponse>>>
            updateDiscountPolicyUsageStatus(
                    @RequestBody UpdateUseDiscountV1ApiRequest updateUseDiscount) {

        throw new UnsupportedOperationException("할인 정책 사용 상태 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 엑셀 업로드로 할인 정책 생성", description = "엑셀 파일을 업로드하여 할인 정책을 생성합니다.")
    @PostMapping("/discounts/excel")
    public ResponseEntity<ApiResponse<List<DiscountPolicyV1ApiResponse>>>
            createDiscountsFromExcelUpload(
                    @Valid @RequestBody List<CreateDiscountV1ApiRequest> createDiscountFromExcels) {

        throw new UnsupportedOperationException("엑셀 업로드 할인 정책 생성 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 대상 목록 조회", description = "할인 정책의 대상 목록을 조회합니다.")
    @GetMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<PageApiResponse<? extends DiscountTargetV1ApiResponse>>>
            getDiscountTargets(
                    @PathVariable("discountPolicyId") long discountPolicyId,
                    @RequestParam String issueType) {

        throw new UnsupportedOperationException("할인 대상 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 대상 생성", description = "할인 정책의 대상을 생성합니다.")
    @PostMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<List<DiscountTargetV1ApiResponse>>> createDiscountTargets(
            @PathVariable("discountPolicyId") long discountPolicyId,
            @Valid @RequestBody CreateDiscountTargetV1ApiRequest createDiscountTarget) {

        throw new UnsupportedOperationException("할인 대상 생성 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 대상 수정", description = "할인 정책의 대상을 수정합니다.")
    @PutMapping("/discount/{discountPolicyId}/targets")
    public ResponseEntity<ApiResponse<List<DiscountTargetV1ApiResponse>>> updateDiscountTargets(
            @PathVariable("discountPolicyId") long discountPolicyId,
            @Valid @RequestBody CreateDiscountTargetV1ApiRequest createDiscountTarget) {

        throw new UnsupportedOperationException("할인 대상 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 정책 히스토리 조회", description = "할인 정책 히스토리를 조회합니다.")
    @GetMapping("/discounts/history")
    public ResponseEntity<ApiResponse<PageApiResponse<DiscountPolicyV1ApiResponse>>>
            getDiscountHistories(
                    @ModelAttribute @Validated DiscountFilterV1ApiRequest discountFilter) {

        throw new UnsupportedOperationException("할인 정책 히스토리 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 할인 사용 히스토리 조회", description = "특정 할인 정책의 사용 히스토리를 조회합니다.")
    @GetMapping("/discount/history/{discountPolicyId}/use")
    public ResponseEntity<ApiResponse<PageApiResponse<DiscountUseHistoryV1ApiResponse>>>
            getDiscountUseHistories(
                    @PathVariable("discountPolicyId") long discountPolicyId,
                    @ModelAttribute DiscountFilterV1ApiRequest discountFilterDto) {

        throw new UnsupportedOperationException("할인 사용 히스토리 조회 기능은 아직 지원되지 않습니다.");
    }
}
