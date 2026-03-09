package com.ryuqq.setof.adapter.in.rest.v1.banner.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.banner.dto.response.BannerSlideV1ApiResponse;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerV1ApiMapper - 배너 V1 API Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Domain → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerV1ApiMapper {

    /**
     * BannerSlide 목록 → BannerSlideV1ApiResponse 목록 변환.
     *
     * @param slides BannerSlide 목록
     * @return BannerSlideV1ApiResponse 목록
     */
    public List<BannerSlideV1ApiResponse> toListResponse(List<BannerSlide> slides) {
        return slides.stream().map(this::toResponse).toList();
    }

    /**
     * BannerSlide → BannerSlideV1ApiResponse 변환.
     *
     * @param slide BannerSlide
     * @return BannerSlideV1ApiResponse
     */
    public BannerSlideV1ApiResponse toResponse(BannerSlide slide) {
        return new BannerSlideV1ApiResponse(
                slide.idValue(), slide.title(), slide.imageUrl(), slide.linkUrl());
    }
}
