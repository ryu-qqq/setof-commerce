package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerGroupV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.response.BannerItemV1ApiResponse;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * BannerQueryV1ApiMapper - v1 배너 Query API 변환 매퍼.
 *
 * <p>Domain 객체(BannerGroup, BannerSlide)를 레거시 v1 응답 DTO로 변환합니다.
 *
 * <p>변환 규칙:
 *
 * <ul>
 *   <li>active boolean → displayYn "Y"/"N"
 *   <li>Instant (UTC) → LocalDateTime (KST)
 *   <li>BannerType enum → String name()
 *   <li>bannerGroupId → bannerId
 *   <li>bannerSlideId → bannerItemId
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerQueryV1ApiMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    /**
     * BannerGroup → BannerGroupV1ApiResponse 변환.
     *
     * <p>레거시 응답에는 insertOperator/updateOperator가 포함되지만, 새 도메인에는 없으므로 빈 문자열로 반환합니다.
     *
     * @param group 배너 그룹 도메인 객체
     * @return BannerGroupV1ApiResponse
     */
    public BannerGroupV1ApiResponse toBannerGroupResponse(BannerGroup group) {
        return BannerGroupV1ApiResponse.of(
                group.id().value(),
                group.title(),
                group.bannerType().name(),
                BannerGroupV1ApiResponse.DisplayPeriodResponse.of(
                        toLocalDateTime(group.displayPeriod().startDate()),
                        toLocalDateTime(group.displayPeriod().endDate())),
                toDisplayYn(group.isActive()),
                toLocalDateTime(group.createdAt()),
                toLocalDateTime(group.updatedAt()),
                "",
                "");
    }

    /**
     * BannerSlide → BannerItemV1ApiResponse 변환.
     *
     * @param slide 배너 슬라이드 도메인 객체
     * @param bannerType 배너 그룹의 타입
     * @return BannerItemV1ApiResponse
     */
    public BannerItemV1ApiResponse toBannerItemResponse(BannerSlide slide, BannerType bannerType) {
        return BannerItemV1ApiResponse.of(
                slide.id().value(),
                bannerType.name(),
                slide.title(),
                BannerItemV1ApiResponse.DisplayPeriodResponse.of(
                        toLocalDateTime(slide.displayPeriod().startDate()),
                        toLocalDateTime(slide.displayPeriod().endDate())),
                slide.imageUrl(),
                slide.linkUrl(),
                slide.displayOrder(),
                toDisplayYn(slide.isActive()),
                BannerItemV1ApiResponse.ImageSizeResponse.of(0.0, 0.0));
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
