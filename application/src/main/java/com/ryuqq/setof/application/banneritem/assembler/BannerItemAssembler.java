package com.ryuqq.setof.application.banneritem.assembler;

import com.ryuqq.setof.application.banneritem.dto.response.BannerItemResponse;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerItemAssembler - Domain → Response DTO 변환
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerItemAssembler {

    /**
     * Domain → Response DTO 변환
     *
     * @param domain BannerItem Domain
     * @return BannerItemResponse
     */
    public BannerItemResponse toResponse(BannerItem domain) {
        return new BannerItemResponse(
                domain.id() != null ? domain.id().value() : null,
                domain.bannerId().value(),
                domain.title() != null ? domain.title().value() : null,
                domain.imageUrl().value(),
                domain.linkUrl() != null ? domain.linkUrl().value() : null,
                domain.displayOrder() != null ? domain.displayOrder().value() : 0,
                domain.status().name(),
                domain.displayPeriod() != null ? domain.displayPeriod().startDate() : null,
                domain.displayPeriod() != null ? domain.displayPeriod().endDate() : null,
                domain.imageSize() != null && domain.imageSize().width() != null
                        ? domain.imageSize().width().intValue()
                        : null,
                domain.imageSize() != null && domain.imageSize().height() != null
                        ? domain.imageSize().height().intValue()
                        : null,
                domain.createdAt());
    }

    /**
     * Domain 목록 → Response DTO 목록 변환
     *
     * @param domains BannerItem Domain 목록
     * @return BannerItemResponse 목록
     */
    public List<BannerItemResponse> toResponseList(List<BannerItem> domains) {
        return domains.stream().map(this::toResponse).toList();
    }
}
