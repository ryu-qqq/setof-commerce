package com.ryuqq.setof.application.banner.assembler;

import com.ryuqq.setof.application.banner.dto.response.BannerResponse;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Banner Assembler
 *
 * <p>Domain → Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerAssembler {

    /**
     * Banner 도메인 → BannerResponse 변환
     *
     * @param banner 배너 도메인
     * @return BannerResponse DTO
     */
    public BannerResponse toResponse(Banner banner) {
        DisplayPeriod displayPeriod = banner.displayPeriod();
        Instant displayStartDate = displayPeriod != null ? displayPeriod.startDate() : null;
        Instant displayEndDate = displayPeriod != null ? displayPeriod.endDate() : null;

        return BannerResponse.of(
                banner.id() != null ? banner.id().value() : null,
                banner.title() != null ? banner.title().value() : null,
                banner.bannerType() != null ? banner.bannerType().name() : null,
                banner.status() != null ? banner.status().name() : null,
                displayStartDate,
                displayEndDate,
                banner.createdAt(),
                banner.updatedAt());
    }

    /**
     * Banner 목록 → BannerResponse 목록 변환
     *
     * @param banners 배너 목록
     * @return BannerResponse 목록
     */
    public List<BannerResponse> toResponses(List<Banner> banners) {
        return banners.stream().map(this::toResponse).toList();
    }
}
