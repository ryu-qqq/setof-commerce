package com.ryuqq.setof.adapter.in.rest.admin.v1.display.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.CreateBannerItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.CreateBannerV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.CreateContentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command.UpdateGnbV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query.BannerFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query.ContentFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.BannerGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.BannerItemV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.BannerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.ContentGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.response.GnbV1ApiResponse;
import com.ryuqq.setof.application.banner.dto.command.CreateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerCommand;
import com.ryuqq.setof.application.banner.dto.query.SearchBannerQuery;
import com.ryuqq.setof.application.banner.dto.response.BannerResponse;
import com.ryuqq.setof.application.banneritem.dto.command.CreateBannerItemCommand;
import com.ryuqq.setof.application.banneritem.dto.response.BannerItemResponse;
import com.ryuqq.setof.application.content.dto.command.CreateContentCommand;
import com.ryuqq.setof.application.content.dto.query.SearchContentQuery;
import com.ryuqq.setof.application.content.dto.response.ContentResponse;
import com.ryuqq.setof.application.content.dto.response.ContentSummaryResponse;
import com.ryuqq.setof.application.gnb.dto.command.CreateGnbCommand;
import com.ryuqq.setof.application.gnb.dto.response.GnbResponse;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * V1 → V2 Display Adapter Mapper
 *
 * <p>레거시 V1 API와 신규 V2 UseCase 간의 DTO 변환을 담당
 *
 * <ul>
 *   <li>V1 Request → V2 Command
 *   <li>V2 Response → V1 Response
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class DisplayV1ToV2Mapper {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_INACTIVE = "INACTIVE";
    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

    // ========================================
    // GNB Mapping
    // ========================================

    /**
     * V1 GNB 등록 요청 → V2 CreateGnbCommand 변환
     *
     * @param request V1 요청
     * @return V2 Command
     */
    public List<CreateGnbCommand> toGnbCommands(UpdateGnbV1ApiRequest request) {
        return request.items().stream()
                .map(
                        item ->
                                new CreateGnbCommand(
                                        item.name(), item.linkUrl(), item.sortOrder(), null, null))
                .toList();
    }

    /**
     * V2 GnbResponse → V1 GnbV1ApiResponse 변환
     *
     * @param response V2 응답
     * @return V1 응답
     */
    public GnbV1ApiResponse toGnbV1Response(GnbResponse response) {
        return new GnbV1ApiResponse(
                response.gnbId(),
                response.title(),
                response.linkUrl(),
                response.displayOrder(),
                isActive(response.status()),
                toLocalDateTime(response.createdAt()),
                toLocalDateTime(response.updatedAt()));
    }

    /**
     * V2 GnbResponse 목록 → V1 GnbV1ApiResponse 목록 변환
     *
     * @param responses V2 응답 목록
     * @return V1 응답 목록
     */
    public List<GnbV1ApiResponse> toGnbV1Responses(List<GnbResponse> responses) {
        return responses.stream().map(this::toGnbV1Response).toList();
    }

    // ========================================
    // Content Mapping
    // ========================================

    /**
     * V1 Content 등록 요청 → V2 CreateContentCommand 변환
     *
     * <p>V1에는 imageUrl, displayStartDate, displayEndDate가 없으므로 null로 설정
     *
     * @param request V1 요청
     * @return V2 Command
     */
    public CreateContentCommand toContentCommand(CreateContentV1ApiRequest request) {
        return new CreateContentCommand(request.title(), request.content(), null, null, null);
    }

    /**
     * V2 ContentResponse → V1 ContentV1ApiResponse 변환
     *
     * @param response V2 응답
     * @return V1 응답
     */
    public ContentV1ApiResponse toContentV1Response(ContentResponse response) {
        return new ContentV1ApiResponse(
                response.contentId(),
                response.title(),
                response.memo(),
                isActive(response.status()),
                toLocalDateTime(response.createdAt()),
                toLocalDateTime(response.updatedAt()));
    }

    /**
     * V2 ContentResponse 목록 → V1 ContentV1ApiResponse 목록 변환
     *
     * @param responses V2 응답 목록
     * @return V1 응답 목록
     */
    public List<ContentV1ApiResponse> toContentV1Responses(List<ContentResponse> responses) {
        return responses.stream().map(this::toContentV1Response).toList();
    }

    // ========================================
    // Banner Mapping
    // ========================================

    /**
     * V1 Banner 등록 요청 → V2 CreateBannerCommand 변환
     *
     * <p>V1에는 displayStartDate, displayEndDate가 없으므로 기본값 설정 (현재 ~ 100년 후)
     *
     * @param request V1 요청
     * @return V2 Command
     */
    public CreateBannerCommand toBannerCommand(CreateBannerV1ApiRequest request) {
        Instant now = Instant.now();
        Instant farFuture = now.plusSeconds(100L * 365 * 24 * 60 * 60);
        return new CreateBannerCommand(request.bannerName(), request.bannerType(), now, farFuture);
    }

    /**
     * V1 Banner 수정 요청 → V2 UpdateBannerCommand 변환
     *
     * <p>V1에는 displayStartDate, displayEndDate가 없으므로 기본값 설정 (현재 ~ 100년 후)
     *
     * @param bannerId 배너 ID
     * @param request V1 요청
     * @return V2 Command
     */
    public UpdateBannerCommand toUpdateBannerCommand(
            long bannerId, CreateBannerV1ApiRequest request) {
        Instant now = Instant.now();
        Instant farFuture = now.plusSeconds(100L * 365 * 24 * 60 * 60);
        return new UpdateBannerCommand(BannerId.of(bannerId), request.bannerName(), now, farFuture);
    }

    /**
     * V2 BannerResponse → V1 BannerV1ApiResponse 변환
     *
     * @param response V2 응답
     * @return V1 응답
     */
    public BannerV1ApiResponse toBannerV1Response(BannerResponse response) {
        return new BannerV1ApiResponse(
                response.bannerId(),
                response.title(),
                response.bannerType(),
                isActive(response.status()),
                null,
                toLocalDateTime(response.createdAt()),
                toLocalDateTime(response.updatedAt()));
    }

    /**
     * V2 BannerResponse 목록 → V1 BannerV1ApiResponse 목록 변환
     *
     * @param responses V2 응답 목록
     * @return V1 응답 목록
     */
    public List<BannerV1ApiResponse> toBannerV1Responses(List<BannerResponse> responses) {
        return responses.stream().map(this::toBannerV1Response).toList();
    }

    // ========================================
    // Utility Methods
    // ========================================

    /**
     * Status 문자열을 Boolean displayYn으로 변환
     *
     * @param status V2 상태 문자열 (ACTIVE/INACTIVE)
     * @return V1 displayYn (true/false)
     */
    private Boolean isActive(String status) {
        return STATUS_ACTIVE.equals(status);
    }

    /**
     * Boolean displayYn을 Status 문자열로 변환
     *
     * @param displayYn V1 표시 여부
     * @return V2 상태 문자열
     */
    public String toStatus(Boolean displayYn) {
        return Boolean.TRUE.equals(displayYn) ? STATUS_ACTIVE : STATUS_INACTIVE;
    }

    /**
     * Instant → LocalDateTime 변환
     *
     * @param instant V2 시간
     * @return V1 시간
     */
    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, SYSTEM_ZONE);
    }

    /**
     * LocalDateTime → Instant 변환
     *
     * @param localDateTime V1 시간
     * @return V2 시간
     */
    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(SYSTEM_ZONE).toInstant();
    }

    // ========================================
    // Query Mapping (V1 Filter → V2 Query)
    // ========================================

    /**
     * V1 ContentFilter → V2 SearchContentQuery 변환
     *
     * @param filter V1 필터
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return V2 Query
     */
    public SearchContentQuery toContentQuery(ContentFilterV1ApiRequest filter, int page, int size) {
        String status = null;
        if (filter != null && filter.displayYn() != null) {
            status = toStatus(filter.displayYn());
        }
        String title = filter != null ? filter.title() : null;
        return new SearchContentQuery(title, status, null, page, size);
    }

    /**
     * V1 BannerFilter → V2 SearchBannerQuery 변환
     *
     * @param filter V1 필터
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return V2 Query
     */
    public SearchBannerQuery toBannerQuery(BannerFilterV1ApiRequest filter, int page, int size) {
        String bannerType = filter != null ? filter.bannerType() : null;
        String status = null;
        if (filter != null && filter.displayYn() != null) {
            status = toStatus(filter.displayYn());
        }
        int offset = page * size;
        return new SearchBannerQuery(bannerType, status, null, offset, size);
    }

    // ========================================
    // Content Response Mapping (Summary)
    // ========================================

    /**
     * V2 ContentSummaryResponse → V1 ContentV1ApiResponse 변환
     *
     * @param response V2 요약 응답
     * @return V1 응답
     */
    public ContentV1ApiResponse toContentSummaryV1Response(ContentSummaryResponse response) {
        return new ContentV1ApiResponse(
                response.contentId(),
                response.title(),
                null,
                isActive(response.status()),
                null,
                null);
    }

    /**
     * V2 ContentSummaryResponse 목록 → V1 ContentV1ApiResponse 목록 변환
     *
     * @param responses V2 응답 목록
     * @return V1 응답 목록
     */
    public List<ContentV1ApiResponse> toContentSummaryV1Responses(
            List<ContentSummaryResponse> responses) {
        return responses.stream().map(this::toContentSummaryV1Response).toList();
    }

    /**
     * V2 ContentResponse → V1 ContentGroupV1ApiResponse 변환
     *
     * <p>V2에서는 컴포넌트가 분리되어 있으므로 items는 빈 리스트로 설정
     *
     * @param response V2 응답
     * @return V1 그룹 응답
     */
    public ContentGroupV1ApiResponse toContentGroupV1Response(ContentResponse response) {
        return new ContentGroupV1ApiResponse(
                response.contentId(),
                response.title(),
                response.memo(),
                isActive(response.status()),
                toLocalDateTime(response.createdAt()),
                toLocalDateTime(response.updatedAt()),
                Collections.emptyList());
    }

    // ========================================
    // Banner Response Mapping
    // ========================================

    /**
     * V2 BannerResponse → V1 BannerGroupV1ApiResponse 변환
     *
     * @param response V2 응답
     * @return V1 그룹 응답
     */
    public BannerGroupV1ApiResponse toBannerGroupV1Response(BannerResponse response) {
        return new BannerGroupV1ApiResponse(
                response.bannerId(),
                response.title(),
                response.bannerType(),
                isActive(response.status()),
                toLocalDateTime(response.createdAt()),
                toLocalDateTime(response.updatedAt()));
    }

    /**
     * V2 BannerResponse 목록 → V1 BannerGroupV1ApiResponse 목록 변환
     *
     * @param responses V2 응답 목록
     * @return V1 그룹 응답 목록
     */
    public List<BannerGroupV1ApiResponse> toBannerGroupV1Responses(List<BannerResponse> responses) {
        return responses.stream().map(this::toBannerGroupV1Response).toList();
    }

    // ========================================
    // BannerItem Mapping
    // ========================================

    /**
     * V1 BannerItem 등록 요청 → V2 CreateBannerItemCommand 변환
     *
     * @param request V1 요청
     * @return V2 Command
     */
    public CreateBannerItemCommand toBannerItemCommand(CreateBannerItemV1ApiRequest request) {
        return new CreateBannerItemCommand(
                request.bannerId(),
                null,
                request.imageUrl(),
                request.linkUrl(),
                request.sortOrder(),
                null,
                null,
                null,
                null);
    }

    /**
     * V1 BannerItem 등록 요청 목록 → V2 Command 목록 변환
     *
     * @param requests V1 요청 목록
     * @return V2 Command 목록
     */
    public List<CreateBannerItemCommand> toBannerItemCommands(
            List<CreateBannerItemV1ApiRequest> requests) {
        return requests.stream().map(this::toBannerItemCommand).toList();
    }

    /**
     * V2 BannerItemResponse → V1 BannerItemV1ApiResponse 변환
     *
     * @param response V2 응답
     * @return V1 응답
     */
    public BannerItemV1ApiResponse toBannerItemV1Response(BannerItemResponse response) {
        return new BannerItemV1ApiResponse(
                response.bannerItemId(),
                response.bannerId(),
                response.imageUrl(),
                response.linkUrl(),
                response.displayOrder(),
                toLocalDateTime(response.createdAt()),
                toLocalDateTime(response.createdAt()));
    }

    /**
     * V2 BannerItemResponse 목록 → V1 BannerItemV1ApiResponse 목록 변환
     *
     * @param responses V2 응답 목록
     * @return V1 응답 목록
     */
    public List<BannerItemV1ApiResponse> toBannerItemV1Responses(
            List<BannerItemResponse> responses) {
        return responses.stream().map(this::toBannerItemV1Response).toList();
    }
}
