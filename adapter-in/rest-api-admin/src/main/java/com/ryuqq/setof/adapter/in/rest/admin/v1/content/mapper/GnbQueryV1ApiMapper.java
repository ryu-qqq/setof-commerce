package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SearchGnbsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.GnbV1ApiResponse;
import com.ryuqq.setof.application.navigation.dto.query.NavigationMenuSearchParams;
import com.ryuqq.setof.domain.navigation.aggregate.NavigationMenu;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * GnbQueryV1ApiMapper - v1 GNB Query API 변환 매퍼.
 *
 * <p>Domain 객체(NavigationMenu)를 레거시 v1 GNB 응답 DTO로 변환합니다.
 *
 * <p>변환 규칙:
 *
 * <ul>
 *   <li>active boolean → displayYn "Y"/"N"
 *   <li>Instant (UTC) → LocalDateTime (KST)
 *   <li>navigationMenuId → gnbId
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class GnbQueryV1ApiMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    /**
     * NavigationMenu → GnbV1ApiResponse 변환.
     *
     * @param menu 네비게이션 메뉴 도메인 객체
     * @return GnbV1ApiResponse
     */
    public GnbV1ApiResponse toGnbResponse(NavigationMenu menu) {
        GnbV1ApiResponse.DisplayPeriodResponse displayPeriod =
                GnbV1ApiResponse.DisplayPeriodResponse.of(
                        toLocalDateTime(menu.displayPeriod().startDate()),
                        toLocalDateTime(menu.displayPeriod().endDate()));

        GnbV1ApiResponse.GnbDetailsResponse details =
                GnbV1ApiResponse.GnbDetailsResponse.of(
                        menu.title(),
                        menu.linkUrl(),
                        menu.displayOrder(),
                        displayPeriod,
                        toDisplayYn(menu.isActive()));

        return GnbV1ApiResponse.of(menu.id().value(), details);
    }

    /**
     * SearchGnbsV1ApiRequest → NavigationMenuSearchParams 변환.
     *
     * @param request v1 GNB 검색 요청 DTO
     * @return Application 검색 파라미터
     */
    public NavigationMenuSearchParams toSearchParams(SearchGnbsV1ApiRequest request) {
        return new NavigationMenuSearchParams(
                toInstant(request.startDate()), toInstant(request.endDate()));
    }

    private Instant toInstant(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return ldt.atZone(KST).toInstant();
    }

    private String toDisplayYn(boolean active) {
        return active ? "Y" : "N";
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(KST).toLocalDateTime();
    }
}
