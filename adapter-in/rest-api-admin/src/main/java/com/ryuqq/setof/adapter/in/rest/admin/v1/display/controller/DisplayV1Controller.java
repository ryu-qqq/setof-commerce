package com.ryuqq.setof.adapter.in.rest.admin.v1.display.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.CreateBannerItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.CreateBannerV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.CreateContentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.UpdateBannerDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.UpdateContentDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.UpdateGnbV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query.BannerFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query.BannerItemFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query.ContentFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query.GnbFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.BannerGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.BannerItemV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.BannerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.ContentGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.GnbV1ApiResponse;

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
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Display Controller (Legacy)
 *
 * <p>
 * 레거시 API 호환을 위한 V1 Display 엔드포인트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Display (Legacy V1)", description = "레거시 Display API - V2로 마이그레이션 권장")
@RestController
@RequestMapping("/api/v1")
@Validated
@Deprecated
public class DisplayV1Controller {

    // ========================================
    // Content 엔드포인트
    // ========================================

    @Deprecated
    @Operation(summary = "[Legacy] 컨텐츠 조회", description = "특정 컨텐츠를 조회합니다.")
    @GetMapping("/content/{contentId}")
    public ResponseEntity<ApiResponse<ContentGroupV1ApiResponse>> getContent(
            @PathVariable("contentId") long contentId) {

        throw new UnsupportedOperationException("컨텐츠 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 컨텐츠 목록 조회", description = "컨텐츠 목록을 조회합니다.")
    @GetMapping("/contents")
    public ResponseEntity<ApiResponse<PageApiResponse<ContentV1ApiResponse>>> getContents(
            @ModelAttribute ContentFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("컨텐츠 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 컨텐츠 등록", description = "새로운 컨텐츠를 등록합니다.")
    @PostMapping("/content")
    public ResponseEntity<ApiResponse<ContentV1ApiResponse>> enrollContent(
            @Valid @RequestBody CreateContentV1ApiRequest request) {

        throw new UnsupportedOperationException("컨텐츠 등록 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 컨텐츠 표시 상태 변경", description = "컨텐츠의 표시 상태를 변경합니다.")
    @PatchMapping("/content/{contentId}/display-status")
    public ResponseEntity<ApiResponse<ContentV1ApiResponse>> updateContentDisplayYn(
            @PathVariable("contentId") long contentId,
            @Valid @RequestBody UpdateContentDisplayYnV1ApiRequest request) {

        throw new UnsupportedOperationException("컨텐츠 표시 상태 변경 기능은 아직 지원되지 않습니다.");
    }

    // ========================================
    // Banner 엔드포인트
    // ========================================

    @Deprecated
    @Operation(summary = "[Legacy] 배너 그룹 조회", description = "특정 배너의 배너 그룹을 조회합니다.")
    @GetMapping("/content/banner/{bannerId}")
    public ResponseEntity<ApiResponse<List<BannerItemV1ApiResponse>>> getBannerGroups(
            @PathVariable("bannerId") long bannerId,
            @ModelAttribute BannerItemFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("배너 그룹 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배너 목록 조회", description = "배너 목록을 조회합니다.")
    @GetMapping("/content/banners")
    public ResponseEntity<ApiResponse<PageApiResponse<BannerGroupV1ApiResponse>>> getBannerGroups(
            @ModelAttribute BannerFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("배너 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배너 등록", description = "새로운 배너를 등록합니다.")
    @PostMapping("/content/banner")
    public ResponseEntity<ApiResponse<BannerV1ApiResponse>> enrollBanner(
            @Valid @RequestBody CreateBannerV1ApiRequest request) {

        throw new UnsupportedOperationException("배너 등록 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배너 수정", description = "기존 배너를 수정합니다.")
    @PutMapping("/content/banner/{bannerId}")
    public ResponseEntity<ApiResponse<BannerV1ApiResponse>> updateBanner(
            @PathVariable("bannerId") long bannerId,
            @Valid @RequestBody CreateBannerV1ApiRequest request) {

        throw new UnsupportedOperationException("배너 수정 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배너 아이템 등록", description = "배너 아이템들을 등록합니다.")
    @PostMapping("/content/banner/items")
    public ResponseEntity<ApiResponse<List<CreateBannerItemV1ApiRequest>>> enrollBannerItems(
            @Valid @RequestBody List<CreateBannerItemV1ApiRequest> request) {

        throw new UnsupportedOperationException("배너 아이템 등록 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] 배너 표시 상태 변경", description = "배너의 표시 상태를 변경합니다.")
    @PatchMapping("/content/banner/{bannerId}/display-status")
    public ResponseEntity<ApiResponse<BannerV1ApiResponse>> updateBannerDisplayYn(
            @PathVariable("bannerId") long bannerId,
            @Valid @RequestBody UpdateBannerDisplayYnV1ApiRequest request) {

        throw new UnsupportedOperationException("배너 표시 상태 변경 기능은 아직 지원되지 않습니다.");
    }

    // ========================================
    // GNB 엔드포인트
    // ========================================

    @Deprecated
    @Operation(summary = "[Legacy] GNB 목록 조회", description = "GNB 목록을 조회합니다.")
    @GetMapping("/content/gnbs")
    public ResponseEntity<ApiResponse<List<GnbV1ApiResponse>>> getGnbs(
            @ModelAttribute GnbFilterV1ApiRequest filter) {

        throw new UnsupportedOperationException("GNB 목록 조회 기능은 아직 지원되지 않습니다.");
    }

    @Deprecated
    @Operation(summary = "[Legacy] GNB 등록", description = "GNB를 등록합니다.")
    @PostMapping("/content/gnbs")
    public ResponseEntity<ApiResponse<List<GnbV1ApiResponse>>> enrollGnbs(
            @Valid @RequestBody UpdateGnbV1ApiRequest request) {

        throw new UnsupportedOperationException("GNB 등록 기능은 아직 지원되지 않습니다.");
    }
}
